package grad.proj.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public abstract class FeatureVectorClassifierTest extends RequiresLoadingTestBaseClass {

	public abstract Classifier<FeatureVector> createClassifier();
	
	@Test
	public void testSimpleData() {
		Map<String, List<FeatureVector>> trainingData = new HashMap<>();

		List<FeatureVector> class0 = new ArrayList<>();
		class0.add(new ArrayFeatureVector(0.0, 0.0));
		class0.add(new ArrayFeatureVector(0.5, 0.5));
		class0.add(new ArrayFeatureVector(-0.5, -0.5));

		List<FeatureVector> class1 = new ArrayList<>();
		class1.add(new ArrayFeatureVector(-1.0, 0.5));
		class1.add(new ArrayFeatureVector(-0.5, 1.0));

		trainingData.put("class0", class0);
		trainingData.put("class1", class1);
		
		Classifier<FeatureVector> classifier = createClassifier();
		classifier.train(trainingData);
		
		String class0Value1 = classifier.classify(new ArrayFeatureVector(0.0, 0.0));
		
		String class0Value2 = classifier.classify(new ArrayFeatureVector(-0.25, -0.25));
		
		String class1Value1 = classifier.classify(new ArrayFeatureVector(-0.75, 0.75));
		
		assertEquals("class 0 vector 1 not recognized", "class0", class0Value1);
		assertEquals("class 0 vector 2 not recognized", "class0", class0Value2);
		assertEquals("class 1 vector 1 not recognized", "class1", class1Value1);
	}

	@Test
	public void testOnSatImageDataSet(){
		// training and testing data already scaled for satimage
		testOnDataSet(DataSet.satimage);
	}
	
	@Test
	public void testOncaltechDataSet(){
		testOnDataSet(DataSet.calteckUniversity);
	}
	
	@Test
	public void testOnMohsenDataSet(){
		testOnDataSet(DataSet.mohsen);
	}
	
	public void testOnDataSet(DataSet dataSet) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		Map<String, List<FeatureVector>> trainingData = dataSetLoader.loadFeatures(Type.Train);
		Map<String, List<FeatureVector>> testingData = dataSetLoader.loadFeatures(Type.Test);

		SVMClassifier classifier = new SVMClassifier(new LinearNormalizer());
		classifier.train(trainingData);
		
		double correctLabels = 0;
		int totalVectors = 0;
		for(Entry<String, List<FeatureVector>> classEntry : testingData.entrySet()){
			String classLabel = classEntry.getKey();
			List<FeatureVector> classVectors = classEntry.getValue();
			
			totalVectors += classVectors.size();
			for(FeatureVector testVector : classVectors){
				String predictedLabel = classifier.classify(testVector);
				
				correctLabels += (classLabel.equals(predictedLabel)) ? 1 : 0;
			}
		}
		
		System.out.println(getClass().getSimpleName() + ": ");
		System.out.println("number of vectors: " + totalVectors);
		System.out.println("number of correctly classified vectors: " + correctLabels);
		System.out.println("percentage: " + (correctLabels*100)/totalVectors + "%");
		
		assertTrue("correct predicted labels percentage below 75%",
				((correctLabels*100)/totalVectors) >= 65.0);
	}
}
