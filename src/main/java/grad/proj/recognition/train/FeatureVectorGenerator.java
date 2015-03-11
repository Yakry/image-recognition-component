package grad.proj.recognition.train;

import grad.proj.Image;

public interface FeatureVectorGenerator {
	float[] generateFeatureVector(Image image);
	void prepareGenerator(Image... trainingSet);
}
