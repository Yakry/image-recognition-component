package grad.proj.recognition.train;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.opencv.core.Core;

public class LinearNormalizerTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	@Test
	public void testNormalizer() {
		List<List<List<Double> > > scaledAllClassData = LinearNormalizerTest.
				loadFeatureVectors("src\\test\\java\\grad\\proj\\recognition\\train\\splice_scale.txt");
		List<List<List<Double> > > unscaledAllClassData = LinearNormalizerTest.
				loadFeatureVectors("src\\test\\java\\grad\\proj\\recognition\\train\\splice.txt");
		
		for(int classLabel = 0; classLabel<scaledAllClassData.size();++classLabel){
			List<List<Double> > scaledfeatureVectors = scaledAllClassData.get(classLabel);
			List<List<Double> > unscaledfeatureVectors = unscaledAllClassData.get(classLabel);
			LinearNormalizer normalizer = new LinearNormalizer();
			List<List<Double> > myScaledfeatureVectors =
					normalizer.reset(unscaledfeatureVectors,-1.0,1.0);
			
			assertTrue("Normalized data has wrong size",
					scaledfeatureVectors.size() == 
					myScaledfeatureVectors.size() );
			
			for(int i=0;i<scaledfeatureVectors.size();++i){
				assertTrue("Input data has wrong size",
						scaledfeatureVectors.get(0).size() == 
						scaledfeatureVectors.get(i).size() );
				assertTrue("Normalized data has wrong feature vector size",
						scaledfeatureVectors.get(i).size() == 
						myScaledfeatureVectors.get(i).size() );
				
				for(int j=0;j<scaledfeatureVectors.get(i).size();++j)
					assertTrue("Normalized data has wrong values " +
							scaledfeatureVectors.get(i).get(j) + "  " +
							myScaledfeatureVectors.get(i).get(j),
							Math.abs( scaledfeatureVectors.get(i).get(j) - 
							myScaledfeatureVectors.get(i).get(j)) <= 1e-6);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static List<List<List<Double> > > loadFeatureVectors(String path){
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(path));
			
			int classNum = scanner.nextInt();
			int vectorNum = scanner.nextInt();
			int featureNum = scanner.nextInt();
			int classCounter = 0;
			
			List<List<List<Double> > > featureVectors = 
					new ArrayList<List<List<Double>>>(classNum);
			List<Double> featureVector;
			
			HashMap<Integer, Integer> classLabelMap = 
					new HashMap<Integer, Integer>();
			
			for(int i=0;i<classNum;++i)
				featureVectors.add(new ArrayList<List<Double>>());
			
			while(scanner.hasNextInt()){
				int inputClassLabel = scanner.nextInt();
				int classLabel;
				if(classLabelMap.get(inputClassLabel) == null){
					classLabel = classCounter++;
					classLabelMap.put(inputClassLabel, classLabel);
				}
				else classLabel = classLabelMap.get(inputClassLabel);
				
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
				featureVectors.get(classLabel).add(featureVector);
			}

			scanner.close();
			return featureVectors;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
