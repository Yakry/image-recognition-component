package grad.proj.classification;

import java.io.Serializable;

public interface FeatureVector extends Iterable<Double>, Serializable {
	public double get(int index);
	public void set(int index, double value);
	public int size();
}
