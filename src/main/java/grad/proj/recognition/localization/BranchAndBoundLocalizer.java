package grad.proj.recognition.localization;

import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;
import grad.proj.utils.SubImage;

import java.awt.Rectangle;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.ml.SVM;

public class BranchAndBoundLocalizer {

	public Rectangle getObjectBounds(Image image, SVM svm, SurfFeatureVectorGenerator featureVectorGenerator) {
		double C = svm.getC();
		Mat supportVectors = svm.getSupportVectors();
		
		MatOfKeyPoint coordinates = featureVectorGenerator.getKeypoints();
		//to access the coordinates of index i
		//coordinates.get(i, 0)[3]
		//coordinates.get(i, 0)[4];
						
		List<List<Integer>> clusterPoints = featureVectorGenerator.getPointIdxsOfClusters();
		// clusterPoints.get(0) means for a list of pointsIdx in cluster 0
		
		// To get a part of the image
		Image part = new SubImage(image, 0, 0, 358, 293);
		
		// feature vector
		Mat featureVector = featureVectorGenerator.generateFeatureVector(part);
		System.out.println(featureVector);

		// use this check before using predicting feature vector  
		// if(featureVector.rows() == 0)
		//	continue;
		
		// to predict a feature vector
		distanceFromMargin(svm, featureVector);
		
		return new Rectangle(0, 0, 0, 0);
	}
	
	private double distanceFromMargin(SVM svm, Mat featureVector){
		Mat predictRes = new Mat();
		svm.predict(featureVector, predictRes, 1);
		
		return predictRes.get(0, 0)[0];
	}
}
