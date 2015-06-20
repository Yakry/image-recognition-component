package grad.proj.recognition.train;

import java.io.Serializable;
import java.util.List;

import org.opencv.core.Mat;

public interface Classifier extends Serializable {
	public int classify(List<Double> featureVector);
	public double classify(List<Double> featureVector, int classLabel);
	public void train(List<List<List<Double>>> trainingData);
}
