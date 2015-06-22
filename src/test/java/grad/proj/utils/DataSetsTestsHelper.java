package grad.proj.utils;

import grad.proj.recognition.Loader;
import grad.proj.recognition.train.FeatureVectorGenerator;
import grad.proj.recognition.train.ImageClassifier;
import grad.proj.recognition.train.impl.LinearNormalizer;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DataSetsTestsHelper {
	private static File DATASET_FOLDER = new File("datasets");
	private static String FEATURES_FOLDER_NAME = "features";
	
	private static final String FEATURE_VECTOR_GENERATOR_FOLDER = "featureVectorGenerators";
	
	public enum DataSet{
		calteckUniversity,
		satimage,
		svmguide4,
		vowel
	}
	
	public enum Type{
		Train,
		Test
	}

	public static DataSetLoader getDataSetLoader(DataSet dataset) {
		return new DataSetLoader(getDataSetFolder(dataset));
	}
	
	private static File getDataSetFolder(DataSet dataset) {
		return new File(DATASET_FOLDER, dataset.toString());
	}

	public static File getFeaturesFile(DataSet dataset, Type type) {
		File datasetFolder = getDataSetFolder(dataset);
		File features = new File(datasetFolder, FEATURES_FOLDER_NAME);
		File featuresFilePath = new File(features, type.toString() + ".txt");
		return featuresFilePath;
	}
	
	public static File getFeatureVectorGeneratorsFile(DataSet dataset, String name) {
		File datasetFolder = getDataSetFolder(dataset);
		File generators = new File(datasetFolder, FEATURE_VECTOR_GENERATOR_FOLDER);
		File generator = new File(generators, name + ".txt");
		return generator;
	}
	
	
	
	public static List<List<List<Double>>> loadDataSetFeaturesSeperated(DataSet dataset, Type type){
		File datasetFolder = getDataSetFolder(dataset);
		File featuresFolder = new File(datasetFolder, FEATURES_FOLDER_NAME);
		return DataFileLoader.loadDataSeprated(new File(featuresFolder, type.toString() + ".txt").getAbsolutePath());
	}
	
	public static List<List<Double>> loadDataSetFeaturesCombined(DataSet dataset, Type type){
		File datasetFolder = getDataSetFolder(dataset);
		File featuresFolder = new File(datasetFolder, FEATURES_FOLDER_NAME);
		return DataFileLoader.loadDataCombined(new File(featuresFolder, type.toString() + ".txt").toString());
	}
	
	
//	public static void main(String[] args) {
//		Loader.load();
//		generateFeaturesFile(DataSet.calteckUniversity);
//	}

	public static void generateSurfGeneratorFile(DataSet dataset){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		
		List<List<Image>> trainingData = getDataSetLoader(dataset).loadImages(Type.Train);
		
		featureVectorGenerator.prepareGenerator(trainingData);
		
		SurfLoader.saveSurf(featureVectorGenerator, getFeatureVectorGeneratorsFile(dataset, "surf"));
	}
	
	public static SurfFeatureVectorGenerator loadSurfGenerator(DataSet dataset){
		return SurfLoader.loadSurf(getFeatureVectorGeneratorsFile(dataset, "surf"));
	}
	
	public static void generateFeaturesFile(DataSet dataset){
		FeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		
		List<List<Image>> trainingData = getDataSetLoader(dataset).loadImages(Type.Train);;
		
		featureVectorGenerator.prepareGenerator(trainingData);

		File trainFeaturesFile = getFeaturesFile(dataset, Type.Train);
		generateAndWrite(trainingData, featureVectorGenerator, trainFeaturesFile);
		
		List<List<Image>> testingData = getDataSetLoader(dataset).loadImages(Type.Test);;
		File testFeaturesFile = getFeaturesFile(dataset, Type.Test);
		generateAndWrite(testingData, featureVectorGenerator, testFeaturesFile);
	}

	private static void generateAndWrite(List<List<Image>> data,
								  FeatureVectorGenerator featureVectorGenerator,
								  File file){
		try {
			FileWriter featuresFile = new FileWriter(file);
			
			int classesNum = data.size();
			int featuresNum = featureVectorGenerator.getFeatureVectorSize();
			
			featuresFile.write(classesNum + " " + featuresNum + "\n");
			
			int currentLabel = 0;
			for (List<Image> clazz : data) {
				for (Image image : clazz) {
					List<Double> featureVector = featureVectorGenerator.generateFeatureVector(image);
	
					featuresFile.write(currentLabel + " ");
					
					for(Double elem : featureVector)
						featuresFile.write(elem + " ");
					
					featuresFile.write('\n');
				}
				currentLabel++;
			}
			
			featuresFile.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static ImageClassifier getTrainedClassifier(DataSet dataset) {
		SurfFeatureVectorGenerator featureVectorGenerator = DataSetsTestsHelper.loadSurfGenerator(dataset);
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		svmClassifier.train(DataSetsTestsHelper.loadDataSetFeaturesSeperated(dataset, Type.Train));
		
		// doesn't need to be trained
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		return classifier;
	}
	
}
