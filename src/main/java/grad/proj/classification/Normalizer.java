package grad.proj.classification;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Normalizer {
	public <CollectionFeatureVector extends Collection<? extends List<Double>>> Map<String, Collection<List<Double>>> reset(Map<String, CollectionFeatureVector> featureVectors, double rangeMin, double rangeMax);
	public List<Double> normalize(List<Double> featureVector);
}
