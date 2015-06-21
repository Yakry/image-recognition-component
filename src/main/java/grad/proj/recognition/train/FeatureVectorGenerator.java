package grad.proj.recognition.train;

import java.util.List;

import org.opencv.core.Mat;

import grad.proj.utils.Image;

public interface FeatureVectorGenerator {
	List<Double> generateFeatureVector(Image image);
	void prepareGenerator(List<List<Image>> trainingSet);
	
	int getFeatureVectorSize();
}
