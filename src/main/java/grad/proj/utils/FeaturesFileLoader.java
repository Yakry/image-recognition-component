package grad.proj.utils;

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
	
	public static void saveFeatures(Map<String, List<List<Double> > > data, File path){
		try {
			FileWriter featuresFile = new FileWriter(path);
			
			int featureVectorSize = data.values().iterator().next().get(0).size();
			
			featuresFile.write(featureVectorSize + "\n");
			
			for (Entry<String, List<List<Double>>> clazz : data.entrySet()) {
				String classLabel = clazz.getKey();
				
				for (List<Double> featureVector : clazz.getValue()) {
	
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
	
	public static Map<String, List<List<Double>>> loadFeatures(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			int featureVectorSize = scanner.nextInt();
			Map<String, List<List<Double>>> featureVectors = new HashMap<>();
			List<Double> featureVector;
			
			while(scanner.hasNext()){
				String classLabel = scanner.next();
				featureVector = new ArrayList<Double>(featureVectorSize);
				for(int j=0; j<featureVectorSize; ++j)
					featureVector.add(scanner.nextDouble());
				
				
				List<List<Double>> clazz = featureVectors.get(classLabel);
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
