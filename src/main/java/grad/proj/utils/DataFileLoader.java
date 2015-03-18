package grad.proj.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DataFileLoader {

	@SuppressWarnings("unused")
	public static List<List<List<Double> > > loadIsolatedFeatureVectors(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			
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
	
	@SuppressWarnings("unused")
	public static List<List<Double> > loadMixedFeatureVectors(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			
			int classNum = scanner.nextInt();
			int vectorNum = scanner.nextInt();
			int featureNum = scanner.nextInt();
			
			List<List<Double> > featureVectors = 
					new ArrayList<List<Double>>(vectorNum);
			List<Double> featureVector;
			
			while(scanner.hasNextInt()){
				int classLabel = scanner.nextInt();
				int idx=1;
				featureVector = new ArrayList<Double>(featureNum+1);
				featureVector.add((double) classLabel);
				while(!scanner.hasNextInt()&&scanner.hasNext()
						&&(idx<=featureNum)){
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
			return featureVectors;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
