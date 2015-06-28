package grad.proj.localization;

import grad.proj.classification.Classifier;
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
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

public abstract class ObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	public abstract ObjectLocalizer createLocalizer();

	@Test
	public void testOnCaltech(){
		testOnClasses(DataSet.calteckUniversity);
	}

	@Ignore
	@Test
	public void testOnCaltechCombined(){
		testCombined(DataSet.calteckUniversity);
	}

	@Test
	public void testOnMohsen(){
		testOnClasses(DataSet.mohsen);
	}

	@Ignore
	@Test
	public void testOnMohsenCombined(){
		testCombined(DataSet.mohsen);
	}
	
	public void testCombined(DataSet dataSet) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Classifier<Image> classifier = dataSetLoader.loadTrainedClassifier();
		
		List<Image> testingData = dataSetLoader.loadClassImages(Type.Localization, DataSetLoader.COMBINED_CLASS);
		
		ObjectLocalizer localizer = createLocalizer();
		
		for(String clazz : classifier.getClasses()){
			int searchIndex = 0;
			for(Image sampleTestImage : testingData){
				testImage(localizer, classifier, clazz, sampleTestImage, DataSetLoader.COMBINED_CLASS + " " + searchIndex);
				searchIndex++;
			}
		}
	}
	
	public void testOnClasses(DataSet dataSet, String ...testClasses) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Classifier<Image> classifier = dataSetLoader.loadTrainedClassifier();
		
		Map<String, List<Image>> testingData = dataSetLoader.loadImages(Type.Localization, testClasses);
		
		ObjectLocalizer localizer = createLocalizer();
		
		for(Entry<String, List<Image>> clazz : testingData.entrySet()){
			String testClass = clazz.getKey();
			
			int searchIndex = 1;
			for(Image sampleTestImage : clazz.getValue()){
				testImage(localizer, classifier, testClass, sampleTestImage, String.valueOf(searchIndex));
				searchIndex++;
			}
		}
	}

	private void testImage(ObjectLocalizer localizer, Classifier<Image> classifier,
						   String testClass, Image sampleTestImage,
						   String savedImageName) {

		System.out.println("starting search " + savedImageName + " test class: " + testClass);
		Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, testClass);
		TestsHelper.drawRectangle(objectBounds, sampleTestImage);
		
		File testResultsFolder = TestsHelper.getTestResultsFolder(getClass(), testClass);
		File resultImageFile = new File(testResultsFolder, savedImageName + ".jpg");
		ImageLoader.saveImage(sampleTestImage, "jpg", resultImageFile);
	}
}
