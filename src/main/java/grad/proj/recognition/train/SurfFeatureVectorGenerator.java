package grad.proj.recognition.train;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import grad.proj.Image;

public class SurfFeatureVectorGenerator implements FeatureVectorGenerator {
	
	// No proof it is correct yet
	
	/*
	 * Helpful materials
	 * http://docs.opencv.org/doc/tutorials/features2d/feature_description/feature_description.html
	 */
	
	private int clusterCount;
	private DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
	private DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
	
	private BOWKMeansTrainer trainer;
	private BOWImgDescriptorExtractor imgDescriptor = new BOWImgDescriptorExtractor(extractor, matcher);
	
	private boolean prepared = false;
	
	public SurfFeatureVectorGenerator(){
		this(64);
	}
	
	public SurfFeatureVectorGenerator(int size) {
		this.clusterCount = size;
		
		trainer = new BOWKMeansTrainer(clusterCount);
	}
	
	@Override
	public void prepareGenerator(List<Image> trainingSet){
		
		for(Image image : trainingSet){
			Mat imageMat = generateMatFromImage(image);
			Mat surfDescriptors = generateSurfDescriptors(imageMat);
			
			trainer.add(surfDescriptors);
		}
		
		imgDescriptor.setVocabulary(trainer.cluster());
		
		prepared = true;
	}

	@Override
	public float[] generateFeatureVector(Image image) {
		if(!prepared){
			throw new RuntimeException("Generator not prepared, use SurfFeatureVectorGenerator.generateFeatureVector");
		}
		
		Mat imageMat = generateMatFromImage(image);
		
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		List<List<Integer>> pointIdxsOfClusters = new ArrayList<>();

		FeatureDetector.create(FeatureDetector.SURF).detect(imageMat, keypoints);

		Mat myImgDescriptor = new Mat();
		Mat descriptors = new Mat();
		imgDescriptor.compute(imageMat, keypoints, myImgDescriptor, pointIdxsOfClusters, descriptors);
		
		float[] featureVector = new float[(int) (myImgDescriptor.total() * myImgDescriptor.channels())];
		myImgDescriptor.get(0, 0, featureVector);
		
		return featureVector;
	}

	/**
	 * @param imageMat
	 * @return
	 */
	private Mat generateSurfDescriptors(Mat imageMat) {
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF);
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		detector.detect(imageMat, keypoints);
		
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		
		Mat featureVector = new Mat();
		extractor.compute(imageMat, keypoints, featureVector);
		return featureVector;
	}

	/**
	 * @param image
	 * @return
	 */
	private Mat generateMatFromImage(Image image) {
		/*
		 * 
		 * For more about CvType: http://docs.opencv.org/modules/core/doc/basic_structures.html
		 * basically CV_ 8 UC 3 means a tuple of 3 elements, each element is an unsigned char
		 */
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
	public int getSize() {
		return clusterCount;
	}

}
