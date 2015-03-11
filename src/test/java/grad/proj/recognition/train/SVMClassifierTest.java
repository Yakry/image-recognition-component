package grad.proj.recognition.train;

import static org.junit.Assert.*;

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
		List<List<List<Double> > > allClassData = LinearNormalizerTest.
				loadFeatureVectors("src\\test\\java\\grad\\proj\\recognition\\train\\splice_scale.txt");
		
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
		List<Double> featureVector1 = Arrays.asList(0.5, 0.6);
		List<Double> featureVector2 = Arrays.asList(0.4, 0.7);
		
		List<List<Double>> vecs = Arrays.asList(featureVector1, featureVector2);
		
		SVMClassifier s = new SVMClassifier();
		
		s.train(vecs);

		double classified = s.classify(featureVector1);
		
		System.out.println("Classified Value: " + classified);
		assertTrue("Feature vector is not recongized successfully", classified > 0.0);
	}
}
