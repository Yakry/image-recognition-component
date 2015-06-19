package grad.proj.recognition.localization;

import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;

import java.awt.Rectangle;
import java.util.List;
import java.util.PriorityQueue;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.ml.SVM;

public class BranchAndBoundLocalizer {

	public Rectangle getObjectBounds(Image image, SVM svm, SurfFeatureVectorGenerator featureVectorGenerator) {
		featureVectorGenerator.generateFeatureVector(image);
		Mat supportVector = svm.getSupportVectors();
		MatOfKeyPoint coordinates = featureVectorGenerator.getKeypoints();
		List<List<Integer>> clusterPoints = featureVectorGenerator.getPointIdxsOfClusters();
		
		// combining key points with cluster index
		Mat keyPoints = new Mat(coordinates.rows(), 3, CvType.CV_32FC1);
		for(int clusterIndex = 0;clusterIndex < clusterPoints.size();++clusterIndex){
			for(int pointIndex : clusterPoints.get(clusterIndex)){
				keyPoints.put(pointIndex, 0, coordinates.get(pointIndex, 0)[3]);
				keyPoints.put(pointIndex, 1, coordinates.get(pointIndex, 0)[4]);
				keyPoints.put(pointIndex, 2, clusterIndex);
			}
		}
		
		PriorityQueue<SearchState> searchQueue = new PriorityQueue<SearchState>();
		int terminationLimit = 300000;
		searchQueue.add(new SearchState(image));
		while(searchQueue.peek().hasSubStates() && (terminationLimit > 0)){
			SearchState subState1 = new SearchState();
			SearchState subState2 = new SearchState();
			searchQueue.poll().split(subState1, subState2);
			subState1.quality = this.evaluateState(subState1, supportVector, keyPoints);
			subState2.quality = this.evaluateState(subState2, supportVector, keyPoints);
			searchQueue.add(subState1);
			searchQueue.add(subState2);
			--terminationLimit;
		}
		
		SearchState target = searchQueue.peek();
		return target.getRectangle();
	}
	
	@SuppressWarnings("unused")
	private double distanceFromMargin(SVM svm, Mat featureVector){
		Mat predictRes = new Mat();
		svm.predict(featureVector, predictRes, 1);
		
		return predictRes.get(0, 0)[0];
	}
	
	private double evaluateState(SearchState state, Mat supportVector, Mat keyPoints){
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
		for(int i = 0;i < keyPoints.rows(); ++i){
			if(minRectangle.contains(keyPoints.get(i, 0)[0], keyPoints.get(i, 1)[0])){
				int clusterIndex = (int)keyPoints.get(i, 2)[0];
				double coefficient = supportVector.get(0, clusterIndex)[0]; 
				stateQuality += coefficient;
			}
			else if(maxRectangle.contains(keyPoints.get(i, 0)[0], keyPoints.get(i, 1)[0])){
				int clusterIndex = (int)keyPoints.get(i, 2)[0];
				double coefficient = supportVector.get(0, clusterIndex)[0];
				if(coefficient < 0.0)
					stateQuality += coefficient;
			}
		}
		
		return stateQuality;
	}
}
