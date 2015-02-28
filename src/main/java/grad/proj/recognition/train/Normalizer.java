package grad.proj.recognition.train;

import java.util.List;

public interface Normalizer {
	public List<List<Double>> reset(List<List<Double>> featureVectors);
	public List<Double> normalize(List<Double> featureVector);
}
