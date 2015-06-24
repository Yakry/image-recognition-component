package grad.proj.localization;

import grad.proj.classification.ImageClassifier;
import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;
import grad.proj.localization.impl.SurfLinearSvmQualityFunction;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import org.junit.Test;

public abstract class ObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	public abstract ObjectLocalizer createLocalizer();
	
	@Test
	public void testOnCaltech(){
		testOnOneClass(DataSet.calteckUniversity, "apple");
	}
	
	@Test
	public void testOnMohsen(){
		testOnOneClass(DataSet.mohsen, "mouse");
	}
	
	public void testOnOneClass(DataSet dataSet, String testClass) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		ImageClassifier classifier = dataSetLoader.loadTrainedClassifier();
		
		List<Image> testingData = dataSetLoader.loadClassImages(Type.Localization, testClass);
		
		ObjectLocalizer localizer = createLocalizer();
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, testClass);
			TestsHelper.drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File(getClass().getSimpleName() + searchIndex + ".jpg"));
			++searchIndex;
		}
	}
}
