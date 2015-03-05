package grad.proj.recognition.train;

import java.io.FileInputStream;
import java.util.ArrayList;
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
		List<List<Double> > featureVectors = 
				LinearNormalizerTest.loadFeatureVectors("splice_scale.txt");
		List<Double> rightFeatureVector = null;
		List<Double> wrongFeatureVector = null;
		
		rightFeatureVector = featureVectors.get(0);
		wrongFeatureVector = new ArrayList<Double>();
		for(int i=0;i<featureVectors.get(0).size();++i)
			wrongFeatureVector.add(new Random().nextDouble());
		
		SVMClassifier classifier = new SVMClassifier();
		classifier.train(featureVectors);
		System.out.println(classifier.classify(rightFeatureVector));
		System.out.println(classifier.classify(wrongFeatureVector));
	}
}
