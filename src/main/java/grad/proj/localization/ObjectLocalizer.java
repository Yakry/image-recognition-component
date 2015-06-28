package grad.proj.localization;

import grad.proj.classification.Classifier;
import grad.proj.classification.FeatureVectorImageClassifier;
import grad.proj.utils.imaging.Image;

import java.awt.Rectangle;

public interface ObjectLocalizer {

	public static final double DISCARDING_ERROR_THRESHOLD = 0.0;
	public Rectangle getObjectBounds(Image image, Classifier<Image> classifier, String classLabel);
}
