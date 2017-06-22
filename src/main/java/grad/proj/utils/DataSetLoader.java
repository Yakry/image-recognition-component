package grad.proj.utils;

import grad.proj.classification.Classifier;
import grad.proj.classification.FeatureVector;
import grad.proj.classification.FeatureVectorGenerator;
import grad.proj.classification.FeatureVectorImageClassifier;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.imaging.ArrayImage;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;

import java.awt.*;
import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class DataSetLoader {
    // special class that holds test images that has more than one image, used with object
    // localizer tests
    public static final String COMBINED_CLASS = "combined";
    private static final String IMAGES_FOLDER_NAME = "images";
    private static final String FEATURES_FOLDER_NAME = "features";
    private static final String FEATURE_VECTOR_GENERATOR_FOLDER = "featureVectorGenerators";
    private static final FilenameFilter IMAGE_FILTER = (dir, name) -> name.toLowerCase().endsWith
            (".png") || name.toLowerCase().endsWith(".jpg");
    private static final FilenameFilter CLASSES_FOLDER_NAME_FILTER = (dir, name) -> (!name.equals
            (COMBINED_CLASS)) && (new File(dir, name).isDirectory());
    private static final CreateListFunction<SimpleEntry<Image, Map<String, Rectangle>>>
            LOCALIZED_IMAGE_LIST_CREATOR = files -> new LocalizationFilesImageList(files);
    private static final CreateListFunction<Image> IMAGE_LIST_CREATOR = files -> new
            FilesImageList(files);
    private File datasetFolder;

    public DataSetLoader(File datasetFolder) {
        this.datasetFolder = datasetFolder;
    }

    public static DataSetLoader getDataSetLoader() {
        DataSetLoader dataSetLoader = new DataSetLoader(new File("datasets/Yasser"));
        if (dataSetLoader.hasImages()) {
            if (!dataSetLoader.hasFeaturesFile() || !dataSetLoader.hasSurfGenerator()) {
                dataSetLoader.generateAndSave();
            }
        }
        return dataSetLoader;
    }

    public List<Image> loadClassImages(Type type, String className) {
        return loadClassImagesInListT(IMAGE_LIST_CREATOR, type, className);
    }

    public Map<String, List<Image>> loadImages(Type type, String... classes) {
        return loadImagesInListT(IMAGE_LIST_CREATOR, type, classes);
    }

    public List<SimpleEntry<Image, Map<String, Rectangle>>> loadClassImagesLocalization(String className) {
        return loadClassImagesInListT(LOCALIZED_IMAGE_LIST_CREATOR, Type.Localization, className);
    }

    public Map<String, List<SimpleEntry<Image, Map<String, Rectangle>>>> loadImagesLocalization
            (String[] classes) {
        return loadImagesInListT(LOCALIZED_IMAGE_LIST_CREATOR, Type.Localization, classes);
    }

    private <T> List<T> loadClassImagesInListT(CreateListFunction<T> function, Type type, String
            className) {
        File classImagesFolder = getImagesClassFolder(type, className);

        if (!classImagesFolder.exists())
            return new ArrayList<>();

        File imageFiles[] = classImagesFolder.listFiles(IMAGE_FILTER);
        List<File> images = new ArrayList<File>();
        for (File imageFile : imageFiles) {
            images.add(imageFile);
        }

        return function.createList(images);
    }

    private <T> Map<String, List<T>> loadImagesInListT(CreateListFunction<T> function, Type type,
                                                       String... classes) {
        Map<String, List<T>> data = new HashMap<>();

        File imagesMainFolder = getImagesFolder(type);

        if (classes.length == 0) {
            classes = imagesMainFolder.list(CLASSES_FOLDER_NAME_FILTER);
        }

        for (String className : classes) {
            data.put(className, loadClassImagesInListT(function, type, className));
        }

        return data;
    }

    public Map<String, List<FeatureVector>> loadFeatures(Type type) {
        File featuresFolder = new File(datasetFolder, FEATURES_FOLDER_NAME);
        return (!featuresFolder.exists()) ? null : FeaturesFileLoader.loadFeatures(new File
                (featuresFolder, type.toString() + ".txt").getAbsolutePath());
    }

    public SurfFeatureVectorGenerator loadSurfFeatureVectorGenerator() {
        File generatorFolder = getFeatureVectorGeneratorsFile("surf");
        return (!generatorFolder.exists()) ? null : SurfLoader.loadSurf(generatorFolder);
    }

    public Classifier<Image> deserializeClassifier() {
        try {
            FileInputStream fileIn = new FileInputStream("classifier.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Classifier<Image> classifier = (Classifier<Image>) in.readObject();
            in.close();
            fileIn.close();
            return classifier;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void serializeClassifier(Classifier<Image> classifier) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("classifier.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(classifier);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Classifier<Image> loadTrainedClassifier() {
//        Classifier<Image> classifier = deserializeClassifier();
//        if (classifier != null) {
//            return classifier;
//        }

        FeatureVectorGenerator featureVectorGenerator = loadSurfFeatureVectorGenerator();
        Map<String, List<FeatureVector>> features = loadFeatures(Type.Train);

        if (featureVectorGenerator == null || features == null)
            return null;

        SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
        svmClassifier.train(features);

        return new FeatureVectorImageClassifier(featureVectorGenerator, svmClassifier);
//        serializeClassifier(classifier);
//        return classifier;
    }

    public void generateAndSave() {
        generateAndSave(false);
    }

    public void generateAndSave(boolean generatorOnly) {
        Map<String, List<Image>> trainingData = loadImages(Type.Train);


        for (Entry<String, List<Image>> entry : trainingData.entrySet()) {
            List<Image> newList = new LinkedList<>();
            int cntr = 0;
            for (Image image : entry.getValue()) {
                int rotations = 4;

                for (int i = 0; i < rotations; ++i) {
                    ArrayImage newImage = new ArrayImage((ArrayImage) image);
                    newImage.rotateImage(i * (2 * Math.PI) / rotations);

                    newList.add(newImage);
                    ImageLoader.saveImage(newImage, "jpg", new File("Output/" + entry.getKey() +
                            "/" + cntr + ".jpg"));
                    ++cntr;
                }
            }

            entry.setValue(newList);
        }


        SurfFeatureVectorGenerator featureVectorGenerator;
        if (hasSurfGenerator()) {
            featureVectorGenerator = loadSurfFeatureVectorGenerator();
        } else {
            featureVectorGenerator = new SurfFeatureVectorGenerator();
            featureVectorGenerator.prepareGenerator(trainingData);
            SurfLoader.saveSurf(featureVectorGenerator, getFeatureVectorGeneratorsFile("surf"));
        }

        if (generatorOnly)
            return;

        Map<String, List<FeatureVector>> trainingFeatures = generateFeatures(trainingData,
                featureVectorGenerator);
        FeaturesFileLoader.saveFeatures(trainingFeatures, getFeaturesFile(Type.Train));

//		Map<String, List<Image>> testingData = loadImages(Type.Test);;
//		Map<String, List<FeatureVector>> testFeatures = generateFeatures(testingData,
// featureVectorGenerator);
//		FeaturesFileLoader.saveFeatures(testFeatures, getFeaturesFile(Type.Test));
    }

    private Map<String, List<FeatureVector>> generateFeatures(Map<String, List<Image>> data,
                                                              FeatureVectorGenerator
                                                                      featureVectorGenerator) {
        Map<String, List<FeatureVector>> features = new HashMap<>();

        for (Entry<String, List<Image>> clazz : data.entrySet()) {
            ArrayList<FeatureVector> clazzFeatures = new ArrayList<FeatureVector>();
            features.put(clazz.getKey(), clazzFeatures);
            int cntr = 0;
            for (Image image : clazz.getValue()) {
                FeatureVector featureVector = featureVectorGenerator.generateFeatureVector(image);
                clazzFeatures.add(featureVector);
                System.out.println("Generated feature vector for " + clazz.getKey() + " image no." +
                        " " + cntr);
                ++cntr;
            }
        }

        return features;
    }

    private File getImagesClassFolder(Type type, String className) {
        File imagesMainFolder = getImagesFolder(type);
        File classImagesFolder = new File(imagesMainFolder, className);
        System.out.println("Fetching images from: " + classImagesFolder);
        return classImagesFolder;
    }

    public boolean hasImages() {
        return getImagesFolder(Type.Train).exists() || getImagesFolder(Type.Test).exists();
    }

    public boolean hasSurfGenerator() {
        return getFeatureVectorGeneratorsFile("surf").exists();
    }

    public boolean hasFeaturesFile() {
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
        return new File(generators, name + ".txt");
    }

    public enum Type {
        Train,
        Test,
        Localization
    }

    private interface CreateListFunction<T> {
        List<T> createList(List<File> files);
    }
}
