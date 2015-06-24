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

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageClassifierTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testClassifiyOnCaltechDataSet(){
		testClassifiyTrainImages(DataSet.calteckUniversity, "apple", "can");
		//testClassifyingCompleteDataset(DataSet.calteckUniversity);
	}
	@Test
	public void testClassifiyOnMohsenDataSet(){
		testClassifiyTrainImages(DataSet.mohsen);
		testClassifyingCompleteDataset(DataSet.mohsen);
	}
	
	public void testClassifiyTrainImages(DataSet dataSet, String... trainClasses){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Map<String, List<Image>> trainingData = dataSetLoader.loadImages( Type.Train, trainClasses);
		classifier.train(trainingData);
		
		double classifiedCorrectly = 0;
		int totalImages = 0;

		// I couldn't use the testing images because they are for branch and bound, so they contain other objects
		for(Entry<String, List<Image>> classEntry : trainingData.entrySet()){
			String classLabel = classEntry.getKey();
			
			for(Image image : classEntry.getValue()){
				String predictedLabel = classifier.classify(image);
				
				classifiedCorrectly += (predictedLabel.equals(classLabel)) ? 1 : 0;
			}			
			totalImages += classEntry.getValue().size();
		}
		
		classifiedCorrectly /= totalImages;
		
		System.out.println("classifiedCorrectly: " + classifiedCorrectly);
		
		assertTrue("classifiedCorrectly < 90", classifiedCorrectly >= 0.9);
	}
	
	public void testClassifyingCompleteDataset(DataSet dataSet){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Map<String, List<Image>> trainingData = dataSetLoader.loadImages( Type.Train);
		Map<String, List<Image>> testingData = dataSetLoader.loadImages( Type.Test);
		classifier.train(trainingData);
		
		double classifiedCorrectly = 0;
		int totalImages = 0;

		for(Entry<String, List<Image>> classEntry : testingData.entrySet()){
			String classLabel = classEntry.getKey();
			
			for(Image image : classEntry.getValue()){
				String predictedLabel = classifier.classify(image);
				
				classifiedCorrectly += (predictedLabel.equals(classLabel)) ? 1 : 0;
			}			
			totalImages += classEntry.getValue().size();
		}
		
		System.out.println("total images: " + totalImages);
		System.out.println("classifiedCorrectly: " + classifiedCorrectly);
		System.out.println("accuracy: " + classifiedCorrectly / totalImages);
		
		assertTrue("classifiedCorrectly < 75", classifiedCorrectly >= 0.75);
	}
}
