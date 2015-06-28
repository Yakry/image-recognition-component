package grad.proj.localization.impl;

import grad.proj.classification.Classifier;
import grad.proj.utils.imaging.Image;

public interface QualityFunction {
	public Object preprocess(Image image, Classifier<Image> classifier, String classLabel);
	
	public double evaluate(SearchState searchState, Image image,
							Classifier<Image> classifier, String classLabel,
							Object preprocessedInfo);
	
	public int getMaxIterations();
}
