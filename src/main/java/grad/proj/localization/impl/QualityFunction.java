package grad.proj.localization.impl;

import grad.proj.classification.ImageClassifier;
import grad.proj.utils.imaging.Image;

public interface QualityFunction {
	public double evaluate(Image image, SearchState searchState, ImageClassifier classifier, String classLabel);
}
