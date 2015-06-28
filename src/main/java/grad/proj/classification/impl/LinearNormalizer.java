package grad.proj.classification.impl;

import grad.proj.classification.ArrayFeatureVector;
import grad.proj.classification.FeatureVector;
import grad.proj.classification.Normalizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

	public <CollectionT extends Collection<? extends FeatureVector>> Map<String, Collection<FeatureVector>> reset(
			Map<String, CollectionT> featureVectors, double rangeMin,
			double rangeMax) {
		
		// array of maximum elements in each column in the matrix
		max = new ArrayList<>(featureVectors.size());
		
		// array of minimum elements in each column in the matrix
		min = new ArrayList<>(featureVectors.size());
		
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		
		// get the max and min of each column
		int featureVectorSize = featureVectors.values().iterator().next().iterator().next().size();

		for(int i=0; i<featureVectorSize; i++){
			double columnMax = Integer.MIN_VALUE;
			double columnMin = Integer.MAX_VALUE;
			
			for(Entry<String, CollectionT> clazz : featureVectors.entrySet()){
				for(FeatureVector featureVector : clazz.getValue()){
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
		
		Map<String, Collection<FeatureVector>> scaledFeatureVectors =  new HashMap<>();
		
		for(Entry<String, CollectionT> clazz : featureVectors.entrySet()){
			Collection<FeatureVector> normalizedClass = new ArrayList<FeatureVector>();
			
			for(FeatureVector featureVector : clazz.getValue()){
				normalizedClass.add(normalize(featureVector));
			}
			
			scaledFeatureVectors.put(clazz.getKey(), normalizedClass);
		}
		
		return scaledFeatureVectors;
	}
	
	@Override
	public FeatureVector normalize(FeatureVector featureVector) {
		FeatureVector scaledFeatureVector = new ArrayFeatureVector(featureVector.size());
		
		double range = rangeMax-rangeMin;
		for (int i = 0; i < featureVector.size(); ++i){
			
			double newVal = (((featureVector.get(i) - min.get(i))*range)
				/(max.get(i) - min.get(i))) + rangeMin;
			
			scaledFeatureVector.set(i, newVal);
		}
		return scaledFeatureVector;
	}

}
