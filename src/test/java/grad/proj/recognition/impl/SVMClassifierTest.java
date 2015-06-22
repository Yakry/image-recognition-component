package grad.proj.recognition.impl;

import static org.junit.Assert.*;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;
import grad.proj.utils.DataSetLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SVMClassifierTest  extends RequiresLoadingTestBaseClass{
	
	@Test
	public void testSimpleData() {
		List<List<List<Double>>> trainingData = new ArrayList<>(2);

		List<List<Double>> class0 = new ArrayList<>();
		class0.add(Arrays.asList(0.0, 0.0));
		class0.add(Arrays.asList(0.5, 0.5));
		class0.add(Arrays.asList(-0.5, -0.5));

		List<List<Double>> class1 = new ArrayList<>();
		class1.add(Arrays.asList(-1.0, 0.5));
		class1.add(Arrays.asList(-0.5, 1.0));

		trainingData.add(class0);
		trainingData.add(class1);
		
		SVMClassifier classifier = new SVMClassifier(new LinearNormalizer());
		classifier.train(trainingData);
		
		int class0Value1 = classifier.classify(Arrays.asList(0.0, 0.0));
		
		int class0Value2 = classifier.classify(Arrays.asList(-0.25, -0.25));
		
		int class1Value1 = classifier.classify(Arrays.asList(-0.75, 0.75));
		
		assertEquals("class 0 vector 1 not recognized", 0, class0Value1);
		assertEquals("class 0 vector 2 not recognized", 0, class0Value2);
		assertEquals("class 1 vector 1 not recognized", 1, class1Value1);
	}

	@Test
	public void testRealData() {
		// training and testing data already scaled
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(DataSet.satimage);
		List<List<List<Double>>> trainingData = dataSetLoader.loadDataSetFeaturesSeperated(Type.Train);
		List<List<Double>> testingData = dataSetLoader.loadDataSetFeaturesCombined(Type.Test);

		SVMClassifier classifier = new SVMClassifier(new LinearNormalizer());
		classifier.train(trainingData);
		
		double correctLabels = 0;
		
		for(List<Double> testingPair : testingData){
			int classLabel = testingPair.get(0).intValue();
			
			List<Double> testVector = new ArrayList<>();
			
			for(int i = 1; i<testingPair.size(); ++i)
				testVector.add(testingPair.get(i));
				
			double predictedLabel = classifier.classify(testVector);
			
			correctLabels += ((classLabel == predictedLabel)?1:0);
		}
		
		System.out.println("MultiClassSVMClassifierTest::testRealData:");
		System.out.println("number of vectors: " + testingData.size());
		System.out.println("number of correctly classified vectors: " + correctLabels);
		System.out.println("percentage: " + (correctLabels*100)/testingData.size() + "%");
		
		assertTrue("correct predicted labels percentage below 75%",
				((correctLabels*100)/testingData.size()) >= 75.0);
	}
}
