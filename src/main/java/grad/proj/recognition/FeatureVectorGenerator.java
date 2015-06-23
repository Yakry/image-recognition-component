package grad.proj.recognition;

import java.util.List;
import java.util.Map;

import grad.proj.utils.imaging.Image;

public interface FeatureVectorGenerator {
	List<Double> generateFeatureVector(Image image);
	void prepareGenerator(Map<String, List<Image>> trainingSet);
	
	int getFeatureVectorSize();
}
