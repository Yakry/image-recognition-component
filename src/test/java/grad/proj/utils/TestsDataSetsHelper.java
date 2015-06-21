package grad.proj.utils;

import grad.proj.recognition.Loader;
import grad.proj.recognition.train.FeatureVectorGenerator;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestsDataSetsHelper {
	private static File DATASET_FILE = new File("datasets");
	private static String IMAGES_FOLDER_NAME = "images";
	private static String FEATURES_FOLDER_NAME = "features";
	
	// special class that holds test images that has more than one image, used with object localizer tests
	public static String COMBINED_CLASS = "combined";

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
		
		if(!classImagesFolder.exists())
			return new ArrayList<>();
		
		File imageFiles[] = classImagesFolder.listFiles();
		List<File> images = new ArrayList<File>();
		for(File imageFile : imageFiles){
			images.add(imageFile);
		}
		
		return new FilesImageList(images);
	}
	
	public static List<List<Image>> loadDataSetImages(DataSet dataset, Type type, String ...classes){
		List<List<Image>> data = new ArrayList<List<Image>>();

		File imagesMainFolder = getImagesFolder(dataset, type);
		
		if(classes.length == 0){
			classes = imagesMainFolder.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return (name != COMBINED_CLASS) && (new File(dir, name).isDirectory());
				}
			});
		}
		
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
		File imagesMainFolder = getImagesFolder(dataset, type);
		File classImagesFolder = new File(imagesMainFolder, className);
		return classImagesFolder;
	}
	
	private static File getImagesFolder(DataSet dataset, Type type) {
		File datasetFolder = getDataSetFolder(dataset);
		File imagesMainFolder = new File(datasetFolder, IMAGES_FOLDER_NAME);
		File typeFolder = new File(imagesMainFolder, type.toString());
		return typeFolder;
	}

	private static File getDataSetFolder(DataSet dataset) {
		File datasetFolder = new File(DATASET_FILE, dataset.toString());
		return datasetFolder;
	}
	
//	public static void main(String[] args) {
//		Loader.load();
//		generateFeaturesFile(DataSet.calteckUniversity);
//	}
	
	public static void generateFeaturesFile(DataSet dataset){
		FeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		
		List<List<Image>> trainingData = loadDataSetImages(dataset, Type.Train);
		
		featureVectorGenerator.prepareGenerator(trainingData);

		File trainFeaturesFile = getFeaturesFile(dataset, Type.Train);
		generateAndWrite(trainingData, featureVectorGenerator, trainFeaturesFile);

		List<List<Image>> testingData = loadDataSetImages(dataset, Type.Test);
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
	
}
