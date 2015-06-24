package grad.proj.classification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import grad.proj.utils.imaging.Image;

public interface FeatureVectorGenerator extends Serializable {
	List<Double> generateFeatureVector(Image image);
	void prepareGenerator(Map<String, List<Image>> trainingSet);
	
	int getFeatureVectorSize();
}
