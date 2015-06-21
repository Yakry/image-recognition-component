package grad.proj.utils;

import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestsDataSetsHelper {
	/*
	 * the datasets should be stored in the project root folder in the following hierarchy
	 * 	datasets
	 * 		calteckUniversity
	 * 			images
	 * 				apple
	 * 					train
	 * 					test
	 * 				bikes
	 * 					train
	 * 					test
	 * 			features
	 * 				train.txt
	 * 				test.txt
	 * 			classifiers
	 * 				svm.xml
	 * 
	 * 		svmguide4
	 * 			features
	 * 
	 * any images folder is ignored from the source control
	 * to avoid bloating the repo with binary files
	 */
	
	private static File DATASET_FILE = new File("datasets");
	private static String IMAGES_FOLDER_NAME = "images";
	private static String FEATURES_FOLDER_NAME = "features";

	// path relative to local machine
	public static String DATA_FILES_PATH = "E:\\dataset";
	public static String CLASSIFIER_FILES_PATH = DATA_FILES_PATH + "\\classifierFiles";
	public static String SYSTEM_FILES_PATH = DATA_FILES_PATH + "\\system";
	
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

	public static List<Image> loadDataSetClassImages(DataSet dataset, Type type, String className){
		File classImagesFolder = getImagesClassFolder(dataset, type, className);
		
		File imageFiles[] = classImagesFolder.listFiles();
		List<File> images = new ArrayList<File>();
		for(File imageFile : imageFiles){
			images.add(imageFile);
		}
		
		return new FilesImageList(images);
	}
	
	public static List<List<Image>> loadDataSetImages(DataSet dataset, Type type){
		List<List<Image>> data = new ArrayList<List<Image>>();

		File imagesMainFolder = getImagesFolder(dataset);
		
		String[] classes = imagesMainFolder.list();
		for(String className : classes){
			data.add(loadDataSetClassImages(dataset, type, className));
		}
		
		return data;
	}

	public static List<List<List<Double>>> loadDataSetFeaturesSeperated(DataSet dataset, Type type){
		return DataFileLoader.loadDataSeprated(getFeaturesFile(dataset, type).toString());
	}
	
	public static List<List<Double>> loadDataSetFeaturesCombined(DataSet dataset, Type type){
		return DataFileLoader.loadDataCombined(getFeaturesFile(dataset, type).toString());
	}

	private static File getFeaturesFile(DataSet dataset, Type type) {
		File datasetFolder = getDataSetFolder(dataset);
		File features = new File(datasetFolder, FEATURES_FOLDER_NAME);
		File featuresFilePath = new File(features, type.toString() + ".txt");
		return featuresFilePath;
	}
	
	private static File getImagesClassFolder(DataSet dataset, Type type,
			String className) {
		File imagesMainFolder = getImagesFolder(dataset);
		File classFolder = new File(imagesMainFolder, className);
		File classImagesFolder = new File(classFolder, type.toString());
		return classImagesFolder;
	}
	
	private static File getImagesFolder(DataSet dataset) {
		File datasetFolder = getDataSetFolder(dataset);
		File imagesMainFolder = new File(datasetFolder, IMAGES_FOLDER_NAME);
		return imagesMainFolder;
	}

	private static File getDataSetFolder(DataSet dataset) {
		File datasetFolder = new File(DATASET_FILE, dataset.toString());
		return datasetFolder;
	}
}
