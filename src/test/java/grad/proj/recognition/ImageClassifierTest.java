package grad.proj.recognition;

import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageClassifierTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testClassifiySingleAppleAndCanTrainImages(){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(DataSet.calteckUniversity);
		List<List<Image>> trainingData = dataSetLoader.loadImages( Type.Train, "apple", "can");
		classifier.train(trainingData);
		
		double classifiedCorrectly = 0;
		int totalImages = 0;

		// I couldn't use the testing images because they are for branch and bound, so they contain other objects
		for(int clazz=0; clazz < trainingData.size(); clazz++){
			for(Image image : trainingData.get(clazz)){
				classifiedCorrectly += (classifier.classify(image) == clazz) ? 1 : 0;
			}			
			totalImages += trainingData.get(clazz).size();
		}
		classifiedCorrectly /= totalImages;
		
		System.out.println("classifiedCorrectly: " + classifiedCorrectly);
		
		assertTrue("classifiedCorrectly < 90", classifiedCorrectly >= 0.9);
	}
}
