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

	public List<List<List<Double>>> reset(List<List<List<Double>>> featureVectors, double rangeMin, double rangeMax) {
		// array of maximum elements in each column in the matrix
		max = new ArrayList<Double>(featureVectors.size());
		
		// array of minimum elements in each column in the matrix
		min = new ArrayList<Double>(featureVectors.size());
		
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		
		// get the max and min of each column
		int featureVectorSize = featureVectors.get(0).get(0).size();

		for(int i=0; i<featureVectorSize; i++){
			double columnMax = Integer.MIN_VALUE;
			double columnMin = Integer.MAX_VALUE;
			
			for(List<List<Double>> clazz : featureVectors){
				for(List<Double> featureVector : clazz){
					double newnumber = featureVector.get(i);
					if (newnumber > columnMax)
						columnMax = newnumber;
					if (newnumber < columnMin)
						columnMin = newnumber;
				}
			}
			max.add(columnMax);
			min.add(columnMin);
		}
		
		List<List<List<Double>>> scaledFeatureVectors =  new ArrayList<>();
		
		for(List<List<Double>> clazz : featureVectors){
			List<List<Double>> normalizedClass = new ArrayList<List<Double>>();
			
			for(List<Double> featureVector : clazz){
				normalizedClass.add(normalize(featureVector));
			}
			
			scaledFeatureVectors.add(normalizedClass);
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
