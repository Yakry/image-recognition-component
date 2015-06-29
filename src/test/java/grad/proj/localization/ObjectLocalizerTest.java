package grad.proj.localization;

import grad.proj.classification.Classifier;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public abstract class ObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	private PrintStream currentResultsPrinter;
	
	public abstract ObjectLocalizer createLocalizer();

	@Ignore
	@Test
	public void testOnCaltech(){
		newResults(DataSet.calteckUniversity);
		testOnClasses(DataSet.calteckUniversity);
	}

	@Ignore
	@Test
	public void testOnCaltechCombined(){
		newResults(DataSet.calteckUniversity);
		testCombined(DataSet.calteckUniversity);
	}

	@Test
	public void testOnMohsen1(){
		newResults(DataSet.mohsen1);
		testOnClasses(DataSet.mohsen1);
	}

	@Ignore
	@Test
	public void testOnMohsen1Combined(){
		newResults(DataSet.mohsen1);
		testCombined(DataSet.mohsen1);
	}

	@Test
	public void testOnMohsen2(){
		newResults(DataSet.mohsen2);
		testOnClasses(DataSet.mohsen2);
	}

	@Ignore
	@Test
	public void testOnMohsen2Combined(){
		newResults(DataSet.mohsen2);
		testCombined(DataSet.mohsen2);
	}
	
	public void testCombined(DataSet dataSet) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Classifier<Image> classifier = dataSetLoader.loadTrainedClassifier();
		
		List<SimpleEntry<Image, Map<String, Rectangle>>> testingData = dataSetLoader.loadClassImagesLocalization(DataSetLoader.COMBINED_CLASS);
		
		ObjectLocalizer localizer = createLocalizer();
		
		for(String testClass : classifier.getClasses()){
			int searchIndex = 0;
			for(SimpleEntry<Image, Map<String, Rectangle>> sampleTestImageEntry : testingData){
				Image sampleTestImage = sampleTestImageEntry.getKey();
				Rectangle objectRealBounds = sampleTestImageEntry.getValue().get(testClass);
				testImage(localizer, classifier, testClass, sampleTestImage, testClass + " in " + DataSetLoader.COMBINED_CLASS + " image " + searchIndex, objectRealBounds);
				searchIndex++;
			}
		}
	}
	
	public void testOnClasses(DataSet dataSet, String ...testClasses) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Classifier<Image> classifier = dataSetLoader.loadTrainedClassifier();
		
		Map<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> testingData = dataSetLoader.loadImages(testClasses);
		
		ObjectLocalizer localizer = createLocalizer();
		
		for(Entry<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> clazz : testingData.entrySet()){
			String testClass = clazz.getKey();
			
			int searchIndex = 1;
			for(SimpleEntry<Image, Map<String, Rectangle>> sampleTestImageEntry : clazz.getValue()){
				Image sampleTestImage = sampleTestImageEntry.getKey();
				Map<String, Rectangle> realBounds = sampleTestImageEntry.getValue();
				testImage(localizer, classifier, testClass, sampleTestImage, testClass + " alone " + String.valueOf(searchIndex), realBounds.get(testClass));
				searchIndex++;
			}
		}
	}
	
	private void newResults(DataSet dataset){
		try {
			File f = new File(TestsHelper.getTestResultsFolder(getClass(), ""), DataSet.mohsen2.toString() + ".csv");
			
			if(currentResultsPrinter != null)
				currentResultsPrinter.close();
			
			if(!f.exists())
				f.createNewFile();
			
			currentResultsPrinter = new PrintStream(f);

			currentResultsPrinter.format("%s\t%s\t%s\t%s\t%s\t%s\n", "item class",
																	 "detect type",
																	 "left diff",
																	 "top diff",
																	 "right diff",
																	 "bottom");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testImage(ObjectLocalizer localizer, Classifier<Image> classifier,
						   String testClass, Image sampleTestImage,
						   String savedImageName, Rectangle realBounnds) {

		System.out.println("starting search " + savedImageName + " test class: " + testClass);
		Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, testClass);
		
		if(objectBounds == null && realBounnds != null){
			appendResult(savedImageName, new Rectangle(), new Rectangle(), "missed");
		}
		
		if(objectBounds == null && realBounnds != null){
			appendResult(savedImageName, new Rectangle(), new Rectangle(), "false");
		}
		
		if(objectBounds != null && realBounnds != null){
				TestsHelper.drawRectangle(objectBounds, sampleTestImage);
				
				appendResult(savedImageName, realBounnds, objectBounds, "correct");
		}
		
		File testResultsFolder = TestsHelper.getTestResultsFolder(getClass(), testClass);
		File resultImageFile = new File(testResultsFolder, savedImageName + ".jpg");
		ImageLoader.saveImage(sampleTestImage, "jpg", resultImageFile);
		
	}

	private void appendResult(String savedImageName, Rectangle realBounnds, Rectangle objectBounds, String type) {
			currentResultsPrinter.format("%s\t%s\t%d\t%d\t%d\t%d\n", savedImageName,
													type,
													realBounnds.x - objectBounds.x,
													realBounnds.x - objectBounds.y,
													(realBounnds.x + realBounnds.width) - (objectBounds.x + objectBounds.width),
													(realBounnds.y + realBounnds.width) - (objectBounds.y + objectBounds.height));
	}
}
