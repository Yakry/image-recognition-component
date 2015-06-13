package grad.proj.recognition.train;

import java.io.Serializable;
import java.util.List;

import org.opencv.core.Mat;

public interface Classifier extends Serializable {
	public int classify(Mat featureVector);
	public double classify(Mat featureVector, int classLabel);
	public void train(List<Mat> trainingData);
}
