package grad.proj.classification;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Classifier<T> extends Serializable {
	public String classify(T instance);
	public double classify(T instance, String classLabel);
	public <CollectionT extends Collection<? extends T>> void train(Map<String, CollectionT> trainingData);
	public Set<String> getClasses();
}
