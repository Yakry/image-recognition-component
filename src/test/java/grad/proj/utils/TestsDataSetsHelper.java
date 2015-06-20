package grad.proj.utils;

import java.io.File;
import java.util.List;

public class TestsDataSetsHelper {
	/*
	 * the datasets should be stored in the project root folder in the following hierarchy
	 * 	datasets
	 * 		images
	 * 			apple
	 * 				train
	 * 				test
	 * 			bikes
	 * 				train
	 * 				test
	 * 		featuresVectors
	 * 				train.txt
	 * 				test.txt
	 * 
	 * the images folder is ignored from the source control
	 * to avoid bloating the repo with binary files
	 */
	
	private static File DATASET_FILE = new File("datasets");
	private static File IMAGE_DATASET_FILE = new File(DATASET_FILE, "images");
	private static File FEATURES_VECTORS_DATASET = new File(DATASET_FILE, "featuresVectors");

	// path relative to local machine
	public static String DATA_FILES_PATH = "E:\\dataset";
	public static String CLASSIFIER_FILES_PATH = DATA_FILES_PATH + "\\classifierFiles";
	public static String SYSTEM_FILES_PATH = DATA_FILES_PATH + "\\system";
	
	public enum ImageDataSet{
		apple,
		bikes,
		can,
		mug,
		sculpt,
		shoe,
		teddyBear,
		toy1,
		trainToy2,
		all
	}
	
	public enum FeaturesVectorsDataSets{
		satimage,
		svmguide4,
		vowel
	}
	
	public enum Type{
		Train,
		Test
	}

	public static List<Image> loadImageDataSetTrain(ImageDataSet dataset, Type type){
		return null;
	}

	public static List<List<List<Double>>> loadFeaturesVectosDataSetSeperated(FeaturesVectorsDataSets dataset, Type type){
		File datasetFolder = new File(FEATURES_VECTORS_DATASET, dataset.toString());
		return DataFileLoader.loadDataSeprated(new File(datasetFolder, type.toString() + ".txt").toString());
	}
	
	public static List<List<Double>> loadFeaturesVectosDataSetCombined(FeaturesVectorsDataSets dataset, Type type){
		File datasetFolder = new File(FEATURES_VECTORS_DATASET, dataset.toString());
		return DataFileLoader.loadDataCombined(new File(datasetFolder, type.toString() + ".txt").toString());
	}
}
