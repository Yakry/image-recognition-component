package grad.proj.classification;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Normalizer {
	public <CollectionFeatureVector extends Collection<? extends FeatureVector>> Map<String, Collection<FeatureVector>> reset(Map<String, CollectionFeatureVector> featureVectors, double rangeMin, double rangeMax);
	public FeatureVector normalize(FeatureVector featureVector);
}
