package grad.proj.recognition.train;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.junit.Test;
import org.opencv.core.Core;

public class LinearNormalizerTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	
	
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
