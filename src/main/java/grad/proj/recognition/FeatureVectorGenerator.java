package grad.proj.recognition;

import java.util.List;

import org.opencv.core.Mat;

import grad.proj.utils.imaging.Image;

public interface FeatureVectorGenerator {
	List<Double> generateFeatureVector(Image image);
	void prepareGenerator(List<List<Image>> trainingSet);
	
	int getFeatureVectorSize();
}
