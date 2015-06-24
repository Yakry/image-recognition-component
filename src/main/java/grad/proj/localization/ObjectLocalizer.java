package grad.proj.localization;

import grad.proj.classification.ImageClassifier;
import grad.proj.utils.imaging.Image;

import java.awt.Rectangle;

public interface ObjectLocalizer {

	public Rectangle getObjectBounds(Image image, ImageClassifier classifier, String classLabel);
}
