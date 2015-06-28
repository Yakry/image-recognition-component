package grad.proj.localization.impl;

import java.awt.Rectangle;
import java.util.AbstractMap.SimpleEntry;

import grad.proj.classification.Classifier;
import grad.proj.classification.FeatureVectorImageClassifier;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.imaging.Image;

import org.opencv.core.Mat;

public class SurfLinearSvmQualityFunction implements QualityFunction {
	
	@Override
	public Object preprocess(Image image, Classifier<Image> classifier, String classLabel) {
		
		if(!(classifier instanceof FeatureVectorImageClassifier)){
			throw new RuntimeException("SurfLinearSvmQualityFunction works only with FeatureVectorBasedImageClassifier");
		}
		
		FeatureVectorImageClassifier imageClassifier = (FeatureVectorImageClassifier) classifier;
		
		if(!(imageClassifier.getFeatureVectorGenerator() instanceof SurfFeatureVectorGenerator) || 
			!(imageClassifier.getClassifier() instanceof SVMClassifier) ){
			throw new RuntimeException("SurfLinearSvmQualityFunction works only with Surf generator and Svm Classifier");
		}

		SurfFeatureVectorGenerator generator = (SurfFeatureVectorGenerator) imageClassifier.getFeatureVectorGenerator();
		SVMClassifier svmClassifier = (SVMClassifier) imageClassifier.getClassifier();
		
		Mat supportVector = svmClassifier.getSupportVector(classLabel);
		Mat imageKeypoints = generator.generateFeatureVector(image, true).getValue();
		
		return new SimpleEntry<Mat, Mat>(supportVector, imageKeypoints);
	}
	
	@Override
	public double evaluate(SearchState state, Image image,
						   Classifier<Image> classifier, String classLabel,
						   Object preprocessedInfo) {
		
		SimpleEntry<Mat, Mat> preprocessedInfoEntry = (SimpleEntry<Mat, Mat>) preprocessedInfo;

		Mat supportVector = preprocessedInfoEntry.getKey();
		Mat imageKeypoints = preprocessedInfoEntry.getValue();
		
		int maxRectangleWidth = state.maxCoordinate[SearchState.RIGHT] - 
				state.minCoordinate[SearchState.LEFT];
		int maxRectangleHeight = state.maxCoordinate[SearchState.BOTTOM] - 
				state.minCoordinate[SearchState.TOP];
		int minRectangleWidth = state.minCoordinate[SearchState.RIGHT] - 
				state.maxCoordinate[SearchState.LEFT];
		int minRectangleHeight = state.minCoordinate[SearchState.BOTTOM] - 
				state.maxCoordinate[SearchState.TOP];
		if(minRectangleWidth < 0)
			minRectangleWidth = 0;
		if(minRectangleHeight < 0)
			minRectangleHeight = 0;
		
		Rectangle minRectangle = new Rectangle(state.maxCoordinate[SearchState.LEFT],
				state.maxCoordinate[SearchState.TOP],
				minRectangleWidth, minRectangleHeight);
		Rectangle maxRectangle = new Rectangle(state.minCoordinate[SearchState.LEFT],
				state.minCoordinate[SearchState.TOP],
				maxRectangleWidth, maxRectangleHeight);
		
		double stateQuality = 0.0;
		for(int i = 0;i < imageKeypoints.rows(); ++i){
			if(minRectangle.contains(imageKeypoints.get(i, 0)[0], imageKeypoints.get(i, 1)[0])){
				int clusterIndex = (int)imageKeypoints.get(i, 2)[0];
				double coefficient = supportVector.get(0, clusterIndex)[0]; 
				stateQuality += coefficient;
			}
			else if(maxRectangle.contains(imageKeypoints.get(i, 0)[0], imageKeypoints.get(i, 1)[0])){
				int clusterIndex = (int)imageKeypoints.get(i, 2)[0];
				double coefficient = supportVector.get(0, clusterIndex)[0];
				if(coefficient < 0.0)
					stateQuality += coefficient;
			}
		}
		
		return stateQuality;
	}

	@Override
	public int getMaxIterations() {
		return 10000;
	}
}
