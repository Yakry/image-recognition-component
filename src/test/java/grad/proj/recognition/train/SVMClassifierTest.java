package grad.proj.recognition.train;

import static org.junit.Assert.*;
import grad.proj.utils.DataFileLoader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.junit.Test;
import org.opencv.core.Core;

@SuppressWarnings("unused")
public class SVMClassifierTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	@Test
	public void testRunTime() throws Exception {
		List<List<List<Double> > > allClassData = DataFileLoader.
				loadIsolatedFeatureVectors("src\\test\\java\\grad\\"
						+ "proj\\recognition\\train\\splice_scale.txt");
		
		for(int classLabel = 0;classLabel<allClassData.size();++classLabel){
			List<List<Double> > featureVectors = allClassData.get(classLabel);
			List<Double> rightFeatureVector = null;
			List<Double> wrongFeatureVector = null;
			
			rightFeatureVector = featureVectors.get(0);
			wrongFeatureVector = new ArrayList<Double>();
			for(int i=0;i<featureVectors.get(0).size();++i)
				wrongFeatureVector.add(new Random().nextDouble());
			
			SVMClassifier classifier = new SVMClassifier();
			classifier.train(featureVectors);
			double rightFeatureVectorValue = classifier.classify(rightFeatureVector);
			double wrongFeatureVectorValue = classifier.classify(wrongFeatureVector);
			System.out.println(rightFeatureVectorValue);
			System.out.println(wrongFeatureVectorValue);
			assertTrue("Right feature vector is not recongized at class " + classLabel,
					rightFeatureVectorValue > 0.0);
			assertTrue("Wrong feature vector is recongized at class " + classLabel,
					wrongFeatureVectorValue == 0.0);
		}
		
		
	}

	@Test
	public void testSimpleData() throws Exception {
		List<List<Double>> featureVectors = new ArrayList<List<Double>>(5000);
		for(int i=0;i<50000;++i)
			featureVectors.add(Arrays.asList((double)i, (double)i));
		
		SVMClassifier s = new SVMClassifier();
		s.train(featureVectors);

		double classificationValue = s.classify(featureVectors.get(0));
		System.out.println("classificationValue Value 1: " + classificationValue);
		assertTrue("Right feature vector is not recongized successfully"
				, classificationValue > 0.0);
		
		classificationValue = s.classify(Arrays.asList(9999.0,9999.0));
		System.out.println("classificationValue Value 2: " + classificationValue);
		assertTrue("Right feature vector is not recongized successfully"
				, classificationValue > 0.0);
		
		classificationValue = s.classify(Arrays.asList(9999.0,9990.0));
		System.out.println("classificationValue Value 3: " + classificationValue);
		assertTrue("Right feature vector is not recongized successfully"
				, classificationValue > 0.0);
		
		classificationValue = s.classify(Arrays.asList(1.0,9990.0));
		System.out.println("classificationValue Value 4: " + classificationValue);
		assertTrue("Wrong feature vector is recongized"
				, classificationValue == 0.0);
	}
}
