package grad.proj.classification;

import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertTrue;

public class FeatureVectorImageClassifierTest extends RequiresLoadingTestBaseClass {

    @Test
    public void testClassifiyOnCaltechDataSet() {
        testClassifyingAccuracyDataset(DataSet.calteckUniversity, Type.Test, "scissors", "watch");

//		testClassifyingCompleteDataset(DataSet.calteckUniversity);
    }

    @Ignore    // Takes a lot of time
    @Test
    public void testClassifiyOnCaltechDataSetBikes() {
        testClassifyingAccuracyDataset(DataSet.calteckUniversity, Type.Test, "bikes");
    }

    @Ignore
    @Test
    public void testClassifiyOnMohsen1DataSet() {
        testClassifyingAccuracyDataset(DataSet.mohsen1, Type.Test);
    }

    @Ignore
    @Test
    public void testClassifiyOnMohsen2DataSet() {
        testClassifyingAccuracyDataset(DataSet.mohsen2, Type.Test);
    }

    public FeatureVectorGenerator createGenerator() {
        return new SurfFeatureVectorGenerator();
    }


    public Classifier<Image> getTrainedClassifier(DataSet dataSet, String... classes) {
        DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
        Map<String, List<Image>> trainingDataImages = dataSetLoader.loadImages(Type.Train, classes);
        Map<String, List<Image>> testingData = dataSetLoader.loadImages(Type.Test, classes);


        FeatureVectorGenerator generator = createGenerator();
        generator.prepareGenerator(trainingDataImages);


        Map<String, List<FeatureVector>> trainingDataFeatures = new HashMap<>();


        for (Entry<String, List<Image>> entry : trainingDataImages.entrySet()) {
            trainingDataFeatures.put(entry.getKey(), new LinkedList<>());
            for (Image image : entry.getValue()) {
                trainingDataFeatures.get(entry.getKey()).add(generator.generateFeatureVector
                        (image));
            }
        }


        Classifier<FeatureVector> featureVectorClassifier = new SVMClassifier(new
                LinearNormalizer());
        featureVectorClassifier.train(trainingDataFeatures);

        Classifier<Image> imageClassifier = new FeatureVectorImageClassifier(generator,
                featureVectorClassifier);

        return imageClassifier;
    }

    public void testClassifyingAccuracyDataset(DataSet dataSet, Type testImagesType, String...
            classes) {
        DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);

        Classifier<Image> imageClassifier = getTrainedClassifier(dataSet, classes);

        imageClassifier = dataSetLoader.loadTrainedClassifier();

        Map<String, List<Image>> testingData = dataSetLoader.loadImages(testImagesType, classes);

        double classifiedCorrectly = 0;
        int totalImages = 0;

        for (Entry<String, List<Image>> classEntry : testingData.entrySet()) {
            String classLabel = classEntry.getKey();

            for (Image image : classEntry.getValue()) {
                String predictedLabel = imageClassifier.classify(image);

                classifiedCorrectly += (classLabel.equals(predictedLabel)) ? 1 : 0;
            }
            totalImages += classEntry.getValue().size();
        }

        double allClassifiedCorrectly = classifiedCorrectly * 100.0 / totalImages;

        System.out.println("total images: " + totalImages);
        System.out.println("classifiedCorrectly: " + classifiedCorrectly);
        System.out.println("accuracy: " + classifiedCorrectly / totalImages);

        assertTrue("classifiedCorrectly < 75", allClassifiedCorrectly >= 0.75);
    }
}
