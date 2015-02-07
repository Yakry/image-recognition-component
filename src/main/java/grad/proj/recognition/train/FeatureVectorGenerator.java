package grad.proj.recognition.train;

import grad.proj.Image;

interface FeatureVectorGenerator {
	float[] generateFeatureVector(Image image);
}
