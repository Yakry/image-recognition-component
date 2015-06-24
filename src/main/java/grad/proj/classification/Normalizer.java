package grad.proj.classification;

import java.util.List;
import java.util.Map;

public interface Normalizer {
	public Map<String, List<List<Double>>> reset(Map<String, List<List<Double>>> featureVectors, double rangeMin, double rangeMax);
	public List<Double> normalize(List<Double> featureVector);
}
