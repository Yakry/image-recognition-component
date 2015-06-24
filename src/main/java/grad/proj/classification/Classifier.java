package grad.proj.classification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Classifier<T> extends Serializable {
	public String classify(T instance);
	public double classify(T instance, String classLabel);
	public void train(Map<String, List<T>> trainingData);
	public Set<String> getClasses();
}
