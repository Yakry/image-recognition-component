package grad.proj.utils;

import grad.proj.classification.ArrayFeatureVector;
import grad.proj.classification.FeatureVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FeaturesFileLoader {
	
	public static void saveFeatures(Map<String, List<FeatureVector > > data, File path){
		try {
			FileWriter featuresFile = new FileWriter(path);
			
			int featureVectorSize = data.values().iterator().next().get(0).size();
			
			featuresFile.write(featureVectorSize + "\n");
			
			for (Entry<String, List<FeatureVector>> clazz : data.entrySet()) {
				String classLabel = clazz.getKey();
				
				for (FeatureVector featureVector : clazz.getValue()) {
	
					featuresFile.write(classLabel + " ");
					
					for(Double elem : featureVector)
						featuresFile.write(elem + " ");
					
					featuresFile.write('\n');
				}
			}
			
			featuresFile.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, List<FeatureVector>> loadFeatures(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			int featureVectorSize = scanner.nextInt();
			Map<String, List<FeatureVector>> featureVectors = new HashMap<>();
			FeatureVector featureVector;
			
			while(scanner.hasNext()){
				String classLabel = scanner.next();
				featureVector = new ArrayFeatureVector(featureVectorSize);
				for(int j=0; j<featureVectorSize; ++j)
					featureVector.set(j, scanner.nextDouble());
				
				
				List<FeatureVector> clazz = featureVectors.get(classLabel);
				if(clazz == null){
					clazz = new ArrayList<>();
					featureVectors.put(classLabel, clazz);
				}
				clazz.add(featureVector);
			}

			scanner.close();
			return featureVectors;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
