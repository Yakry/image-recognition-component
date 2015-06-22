package grad.proj.localization;

import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.imaging.Image;

import java.awt.Rectangle;

import org.opencv.ml.SVM;

public interface ObjectLocalizer {

	public Rectangle getObjectBounds(Image image, ImageClassifier classifier, int classLabel);
}
