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

	public List<List<Double>> reset(List<List<Double>> featureVectors, double rangeMin, double rangeMax) {
		List<List<Double>> scaledFeatureVectors =  new ArrayList<>();
		
		// array of maximum elements in each column in the matrix
		max = new ArrayList<Double>(featureVectors.size());
		
		// array of minimum elements in each column in the matrix
		min = new ArrayList<Double>(featureVectors.size());
		
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		
		// get the max and min of each column
		int featureVectorSize = featureVectors.get(0).size();
		
		for (int col = 0; col < featureVectorSize; ++col){
			double columnMax = Integer.MIN_VALUE;
			double columnMin = Integer.MAX_VALUE;
			for (int row = 0; row < featureVectors.size(); ++row){
				double newnumber = featureVectors.get(row).get(col);
				if (newnumber > columnMax)
					columnMax = newnumber;
				if (newnumber < columnMin)
					columnMin = newnumber;
			}
			max.add(columnMax);
			min.add(columnMin);
		}
		
		double range = rangeMax-rangeMin;
		for(int i = 0; i < featureVectors.size(); ++i){
			scaledFeatureVectors.add(normalize(featureVectors.get(i)));
		}
		
		return scaledFeatureVectors;
	}
	
	@Override
	public List<Double> normalize(List<Double> featureVector) {
		List<Double> scaledFeatureVector = new ArrayList<>();
		
		double range = rangeMax-rangeMin;
		for (int i = 0; i < featureVector.size(); ++i){
			
			double newVal = (((featureVector.get(i) - min.get(i))*range)
				/(max.get(i) - min.get(i))) + rangeMin;
			
			scaledFeatureVector.add(newVal);
		}
		return scaledFeatureVector;
	}
}
