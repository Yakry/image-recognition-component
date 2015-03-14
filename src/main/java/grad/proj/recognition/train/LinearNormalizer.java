package grad.proj.recognition.train;

import java.util.ArrayList;
import java.util.List;

public class LinearNormalizer implements Normalizer {
	private List<Double> max;
	private List<Double> min;
	private double rangeMin;
	private double rangeMax;

	@Override
	public List<List<Double>> reset(List<List<Double>> featureVectors,
			double rangeMin, double rangeMax) {
		List<List<Double>> scaledFeatureVectors =
				new ArrayList<List<Double>>(featureVectors.size());
		// array of maximum elements in each column in the matrix
		max = new ArrayList<Double>(featureVectors.get(0).size());
		// array of minimum elements in each column in the matrix
		min = new ArrayList<Double>(featureVectors.get(0).size());
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;

		// get the max and min of each column
		for (int col = 0; col < featureVectors.get(0).size(); col++){
			double largest = Integer.MIN_VALUE;
			double smallest = Integer.MAX_VALUE;
			for (int row = 0; row < featureVectors.size(); row++){
				double newnumber = featureVectors.get(row).get(col);
				if (newnumber > largest)
					largest = newnumber;
				if (newnumber < smallest)
					smallest = newnumber;
			}
			max.add(largest);
			min.add(smallest);
		}
		
		for (int i = 0; i < featureVectors.size(); i++)
			scaledFeatureVectors.add(this.normalize(featureVectors.get(i)));
		
		return scaledFeatureVectors;
	}

	@Override
	public List<Double> normalize(List<Double> featureVector) {
		List<Double> scaledFeatureVector = 
				new ArrayList<Double>(featureVector.size());
		double range = rangeMax-rangeMin;
		for (int i = 0; i < featureVector.size(); i++)
			scaledFeatureVector.add((((featureVector.get(i) - min.get(i))*range)
					/(max.get(i) - min.get(i))) + rangeMin);
		return scaledFeatureVector;
	}
}
