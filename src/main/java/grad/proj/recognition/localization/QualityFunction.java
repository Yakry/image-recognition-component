package grad.proj.recognition.localization;

import grad.proj.recognition.train.ImageClassifier;
import grad.proj.utils.Image;

public interface QualityFunction {
	public double evaluate(Image image, SearchState searchState, ImageClassifier classifier, int classLabel);
}
