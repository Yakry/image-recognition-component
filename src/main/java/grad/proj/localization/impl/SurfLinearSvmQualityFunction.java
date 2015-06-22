package grad.proj.localization.impl;

import java.awt.Rectangle;

import grad.proj.recognition.ImageClassifier;
import grad.proj.utils.imaging.Image;

import org.opencv.core.Mat;

public class SurfLinearSvmQualityFunction implements QualityFunction {
	
	private Mat supportVector;
	private Mat imageKeypoints;
	
	public SurfLinearSvmQualityFunction(Mat supportVector, Mat imageKeypoints) {
		this.supportVector = supportVector;
		this.imageKeypoints = imageKeypoints;
	}

	@Override
	public double evaluate(Image image, SearchState state, ImageClassifier classifier, int classLabel) {
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

}
