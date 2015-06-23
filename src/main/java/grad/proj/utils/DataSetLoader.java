package grad.proj.utils;

import grad.proj.recognition.FeatureVectorGenerator;
import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.imaging.Image;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataSetLoader {
	public enum Type{
		Train,
		Test
	}
	
	private static final String IMAGES_FOLDER_NAME = "images";
	private static final String FEATURES_FOLDER_NAME = "features";
	private static final String FEATURE_VECTOR_GENERATOR_FOLDER = "featureVectorGenerators";
	
	// special class that holds test images that has more than one image, used with object localizer tests
	private static final String COMBINED_CLASS = "combined";
	
	private File datasetFolder;
	
	public DataSetLoader(File datasetFolder) {
		this.datasetFolder = datasetFolder;
	}
	
	public List<Image> loadClassImages(Type type, String className){
		File classImagesFolder = getImagesClassFolder(type, className);
		
		if(!classImagesFolder.exists())
			return new ArrayList<>();
		
		File imageFiles[] = classImagesFolder.listFiles();
		List<File> images = new ArrayList<File>();
		for(File imageFile : imageFiles){
			images.add(imageFile);
		}
		
		return new FilesImageList(images);
	}
	
	public Map<String, List<Image>> loadImages(Type type, String ...classes){
		Map<String, List<Image>> data = new HashMap<>();
	
		File imagesMainFolder = getImagesFolder(type);
		
		if(classes.length == 0){
			classes = imagesMainFolder.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return (name != COMBINED_CLASS) && (new File(dir, name).isDirectory());
				}
			});
		}
		
		for(String className : classes){
			data.put(className, loadClassImages(type, className));
		}
		
		return data;
	}
	public Map<String, List<List<Double>>> loadFeatures(Type type){
		return loadFeatures(type, false);
	}
	
	public Map<String, List<List<Double>>> loadFeatures(Type type, boolean generateIfNotExists){
		if(generateIfNotExists){
			generateFeaturesFile();
		}
		File featuresFolder = new File(datasetFolder, FEATURES_FOLDER_NAME);
		return DataFileLoader.loadDataSeprated(new File(featuresFolder, type.toString() + ".txt").getAbsolutePath());
	}
	
	public SurfFeatureVectorGenerator loadSurfGenerator(){
		return loadSurfGenerator(false);
	}
	
	public SurfFeatureVectorGenerator loadSurfGenerator(boolean generateIfNotExists){
		if(generateIfNotExists){
			generateSurfGeneratorFile();
		}
		return SurfLoader.loadSurf(getFeatureVectorGeneratorsFile("surf"));
	}
	
	public ImageClassifier loadTrainedClassifier() {
		SurfFeatureVectorGenerator featureVectorGenerator = loadSurfGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		svmClassifier.train(loadFeatures(Type.Train));
		
		// doesn't need to be trained
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		return classifier;
	}
	
	public void generateSurfGeneratorFile(){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		
		Map<String, List<Image>> trainingData = loadImages(Type.Train);
		
		featureVectorGenerator.prepareGenerator(trainingData);
		
		SurfLoader.saveSurf(featureVectorGenerator, getFeatureVectorGeneratorsFile("surf"));
	}
	
	public void generateFeaturesFile(){
		FeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		
		Map<String, List<Image>> trainingData = loadImages(Type.Train);;
		
		featureVectorGenerator.prepareGenerator(trainingData);

		File trainFeaturesFile = getFeaturesFile(Type.Train);
		generateAndWriteFeatures(trainingData, featureVectorGenerator, trainFeaturesFile);
		
		Map<String, List<Image>> testingData = loadImages(Type.Test);;
		File testFeaturesFile = getFeaturesFile(Type.Test);
		generateAndWriteFeatures(testingData, featureVectorGenerator, testFeaturesFile);
	}

	private void generateAndWriteFeatures(Map<String, List<Image>> data,
								  FeatureVectorGenerator featureVectorGenerator,
								  File file){
		try {
			FileWriter featuresFile = new FileWriter(file);
			
			int classesNum = data.size();
			int featuresNum = featureVectorGenerator.getFeatureVectorSize();
			
			featuresFile.write(classesNum + " " + featuresNum + "\n");
			
			for (Entry<String, List<Image>> clazz : data.entrySet()) {
				for (Image image : clazz.getValue()) {
					List<Double> featureVector = featureVectorGenerator.generateFeatureVector(image);
	
					featuresFile.write(clazz.getKey() + " ");
					
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

	private File getImagesClassFolder(Type type, String className) {
		File imagesMainFolder = getImagesFolder(type);
		File classImagesFolder = new File(imagesMainFolder, className);
		return classImagesFolder;
	}
	
	private File getImagesFolder(Type type) {
		File imagesMainFolder = new File(datasetFolder, IMAGES_FOLDER_NAME);
		File typeFolder = new File(imagesMainFolder, type.toString());
		return typeFolder;
	}
	
	private File getFeaturesFile(Type type) {
		File features = new File(datasetFolder, FEATURES_FOLDER_NAME);
		File featuresFilePath = new File(features, type.toString() + ".txt");
		return featuresFilePath;
	}
	
	public File getFeatureVectorGeneratorsFile(String name) {
		File generators = new File(datasetFolder, FEATURE_VECTOR_GENERATOR_FOLDER);
		File generator = new File(generators, name + ".txt");
		return generator;
	}
}
