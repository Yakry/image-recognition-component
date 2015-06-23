package grad.proj.localization.impl;

import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.impl.SlidingWindowObjectLocalizer;
import grad.proj.recognition.ImageClassifier;
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

import org.junit.Test;

public class SlidingWindowObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	@Test
	public void testSlidingWindow() throws Exception {
		// saved generator and features
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(DataSet.calteckUniversity);
		ImageClassifier classifier = dataSetLoader.loadTrainedClassifier();

		List<Image> testingClassData = dataSetLoader.loadClassImages( Type.Test, "bikes");
				
		ObjectLocalizer localizer = new SlidingWindowObjectLocalizer();
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingClassData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, "0");
			TestsHelper.drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File("slidingWindow" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}

}
