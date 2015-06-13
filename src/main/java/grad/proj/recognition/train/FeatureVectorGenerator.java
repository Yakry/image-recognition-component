package grad.proj.recognition.train;

import java.util.List;

import grad.proj.Image;

public interface FeatureVectorGenerator {
	float[] generateFeatureVector(Image image);
	void prepareGenerator(List<Image> trainingSet);
}
