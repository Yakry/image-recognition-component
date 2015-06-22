package grad.proj.recognition.localization;

import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.ImageClassifier;
import grad.proj.recognition.train.impl.LinearNormalizer;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.TestsDataSetsHelper;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;
import grad.proj.utils.TestsDataSetsHelper.DataSet;
import grad.proj.utils.TestsDataSetsHelper.Type;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.SVM;

public class BranchAndBoundObjectLocalizerTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testBranchAndBound() throws Exception {
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		List<List<Image>> trainingData = TestsDataSetsHelper.loadDataSetImages(DataSet.calteckUniversity, Type.Train, "apple", "can");
		List<Image> testingClassData = TestsDataSetsHelper.loadDataSetClassImages(DataSet.calteckUniversity, Type.Test, "apple");
		
		classifier.train(trainingData);
		
		Image sampleTestImage = testingClassData.get(0);
		
		int classLabel = 0;
		
		Mat supportVectors = svmClassifier.svmArray[classLabel].getSupportVectors();
		featureVectorGenerator.generateFeatureVector(sampleTestImage);
		Mat imageKeypoints = featureVectorGenerator.getKeypointsClusterIdxMat();
		
		QualityFunction qualityFunction = new SurfLinearSvmQualityFunction(supportVectors, imageKeypoints);
		
		BranchAndBoundObjectLocalizer localizer = new BranchAndBoundObjectLocalizer(qualityFunction);
		
		System.out.println("starting search");
		System.out.println("####################");
		Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, classLabel);
		
		drawRectangle(objectBounds, sampleTestImage);
		ImageLoader.saveImage(sampleTestImage, "jpg", new File("branchAndBounds.jpg"));
		
		System.out.println("object bounds: ");
		System.out.println("x: " + objectBounds.getX());
		System.out.println("y: " + objectBounds.getY());
		System.out.println("width: " + objectBounds.getWidth());
		System.out.println("height: " + objectBounds.getHeight());
		System.out.println("####################");
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
	
//	@Test
//	public void testBranchAndBound2(){
//		File trainFolder = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH + "\\train\\apple");
////		File testFolder = new File(DataFilesPathWrapper.CLASSIFIER_FILES_PATH + "\\test\\apple");
//		
//		ArrayList<File> trainImagesFiles = new ArrayList<File>();
//		for(File file : trainFolder.listFiles()){
//			trainImagesFiles.add(file);
//		}
//		List<Image> trainImages = new FilesImageList(trainImagesFiles);
//		
//		SVMClassifier svm = new SVMClassifier(new LinearNormalizer());
//
//		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
//		featureVectorGenerator.prepareGenerator(trainImages);
//		
//		List<List<Double>> trainVectors = new ArrayList<>();
//		int cur = 0;
//		for(Image image : trainImages){
//			List<Double> featureVector = featureVectorGenerator.generateFeatureVector(image);
//			trainVectors.add(featureVector);
//			cur++;
//		}
//		List<List<Double>> emptyClass = Arrays.asList(Arrays.asList(new Double[featureVectorGenerator.getFeatureVectorSize()]));
//		svm.train(Arrays.asList(trainVectors, emptyClass));
//		
//		Image testImage = ImageLoader.loadImage(new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH + "\\test\\apple\\test05.jpg"));
//		
//		Rectangle objectBounds = new BranchAndBoundObjectLocalizer().getObjectBounds(testImage, svm.svmArray[0], featureVectorGenerator);
//		
//		Rectangle real = new Rectangle(504, 21, 358, 293);
//
//		System.out.println("errorLeft = " + Math.abs(real.x - objectBounds.x));
//		System.out.println("errorTop = " + Math.abs(real.y - objectBounds.y));
//		System.out.println("errorRight = " + Math.abs((real.x+real.width) - (objectBounds.x + objectBounds.width)));
//		System.out.println("errorBottom = " + Math.abs((real.y+real.height) - (objectBounds.y + objectBounds.height)));
//	}
}
