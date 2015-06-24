package grad.proj.localization.impl;

import grad.proj.classification.ImageClassifier;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.impl.SlidingWindowObjectLocalizer;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Rectangle;
import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SlidingWindowObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	@Test
	public void testSlidingWindow() throws Exception {
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		ObjectLocalizer localizer = new SlidingWindowObjectLocalizer();
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(DataSet.calteckUniversity);
		Map<String, List<Image>> trainingData = dataSetLoader.loadImages( Type.Train, "mouse", "clipper", "toy1", "toy2");
		List<Image> testingData = dataSetLoader.loadClassImages(Type.Localization, "mouse");
		classifier.train(trainingData);
		
		int searchIndex = 1;
		for(Image image : testingData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(image, classifier, "mouse");
			TestsHelper.drawRectangle(objectBounds, image);
			ImageLoader.saveImage(image, "jpg", new File("slidingWindow" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}
}
