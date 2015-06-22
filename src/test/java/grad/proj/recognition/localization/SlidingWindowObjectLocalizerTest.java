package grad.proj.recognition.localization;

import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.ImageClassifier;
import grad.proj.recognition.train.impl.LinearNormalizer;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;
import grad.proj.utils.DataSetsTestsHelper;
import grad.proj.utils.DataSetsTestsHelper.DataSet;
import grad.proj.utils.DataSetsTestsHelper.Type;

import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import org.junit.Test;

public class SlidingWindowObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	@Test
	public void testSlidingWindow() throws Exception {
		// saved generator and features
		DataSet dataset = DataSet.calteckUniversity;
		ImageClassifier classifier = DataSetsTestsHelper.getTrainedClassifier(dataset);

		DataSetLoader dataSetLoader = DataSetsTestsHelper.getDataSetLoader(dataset);
		List<Image> testingClassData = dataSetLoader.loadClassImages( Type.Test, "bikes");
				
		ObjectLocalizer localizer = new SlidingWindowObjectLocalizer();
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingClassData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, 0);
			BranchAndBoundObjectLocalizerTest.drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File("slidingWindow" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}

}
