package grad.proj.recognition.train;

import org.opencv.core.Mat;

public interface Normalizer {
	public Mat reset(Mat featureVectors, double rangeMin, double rangeMax);
	public Mat normalize(Mat featureVector);
}
