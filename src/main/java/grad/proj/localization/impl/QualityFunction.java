package grad.proj.localization.impl;

import grad.proj.classification.ImageClassifier;
import grad.proj.utils.imaging.Image;

public interface QualityFunction {
	public Object preprocess(Image image, ImageClassifier classifier, String classLabel);
	
	public double evaluate(SearchState searchState, Image image,
							ImageClassifier classifier, String classLabel,
							Object preprocessedInfo);
	
	public int getMaxIterations();
}
