package grad.proj.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataFileLoader {

	public static Map<String, List<List<Double> > > loadDataSeprated(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			int classNum = scanner.nextInt();
			int featureNum = scanner.nextInt();
			Map<String, List<List<Double>>> featureVectors = new HashMap<>();
			List<Double> featureVector;
			
			while(scanner.hasNext()){
				String classLabel = scanner.next();
				featureVector = new ArrayList<Double>(featureNum);
				for(int j=0; j<featureNum; ++j)
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
	
	@SuppressWarnings("unused")
	public static List<SimpleEntry<String, List<Double>>> loadDataCombined(String path){
		try {
			
			Scanner scanner = new Scanner(new FileInputStream(path));
			int classNum = scanner.nextInt();
			int featureNum = scanner.nextInt();
			List<SimpleEntry<String, List<Double>>> featureVectors = new ArrayList<>();
			
			while(scanner.hasNext()){
				List<Double >featureVector = new ArrayList<Double>(featureNum + 1);
				
				String classLabel = scanner.next();
				
				for(int j=0; j<featureNum; ++j)
					featureVector.add(scanner.nextDouble());


				featureVectors.add(new SimpleEntry<>(classLabel, featureVector));
			}

			scanner.close();
			return featureVectors;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
