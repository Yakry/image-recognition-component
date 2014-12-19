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
}
