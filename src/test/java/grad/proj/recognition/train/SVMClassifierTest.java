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
		Scanner scanner = new Scanner(new FileInputStream("splice_scale.txt"));
		List<List<Double> > featureVectors = new ArrayList<List<Double>>();
		List<Double> featureVector;
		List<Double> rightFeatureVector = null;
		List<Double> wrongFeatureVector = null;
		
		int classNum = scanner.nextInt();
		int vectorNum = scanner.nextInt();
		int featureNum = scanner.nextInt();
		
		while(scanner.hasNextInt()){
			int classLabel = scanner.nextInt();
			int idx=1;
			featureVector = new ArrayList<Double>();
			while(!scanner.hasNextInt()&&scanner.hasNext()&&(idx<=featureNum)){
				String temp = scanner.next();
				int colonIdx = temp.indexOf(':');
				int tempIdx = Integer.valueOf(temp.substring(0,colonIdx));
				double val = Double.valueOf(temp.substring(colonIdx+1));
				for(;idx<tempIdx;++idx)
					featureVector.add(0.0);
				featureVector.add(val);
				++idx;
			}
			
			for(;idx<=featureNum;++idx)
				featureVector.add(0.0);
			featureVectors.add(featureVector);
		}
		scanner.close();
		
		rightFeatureVector = featureVectors.get(0);
		wrongFeatureVector = new ArrayList<Double>();
		for(int i=0;i<featureNum;++i)
			wrongFeatureVector.add(new Random().nextDouble());
		
		SVMClassifier classifier = new SVMClassifier();
		classifier.train(featureVectors);
		System.out.println(classifier.classify(rightFeatureVector));
		System.out.println(classifier.classify(wrongFeatureVector));
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
