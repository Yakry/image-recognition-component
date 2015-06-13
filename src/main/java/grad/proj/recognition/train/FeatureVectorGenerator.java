package grad.proj.recognition.train;

import java.util.List;

import org.opencv.core.Mat;

import grad.proj.Image;

public interface FeatureVectorGenerator {
	Mat generateFeatureVector(Image image);
	void prepareGenerator(List<Image> trainingSet);
	
	int getSize();
}
