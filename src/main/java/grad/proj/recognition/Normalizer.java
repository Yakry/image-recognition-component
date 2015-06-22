package grad.proj.recognition;

import java.util.List;

public interface Normalizer {
	public List<List<List<Double>>> reset(List<List<List<Double>>> featureVectors, double rangeMin, double rangeMax);
	public List<Double> normalize(List<Double> featureVector);
}
