package grad.proj.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataFileLoader {

	public static List<List<List<Double> > > loadDataSeprated(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			int classNum = scanner.nextInt();
			int vectorNum = scanner.nextInt();
			int featureNum = scanner.nextInt();
			List<List<List<Double> > > featureVectors = 
					new ArrayList<List<List<Double>>>(classNum);
			List<Double> featureVector;
			
			for(int i=0;i<classNum;++i)
				featureVectors.add(new ArrayList<List<Double>>());
			
			for(int i=0; i<vectorNum; ++i){
				int classLabel = scanner.nextInt();
				featureVector = new ArrayList<Double>(featureNum);
				for(int j=0; j<featureNum; ++j)
					featureVector.add(scanner.nextDouble());
				
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
	public static List<List<Double> > loadDataCombined(String path){
		try {
			Scanner scanner = new Scanner(new FileInputStream(path));
			int classNum = scanner.nextInt();
			int vectorNum = scanner.nextInt();
			int featureNum = scanner.nextInt();
			List<List<Double> > featureVectors = 
					new ArrayList<List<Double>>(vectorNum);
			List<Double> featureVector;
			
			for(int i=0; i<vectorNum; ++i){
				featureVector = new ArrayList<Double>(featureNum + 1);
				featureVector.add((double) scanner.nextInt());
				for(int j=0; j<featureNum; ++j)
					featureVector.add(scanner.nextDouble());
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
