package grad.proj.recognition.localization;

import grad.proj.recognition.train.ImageClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;

import java.awt.Rectangle;

import org.opencv.ml.SVM;

public interface ObjectLocalizer {

	public Rectangle getObjectBounds(Image image, ImageClassifier classifier, int classLabel);
}
