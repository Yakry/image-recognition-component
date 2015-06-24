package grad.proj.utils;

import grad.proj.recognition.FeatureVectorGenerator;
import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.imaging.Image;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataSetLoader {
	public enum Type{
		Train,
		Test,
		Localization
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
		File featuresFolder = new File(datasetFolder, FEATURES_FOLDER_NAME);
		return (!featuresFolder.exists()) ? null : FeaturesFileLoader.loadFeatures(new File(featuresFolder, type.toString() + ".txt").getAbsolutePath());
	}
	
	public SurfFeatureVectorGenerator loadSurfFeatureVectorGenerator(){
		File generatorFolder = getFeatureVectorGeneratorsFile("surf");
		return (!generatorFolder.exists()) ? null : SurfLoader.loadSurf(generatorFolder);
	}
	
	public ImageClassifier loadTrainedClassifier() {
		
		FeatureVectorGenerator featureVectorGenerator = loadSurfFeatureVectorGenerator();
		Map<String, List<List<Double>>> features = loadFeatures(Type.Train);
		
		if(featureVectorGenerator == null || features == null)
			return null;
		
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		svmClassifier.train(features);
		
		return new ImageClassifier(featureVectorGenerator, svmClassifier);
	}
	
	public void generateAndSave(){
		generateAndSave(false);
	}
	
	public void generateAndSave(boolean generatorOnly){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
	
		Map<String, List<Image>> trainingData = loadImages(Type.Train);
		featureVectorGenerator.prepareGenerator(trainingData);

		SurfLoader.saveSurf(featureVectorGenerator, getFeatureVectorGeneratorsFile("surf"));
		
		if(generatorOnly)
			return;
		
		Map<String, List<List<Double>>> trainingFeatures = generateFeatures(trainingData, featureVectorGenerator);
		FeaturesFileLoader.saveFeatures(trainingFeatures, getFeaturesFile(Type.Train));
		
		Map<String, List<Image>> testingData = loadImages(Type.Test);;
		Map<String, List<List<Double>>> testFeatures = generateFeatures(testingData, featureVectorGenerator);
		FeaturesFileLoader.saveFeatures(testFeatures, getFeaturesFile(Type.Test));
	}
	
	private Map<String, List<List<Double>>> generateFeatures(Map<String, List<Image>> data, FeatureVectorGenerator featureVectorGenerator){
		Map<String, List<List<Double>>> features = new HashMap<>();
		
		for (Entry<String, List<Image>> clazz : data.entrySet()) {
			ArrayList<List<Double>> clazzFeatures = new ArrayList<List<Double>>();
			features.put(clazz.getKey(), clazzFeatures);
			
			for (Image image : clazz.getValue()) {
				List<Double> featureVector = featureVectorGenerator.generateFeatureVector(image);
				clazzFeatures.add(featureVector);
			}
		}
		
		return features;
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
