package grad.proj.recognition.train.impl;

import grad.proj.recognition.train.Normalizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class LinearNormalizer implements Normalizer, Serializable {
	private static final long serialVersionUID = 1L;
	private List<Double> max;
	private List<Double> min;
	private double rangeMin;
	private double rangeMax;

	public Mat reset(Mat featureVectors, double rangeMin, double rangeMax) {
		Mat scaledFeatureVectors = new Mat(featureVectors.rows(),
				featureVectors.cols(), CvType.CV_32FC1);
		// array of maximum elements in each column in the matrix
		max = new ArrayList<Double>(featureVectors.cols());
		// array of minimum elements in each column in the matrix
		min = new ArrayList<Double>(featureVectors.cols());
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		// get the max and min of each column
		for (int col = 0; col < featureVectors.cols(); ++col){
			double columnMax = Integer.MIN_VALUE;
			double columnMin = Integer.MAX_VALUE;
			for (int row = 0; row < featureVectors.rows(); ++row){
				double newnumber = featureVectors.get(row, col)[0];
				if (newnumber > columnMax)
					columnMax = newnumber;
				if (newnumber < columnMin)
					columnMin = newnumber;
			}
			max.add(columnMax);
			min.add(columnMin);
		}
		
		double range = rangeMax-rangeMin;
		for(int row = 0; row < featureVectors.rows(); ++row){
			for (int col = 0; col < featureVectors.cols(); ++col){
				double newVal = (((featureVectors.get(row, col)[0] - min.get(col))*range)
					/(max.get(col) - min.get(col))) + rangeMin;
				scaledFeatureVectors.put(row, col, newVal);
			}
		}
		
		return scaledFeatureVectors;
	}
	
	@Override
	public Mat normalize(Mat featureVector) {
		if(featureVector.rows()!=1)
			throw new RuntimeException("the passed argument is not a 1D vector");
		
		Mat scaledFeatureVector = new Mat(1,featureVector.cols(),CvType.CV_32FC1);
		double range = rangeMax-rangeMin;
		for (int i = 0; i < featureVector.cols(); ++i){
			double newVal = (((featureVector.get(0, i)[0] - min.get(i))*range)
				/(max.get(i) - min.get(i))) + rangeMin;
			scaledFeatureVector.put(0, i, newVal);
		}
		
		return scaledFeatureVector;
	}
}
