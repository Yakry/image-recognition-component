package grad.proj.utils;

import grad.proj.classification.Classifier;
import grad.proj.classification.FeatureVector;
import grad.proj.classification.FeatureVectorGenerator;
import grad.proj.classification.FeatureVectorImageClassifier;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class DataSetLoader {
	public enum Type{
		Train,
		Test,
		Localization
	}
	
	private static final FilenameFilter IMAGE_FILTER = (FilenameFilter) (dir, name) -> {
		String lowerCase = name.toLowerCase();
		return lowerCase.endsWith(".png") || lowerCase.endsWith(".jpg");
	};
	
	private static final String IMAGES_FOLDER_NAME = "images";
	private static final String FEATURES_FOLDER_NAME = "features";
	private static final String FEATURE_VECTOR_GENERATOR_FOLDER = "featureVectorGenerators";
	
	// special class that holds test images that has more than one image, used with object localizer tests
	public static final String COMBINED_CLASS = "combined";
	
	private File datasetFolder;
	
	public DataSetLoader(File datasetFolder) {
		this.datasetFolder = datasetFolder;
	}
	
	class LocalizationImageList extends AbstractList<SimpleEntry<Image, Map<String, Rectangle>>>{
		private List<File> files;
		
		public LocalizationImageList(List<File> files) {
			this.files = files;
		}

		@Override
		public SimpleEntry<Image, Map<String, Rectangle>> get(int index) {
			String imagePath = files.get(index).getAbsolutePath();
			String pPath = imagePath.substring(0, imagePath.lastIndexOf('.')) + ".txt"; 
			Image image = ImageLoader.loadImage(imagePath);
			Properties p = new Properties();
			InputStream in;
			Map<String, Rectangle> bounds = new HashMap<>();
			try {
				in = new FileInputStream(pPath);
				p.load(in);
				for(String key : p.stringPropertyNames()){
					String[] splitBounds = p.getProperty(key).split(",");
					bounds.put(key, new Rectangle(Integer.valueOf(splitBounds[0]),
												  Integer.valueOf(splitBounds[1]),
												  Integer.valueOf(splitBounds[2]),
												  Integer.valueOf(splitBounds[3])));
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new SimpleEntry<Image, Map<String,Rectangle>>(image, bounds);
		}

		@Override
		public int size() {
			return files.size();
		}
	}

	public List<SimpleEntry<Image, Map<String, Rectangle>>> loadClassImagesLocalization(String className){
		File classImagesFolder = getImagesClassFolder(Type.Localization, className);
		
		if(!classImagesFolder.exists())
			return new ArrayList<>();
		
		File imageFiles[] = classImagesFolder.listFiles(IMAGE_FILTER);
		List<File> images = new ArrayList<File>();
		for(File imageFile : imageFiles){
			images.add(imageFile);
		}
		
		return new LocalizationImageList(images);
	}
	
	public Map<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> loadImages(String ...classes){
		Map<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> data = new HashMap<>();
	
		File imagesMainFolder = getImagesFolder(Type.Localization);
		
		if(classes.length == 0){
			classes = imagesMainFolder.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return (!name.equals(COMBINED_CLASS)) && (new File(dir, name).isDirectory());
				}
			});
		}
		
		for(String className : classes){
			data.put(className, loadClassImagesLocalization(className));
		}
		
		return data;
	}
	
	public List<Image> loadClassImages(Type type, String className){
		File classImagesFolder = getImagesClassFolder(type, className);
		
		if(!classImagesFolder.exists())
			return new ArrayList<>();
		
		
		File imageFiles[] = classImagesFolder.listFiles(IMAGE_FILTER);
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
					return (!name.equals(COMBINED_CLASS)) && (new File(dir, name).isDirectory());
				}
			});
		}
		
		for(String className : classes){
			data.put(className, loadClassImages(type, className));
		}
		
		return data;
	}
	
	public Map<String, List<FeatureVector>> loadFeatures(Type type){
		File featuresFolder = new File(datasetFolder, FEATURES_FOLDER_NAME);
		return (!featuresFolder.exists()) ? null : FeaturesFileLoader.loadFeatures(new File(featuresFolder, type.toString() + ".txt").getAbsolutePath());
	}
	
	public SurfFeatureVectorGenerator loadSurfFeatureVectorGenerator(){
		File generatorFolder = getFeatureVectorGeneratorsFile("surf");
		return (!generatorFolder.exists()) ? null : SurfLoader.loadSurf(generatorFolder);
	}
	
	public Classifier<Image> loadTrainedClassifier() {
		
		FeatureVectorGenerator featureVectorGenerator = loadSurfFeatureVectorGenerator();
		Map<String, List<FeatureVector>> features = loadFeatures(Type.Train);
		
		if(featureVectorGenerator == null || features == null)
			return null;
		
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		svmClassifier.train(features);
		
		return new FeatureVectorImageClassifier(featureVectorGenerator, svmClassifier);
	}
	
	public void generateAndSave(){
		generateAndSave(false);
	}
	
	public void generateAndSave(boolean generatorOnly){
		Map<String, List<Image>> trainingData = loadImages(Type.Train);
		
		SurfFeatureVectorGenerator featureVectorGenerator;
		if(hasSurfGenerator()){
			featureVectorGenerator = loadSurfFeatureVectorGenerator();
		}
		else{
			featureVectorGenerator = new SurfFeatureVectorGenerator();
			featureVectorGenerator.prepareGenerator(trainingData);
			SurfLoader.saveSurf(featureVectorGenerator, getFeatureVectorGeneratorsFile("surf"));
		}

		if(generatorOnly)
			return;
		
		Map<String, List<FeatureVector>> trainingFeatures = generateFeatures(trainingData, featureVectorGenerator);
		FeaturesFileLoader.saveFeatures(trainingFeatures, getFeaturesFile(Type.Train));
		
		Map<String, List<Image>> testingData = loadImages(Type.Test);;
		Map<String, List<FeatureVector>> testFeatures = generateFeatures(testingData, featureVectorGenerator);
		FeaturesFileLoader.saveFeatures(testFeatures, getFeaturesFile(Type.Test));
	}
	
	private Map<String, List<FeatureVector>> generateFeatures(Map<String, List<Image>> data, FeatureVectorGenerator featureVectorGenerator){
		Map<String, List<FeatureVector>> features = new HashMap<>();
		
		for (Entry<String, List<Image>> clazz : data.entrySet()) {
			ArrayList<FeatureVector> clazzFeatures = new ArrayList<FeatureVector>();
			features.put(clazz.getKey(), clazzFeatures);
			
			for (Image image : clazz.getValue()) {
				FeatureVector featureVector = featureVectorGenerator.generateFeatureVector(image);
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
	
	public boolean hasImages(){
		return getImagesFolder(Type.Train).exists() || getImagesFolder(Type.Test).exists();
	}
	
	public boolean hasSurfGenerator(){
		return getFeatureVectorGeneratorsFile("surf").exists();
	}
	
	public boolean hasFeaturesFile(){
		return getFeaturesFile(Type.Train).exists() && getFeaturesFile(Type.Test).exists();
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
	
	private File getFeatureVectorGeneratorsFile(String name) {
		File generators = new File(datasetFolder, FEATURE_VECTOR_GENERATOR_FOLDER);
		File generator = new File(generators, name + ".txt");
		return generator;
	}
}
