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

import org.junit.Ignore;
import org.junit.Test;

public class SlidingWindowObjectLocalizerTest extends RequiresLoadingTestBaseClass {

//	@Test
//	public void testSlidingWindowOnCaltech(){
//		testSlidingWindowOnOneClass(DataSet.calteckUniversity, new String[] {"apple", "can"}, "apple");
//	}
	
	@Ignore // Slow
	@Test
	public void testSlidingWindowOnMohsen(){
		testSlidingWindowOnOneClass(DataSet.mohsen, "mouse");
	}
	
	public void testSlidingWindowOnOneClass(DataSet dataSet, String testClass) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		ImageClassifier classifier = dataSetLoader.loadTrainedClassifier();
		
		List<Image> testingData = dataSetLoader.loadClassImages(Type.Localization, testClass);
		
		ObjectLocalizer localizer = new SlidingWindowObjectLocalizer();
		
		int searchIndex = 1;
		for(Image image : testingData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(image, classifier, testClass);
			TestsHelper.drawRectangle(objectBounds, image);
			ImageLoader.saveImage(image, "jpg", new File("slidingWindow" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}
}
