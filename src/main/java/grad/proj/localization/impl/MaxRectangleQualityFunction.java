package grad.proj.localization.impl;

import java.awt.Rectangle;

import grad.proj.classification.Classifier;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.SubImage;

public class MaxRectangleQualityFunction implements QualityFunction {

	@Override
	public Object preprocess(Image image, Classifier<Image> classifier,
			String classLabel) {
		return null;
	}

	@Override
	public double evaluate(SearchState searchState, Image image,
			Classifier<Image> classifier, String classLabel,
			Object preprocessedInfo) {
		Rectangle maxRectangle = searchState.getRectangle();
		SubImage suimage = new SubImage(image, maxRectangle.x,
				maxRectangle.y, maxRectangle.width, maxRectangle.height);
		return classifier.classify(suimage, classLabel);
	}

	@Override
	public int getMaxIterations() {
		return 10;
	}
}
