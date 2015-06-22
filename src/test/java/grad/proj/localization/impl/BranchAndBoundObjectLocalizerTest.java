package grad.proj.localization.impl;

import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;
import grad.proj.localization.impl.QualityFunction;
import grad.proj.localization.impl.SurfLinearSvmQualityFunction;
import grad.proj.recognition.FeatureVectorClassifier;
import grad.proj.recognition.FeatureVectorGenerator;
import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.DataSetsTestsHelper;
import grad.proj.utils.DataSetsTestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Mat;

public class BranchAndBoundObjectLocalizerTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testBranchAndBound() throws Exception {
		DataSetLoader dataSetLoader = DataSetsTestsHelper.getDataSetLoader(DataSet.calteckUniversity);
		
		ImageClassifier classifier = dataSetLoader.loadTrainedClassifier();
		
		SurfFeatureVectorGenerator featureVectorGenerator = (SurfFeatureVectorGenerator)classifier.getFeatureVectorGenerator();
		SVMClassifier svmClassifier = (SVMClassifier) classifier.getClassifier();
		
		List<Image> testingClassData = dataSetLoader.loadClassImages(Type.Test, "apple");
		
		int classLabel = 0;
		Mat supportVectors = svmClassifier.svmArray[classLabel].getSupportVectors();
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingClassData){
			System.out.println("starting search " + searchIndex);
			featureVectorGenerator.generateFeatureVector(sampleTestImage);
			Mat imageKeypoints = featureVectorGenerator.getKeypointsClusterIdxMat();
			QualityFunction qualityFunction = new SurfLinearSvmQualityFunction(supportVectors, imageKeypoints);
			BranchAndBoundObjectLocalizer localizer = new BranchAndBoundObjectLocalizer(qualityFunction);
			
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, classLabel);
			drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File("branchAndBound" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}

	public static void drawRectangle(Rectangle objectBounds, Image drawableImage) {
		for(int i=0;i<objectBounds.getWidth();++i){
			drawableImage.setPixelAt(objectBounds.y, objectBounds.x + i, Color.GREEN.getRGB());
			drawableImage.setPixelAt(objectBounds.y + objectBounds.height, objectBounds.x + i, Color.GREEN.getRGB());
		}
		
		for(int i=0;i<objectBounds.getHeight();++i){
			drawableImage.setPixelAt(objectBounds.y + i, objectBounds.x, Color.GREEN.getRGB());
			drawableImage.setPixelAt(objectBounds.y + i, objectBounds.x + objectBounds.width, Color.GREEN.getRGB());
		}
	}
}
