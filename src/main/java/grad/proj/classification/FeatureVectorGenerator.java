package grad.proj.classification;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import grad.proj.utils.imaging.Image;

public interface FeatureVectorGenerator extends Serializable {
	List<Double> generateFeatureVector(Image image);
	<CollectionImage extends Collection<? extends Image>> void prepareGenerator(Map<String, CollectionImage> trainingSet);
	
	int getFeatureVectorSize();
}
