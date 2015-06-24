package grad.proj.classification;

import grad.proj.classification.ImageClassifier;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageClassifierTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testClassifiyOnCaltechDataSet(){
		testClassifyingAccuracyDataset(DataSet.calteckUniversity, Type.Train, "apple");
		//testClassifyingCompleteDataset(DataSet.calteckUniversity);
	}
	
	@Ignore	// Takes a lot of time
	@Test
	public void testClassifiyOnCaltechDataSetBikes(){
		testClassifyingAccuracyDataset(DataSet.calteckUniversity, Type.Test, "bikes");
	}
	
	@Test
	public void testClassifiyOnMohsenDataSet(){
		testClassifyingAccuracyDataset(DataSet.mohsen, Type.Test);
	}
	
	
	public void testClassifyingAccuracyDataset(DataSet dataSet, Type testImagesType, String... classes){
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		ImageClassifier classifier = dataSetLoader.loadTrainedClassifier();
		
		Map<String, List<Image>> testingData = dataSetLoader.loadImages(testImagesType, classes);
		
		double classifiedCorrectly = 0;
		int totalImages = 0;

		for(Entry<String, List<Image>> classEntry : testingData.entrySet()){
			String classLabel = classEntry.getKey();
			
			for(Image image : classEntry.getValue()){
				String predictedLabel = classifier.classify(image);
				
				classifiedCorrectly += (classLabel.equals(predictedLabel)) ? 1 : 0;
			}			
			totalImages += classEntry.getValue().size();
		}
		
		System.out.println("total images: " + totalImages);
		System.out.println("classifiedCorrectly: " + classifiedCorrectly);
		System.out.println("accuracy: " + classifiedCorrectly / totalImages);
		
		assertTrue("classifiedCorrectly < 75", classifiedCorrectly >= 0.75);
	}
}
