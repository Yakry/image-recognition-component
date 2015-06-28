package grad.proj.classification;

public interface FeatureVector extends Iterable<Double> {
	public double get(int index);
	public void set(int index, double value);
	public int size();
}
