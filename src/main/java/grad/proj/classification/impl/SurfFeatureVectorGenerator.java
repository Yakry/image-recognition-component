package grad.proj.classification.impl;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import grad.proj.classification.FeatureVector;
import grad.proj.classification.FeatureVectorGenerator;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.MatConverters;
import grad.proj.utils.opencv.MatListOfListAdapter;

public class SurfFeatureVectorGenerator implements FeatureVectorGenerator {
	
	/*
	 * Helpful materials
	 * http://docs.opencv.org/doc/tutorials/features2d/feature_description/feature_description.html
	 */
	
	private static final long serialVersionUID = 681641512397327289L;
	
	private static FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
	private static DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
	
	// must be per instance because the vocabulary is passed to it 
	private transient DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
	
	// manually serializing it
	private transient BagOfWordsImageDescriptorExtractor imgDescriptor = new BagOfWordsImageDescriptorExtractor(extractor, matcher);
	private transient BagOfWordsKMeansTrainer trainer;
	
	private boolean prepared = false;
	
	
	public SurfFeatureVectorGenerator(){
		this(64);
	}
	
	public SurfFeatureVectorGenerator(int size) {
		trainer = new BagOfWordsKMeansTrainer(size);
	}
	
	@Override
	public <CollectionImage extends Collection<? extends Image>> void prepareGenerator(Map<String, CollectionImage> trainingSet){
		
		for(Entry<String, CollectionImage> clazz : trainingSet.entrySet()){
			for(Image image : clazz.getValue()){
				Mat imageMat = generateMatFromImage(image);
				Mat surfDescriptors = generateSurfDescriptors(imageMat);
				if(surfDescriptors.empty())
					continue;
				trainer.add(surfDescriptors);
			}
		}
		
		if(trainer.getDescriptors().size() > 0){
			imgDescriptor.setVocabulary(trainer.cluster());
		}
		
		prepared = true;
	}
	
	@Override
	public FeatureVector generateFeatureVector(Image image) {
		return generateFeatureVector(image, false).getKey();
	}
	
	public SimpleEntry<FeatureVector, Mat> generateFeatureVector(Image image, boolean getKeypoints) {
		if(!prepared){
			throw new RuntimeException("Generator not prepared, use SurfFeatureVectorGenerator.prepareGenerator");
		}

		Mat imageMat = generateMatFromImage(image);
		
		Mat myImgDescriptor = new Mat();
		Mat descriptors = new Mat();

		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		List<List<Integer>> pointIdxsOfClusters = new ArrayList<>();

		featureDetector.detect(imageMat, keypoints);

		imgDescriptor.compute(imageMat, keypoints, myImgDescriptor, pointIdxsOfClusters, descriptors);
		
		FeatureVector featureVector = MatConverters.MatToFeatureVector(myImgDescriptor);
		
		Mat KeypointsClusterIdx = null;
		if(getKeypoints)
			KeypointsClusterIdx = createKeypointsClusterIdxMat(keypoints, pointIdxsOfClusters);

		return new SimpleEntry<>(featureVector, KeypointsClusterIdx);
	}

	public Mat createKeypointsClusterIdxMat(Mat keyPointsCoordinates, List<List<Integer>> clusterPoints) {
		
		// combining key points with cluster index
		Mat keyPointsAndClustersCombined = new Mat(keyPointsCoordinates.rows(), 3, CvType.CV_32FC1);
		for(int clusterIndex = 0;clusterIndex < clusterPoints.size();++clusterIndex){
			for(int pointIndex : clusterPoints.get(clusterIndex)){
				keyPointsAndClustersCombined.put(pointIndex, 0, keyPointsCoordinates.get(pointIndex, 0)[3]);
				keyPointsAndClustersCombined.put(pointIndex, 1, keyPointsCoordinates.get(pointIndex, 0)[4]);
				keyPointsAndClustersCombined.put(pointIndex, 2, clusterIndex);
			}
		}
		
		return keyPointsAndClustersCombined;
	}

	private Mat generateSurfDescriptors(Mat imageMat) {
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		featureDetector.detect(imageMat, keypoints);
		
		Mat featureVector = new Mat();
		extractor.compute(imageMat, keypoints, featureVector);
		return featureVector;
	}

	private Mat generateMatFromImage(Image image) {
		Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		for(int row=0; row<image.getHeight(); row++){
			for(int col=0; col<image.getWidth(); col++){
				Color c = new Color(image.getPixelAt(row,  col));
				
				// using byte instead of char because char in java is 2bytes
				byte[] data = new byte[3];
				data[0] = (byte)c.getRed();
				data[1] = (byte)c.getGreen();
				data[2] = (byte)c.getBlue();
				
				imageMat.put(row, col, data);
			}
		}
		return imageMat;
	}

	@Override
	public int getFeatureVectorSize() {
		return trainer.getClusterCount();
	}
	
	public Mat getVocab(){
		return imgDescriptor.getVocabulary();
	}
	
	public void setVocab(Mat vocab){
		imgDescriptor.setVocabulary(vocab);
		prepared = true;
	}
	
	// Serialization support
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		Mat vocab = getVocab();
		
		out.defaultWriteObject();
		
		out.writeInt(trainer.getClusterCount());
		out.writeInt(vocab.rows());
		out.writeInt(vocab.cols());
		
		for(int i=0; i<vocab.rows(); i++){
			for(int j=0; j<vocab.cols(); j++){
				out.writeFloat((float) vocab.get(i, j)[0]);
			}
		}
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		int clusterCount = in.readInt();
		
		int rows = in.readInt();
		int cols = in.readInt();

		trainer = new BagOfWordsKMeansTrainer(clusterCount);
		imgDescriptor = new BagOfWordsImageDescriptorExtractor(extractor, matcher);
		
		Mat mat = new Mat(rows, cols, CvType.CV_32F);
		
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				mat.put(i, j, in.readFloat());
			}
		}
		
		setVocab(mat);
	}
}
