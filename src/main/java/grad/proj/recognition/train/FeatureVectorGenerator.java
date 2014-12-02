package grad.proj.recognition.train;

import grad.proj.Image;

interface FeatureVectorGenerator {
	double[] generateFeatureVector(Image image);
}
