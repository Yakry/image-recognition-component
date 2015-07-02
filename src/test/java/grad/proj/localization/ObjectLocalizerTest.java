package grad.proj.localization;

import grad.proj.classification.Classifier;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		testOnClasses(DataSet.calteckUniversity);
	}

	@Ignore
	@Test
	public void testOnCaltechCombined(){
		testCombined(DataSet.calteckUniversity);
	}

	@Test
	public void testOnMohsen1(){
		testOnClasses(DataSet.mohsen1);
	}

	@Ignore
	@Test
	public void testOnMohsen1Combined(){
		testCombined(DataSet.mohsen1);
	}

	@Test
	public void testOnMohsen2(){
		testOnClasses(DataSet.mohsen2);
	}

	@Ignore
	@Test
	public void testOnMohsen2Combined(){
		testCombined(DataSet.mohsen2);
	}
	
	public void testCombined(DataSet dataSet) {
		newResults(dataSet);
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Classifier<Image> classifier = dataSetLoader.loadTrainedClassifier();
		
		List<SimpleEntry<Image, Map<String, Rectangle>>> testingData = dataSetLoader.loadClassImagesLocalization(DataSetLoader.COMBINED_CLASS);
		
		ObjectLocalizer localizer = createLocalizer();
		
		for(String testClass : classifier.getClasses()){
			int searchIndex = 0;
			for(SimpleEntry<Image, Map<String, Rectangle>> sampleTestImageEntry : testingData){
				Image sampleTestImage = sampleTestImageEntry.getKey();
				Rectangle objectRealBounds = sampleTestImageEntry.getValue().get(testClass);
				testImage(localizer, classifier, testClass, dataSet, sampleTestImage, testClass + " in " + DataSetLoader.COMBINED_CLASS + " image " + searchIndex, objectRealBounds);
				searchIndex++;
			}
		}
		
		saveResults();
	}
	
	public void testOnClasses(DataSet dataSet, String ...testClasses) {
		newResults(dataSet);
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Classifier<Image> classifier = dataSetLoader.loadTrainedClassifier();
		
		Map<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> testingData = dataSetLoader.loadImagesLocalization(testClasses);
		
		ObjectLocalizer localizer = createLocalizer();
		
		for(Entry<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> clazz : testingData.entrySet()){
			String testClass = clazz.getKey();
			
			int searchIndex = 1;
			for(SimpleEntry<Image, Map<String, Rectangle>> sampleTestImageEntry : clazz.getValue()){
				Image sampleTestImage = sampleTestImageEntry.getKey();
				Map<String, Rectangle> realBounds = sampleTestImageEntry.getValue();
				Rectangle objBounds = realBounds.get(testClass);
				testImage(localizer, classifier, testClass, dataSet, sampleTestImage, testClass + " alone " + String.valueOf(searchIndex), objBounds);
				searchIndex++;
			}
		}
		
		saveResults();
	}
	
	private void testImage(ObjectLocalizer localizer, Classifier<Image> classifier,
						   String testClass, DataSet dataSet, Image sampleTestImage,
						   String savedImageName, Rectangle realBounnds) {

		System.out.println("starting search " + savedImageName + " test class: " + testClass);
		Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, testClass);
		
		if(objectBounds != null && realBounnds != null){
			TestsHelper.drawRectangle(objectBounds, sampleTestImage, Color.GREEN);
			TestsHelper.drawRectangle(realBounnds, sampleTestImage, Color.BLUE);
			appendResult(savedImageName, realBounnds, objectBounds, "TruePositive");
		}
		
		if(objectBounds != null && realBounnds == null){
			TestsHelper.drawRectangle(objectBounds, sampleTestImage, Color.GREEN);
			appendResult(savedImageName, new Rectangle(0,0,0,0), new Rectangle(0,0,0,0), "FalsePositive");
		}
		
		if(objectBounds == null && realBounnds != null){
			TestsHelper.drawRectangle(realBounnds, sampleTestImage, Color.BLUE);
			appendResult(savedImageName, new Rectangle(0,0,0,0), new Rectangle(0,0,0,0), "FalseNegative");
		}
		if(objectBounds == null && realBounnds == null){
			appendResult(savedImageName, new Rectangle(0,0,0,0), new Rectangle(0,0,0,0), "TrueNegative");
		}
		
		File testResultsFolder = TestsHelper.getTestResultsFolder(getClass(), dataSet.toString() + File.separatorChar + testClass);
		File resultImageFile = new File(testResultsFolder, savedImageName + ".jpg");
		ImageLoader.saveImage(sampleTestImage, "jpg", resultImageFile);
	}

	private void newResults(DataSet dataset){
		try {
			File f = new File(TestsHelper.getTestResultsFolder(getClass(), ""), dataset.toString() + ".txt");
			
			if(!f.exists())
				f.createNewFile();
			
			currentResultsPrinter = new PrintStream(new FileOutputStream(f, true));
			
			currentResultsPrinter.format("%s\t%s\t%s\t%s\t%s\n", "item class",
																	 "detect type",
																	 "real area",
																	 "detected area",
																	 "common area");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveResults() {
		if(currentResultsPrinter != null)
			currentResultsPrinter.close();
	}
	
	private void appendResult(String savedImageName, Rectangle realBounnds, Rectangle objectBounds, String type) {
		Rectangle commonBounds = realBounnds.intersection(objectBounds);
			currentResultsPrinter.format("%s\t%s\t%d\t%d\t%d\t%d\n", savedImageName,
													type,
													realBounnds.getWidth()*realBounnds.getHeight(),
													objectBounds.getWidth()*objectBounds.getHeight(),
													commonBounds.getWidth()*commonBounds.getHeight());
	}
}
