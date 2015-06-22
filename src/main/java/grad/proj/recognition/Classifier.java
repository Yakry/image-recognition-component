package grad.proj.recognition;

import java.io.Serializable;
import java.util.List;

public interface Classifier<T> extends Serializable {
	public int classify(T instance);
	public double classify(T instance, int classLabel);
	public void train(List<List<T>> trainingData);
	public int getClassesNo();
}
