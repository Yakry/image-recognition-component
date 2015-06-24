package grad.proj.classification.impl;

import grad.proj.classification.Normalizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LinearNormalizer implements Normalizer, Serializable {
	private static final long serialVersionUID = 1L;
	private List<Double> max;
	private List<Double> min;
	private double rangeMin;
	private double rangeMax;

	public Map<String, List<List<Double>>> reset(Map<String, List<List<Double>>> featureVectors, double rangeMin, double rangeMax) {
		// array of maximum elements in each column in the matrix
		max = new ArrayList<Double>(featureVectors.size());
		
		// array of minimum elements in each column in the matrix
		min = new ArrayList<Double>(featureVectors.size());
		
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		
		// get the max and min of each column
		int featureVectorSize = featureVectors.values().iterator().next().get(0).size();

		for(int i=0; i<featureVectorSize; i++){
			double columnMax = Integer.MIN_VALUE;
			double columnMin = Integer.MAX_VALUE;
			
			for(Entry<String, List<List<Double>>> clazz : featureVectors.entrySet()){
				for(List<Double> featureVector : clazz.getValue()){
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
		
		Map<String, List<List<Double>>> scaledFeatureVectors =  new HashMap<>();
		
		for(Entry<String, List<List<Double>>> clazz : featureVectors.entrySet()){
			List<List<Double>> normalizedClass = new ArrayList<List<Double>>();
			
			for(List<Double> featureVector : clazz.getValue()){
				normalizedClass.add(normalize(featureVector));
			}
			
			scaledFeatureVectors.put(clazz.getKey(), normalizedClass);
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
