package grad.proj.recognition.localization;

import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;

import java.awt.Rectangle;

import org.opencv.ml.SVM;

public interface ObjectLocalizer {

	public Rectangle getObjectBounds(Image image, SVM svm,
			SurfFeatureVectorGenerator featureVectorGenerator);
}
