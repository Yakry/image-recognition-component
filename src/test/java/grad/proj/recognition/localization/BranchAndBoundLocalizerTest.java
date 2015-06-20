package grad.proj.recognition.localization;

import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.impl.LinearNormalizer;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.TestsDataSetsHelper;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;

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

public class BranchAndBoundLocalizerTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testBranchAndBound() throws Exception {
		File trainDataSetDirectory = new File(
				TestsDataSetsHelper.CLASSIFIER_FILES_PATH + "\\train");
		ArrayList<File> inputImagesFiles = new ArrayList<File>();
		ArrayList<Integer> labels = new ArrayList<Integer>();
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		SVMClassifier classifier = new SVMClassifier(new LinearNormalizer());
		Integer currentLabel = 0;
		Integer classesNum = 0;
		Integer featuresNum = 0;
		ArrayList<Integer> classVectorsNum = new ArrayList<Integer>();

		File[] classesDirectories = trainDataSetDirectory.listFiles();
		for (File classDirectory : classesDirectories) {
			if (!classDirectory.isDirectory())
				continue;
			File imageFiles[] = classDirectory.listFiles();
			classVectorsNum.add(imageFiles.length);
			for (File imageFile : imageFiles) {
				inputImagesFiles.add(imageFile);
				labels.add(currentLabel);
			}
			++currentLabel;
		}

		List<Image> inputImages = new FilesImageList(inputImagesFiles);
		generator.prepareGenerator(inputImages);

		classesNum = classesDirectories.length;
		featuresNum = generator.getFeatureVectorSize();

		List<List<List<Double>>> trainingData = new ArrayList<>(classesNum);
		int index = 0;
		for (File classDirectory : classesDirectories) {
			if (!classDirectory.isDirectory())
				continue; // for safety
			List<List<Double>> classTrainingData = new ArrayList<>();
			File imageFiles[] = classDirectory.listFiles();
			int row = 0;
			
			for (File imageFile : imageFiles) {
				List<Double> featureVector = generator.generateFeatureVector(ImageLoader
						.loadImage(imageFile));
				classTrainingData.add(featureVector);
				++row;
			}
			trainingData.add(classTrainingData);
		}

		classifier.train(trainingData);
		Image image = ImageLoader.loadImage(TestsDataSetsHelper.DATA_FILES_PATH + "\\001.jpg");
		List<Double> featureVector = generator.generateFeatureVector(image);
		int classLabel = classifier.classify(featureVector);
		SVM classSVM = classifier.svmArray[classLabel];
		BranchAndBoundLocalizer localizer = new BranchAndBoundLocalizer();
		
		System.out.println("starting search");
		System.out.println("####################");
		Rectangle objectBounds = localizer.getObjectBounds(image, classSVM, generator);
		
		BufferedImage drawableImage = ImageIO.read(
				new File(TestsDataSetsHelper.DATA_FILES_PATH + "\\001.jpg"));
		for(int i=0;i<objectBounds.getWidth();++i){
			drawableImage.setRGB((int)objectBounds.getX() + i,
					(int)objectBounds.getY(),
					Color.GREEN.getRGB());
			drawableImage.setRGB((int)objectBounds.getX() + i,
					(int)objectBounds.getY() + (int)objectBounds.getHeight(),
					Color.GREEN.getRGB());
		}
		
		for(int i=0;i<objectBounds.getHeight();++i){
			drawableImage.setRGB((int)objectBounds.getX(),
					(int)objectBounds.getY() + i,
					Color.GREEN.getRGB());
			drawableImage.setRGB((int)objectBounds.getX() + (int)objectBounds.getWidth(),
					(int)objectBounds.getY() + i,
					Color.GREEN.getRGB());
		}
		
		ImageIO.write(drawableImage, "jpg",
				new File(TestsDataSetsHelper.DATA_FILES_PATH + "\\out.jpg"));
		
		System.out.println("class lable: " + classLabel);
		System.out.println("####################");
		System.out.println("object bounds: ");
		System.out.println("x: " + objectBounds.getX());
		System.out.println("y: " + objectBounds.getY());
		System.out.println("width: " + objectBounds.getWidth());
		System.out.println("height: " + objectBounds.getHeight());
		System.out.println("####################");
	}
	
	@Test
	public void testBranchAndBound2(){
		File trainFolder = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH + "\\train\\apple");
//		File testFolder = new File(DataFilesPathWrapper.CLASSIFIER_FILES_PATH + "\\test\\apple");
		
		ArrayList<File> trainImagesFiles = new ArrayList<File>();
		for(File file : trainFolder.listFiles()){
			trainImagesFiles.add(file);
		}
		List<Image> trainImages = new FilesImageList(trainImagesFiles);
		
		SVMClassifier svm = new SVMClassifier(new LinearNormalizer());

		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		featureVectorGenerator.prepareGenerator(trainImages);
		
		List<List<Double>> trainVectors = new ArrayList<>();
		int cur = 0;
		for(Image image : trainImages){
			List<Double> featureVector = featureVectorGenerator.generateFeatureVector(image);
			trainVectors.add(featureVector);
			cur++;
		}
		List<List<Double>> emptyClass = Arrays.asList(Arrays.asList(new Double[featureVectorGenerator.getFeatureVectorSize()]));
		svm.train(Arrays.asList(trainVectors, emptyClass));
		
		Image testImage = ImageLoader.loadImage(new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH + "\\test\\apple\\test05.jpg"));
		
		Rectangle objectBounds = new BranchAndBoundLocalizer().getObjectBounds(testImage, svm.svmArray[0], featureVectorGenerator);
		
		Rectangle real = new Rectangle(504, 21, 358, 293);

		System.out.println("errorLeft = " + Math.abs(real.x - objectBounds.x));
		System.out.println("errorTop = " + Math.abs(real.y - objectBounds.y));
		System.out.println("errorRight = " + Math.abs((real.x+real.width) - (objectBounds.x + objectBounds.width)));
		System.out.println("errorBottom = " + Math.abs((real.y+real.height) - (objectBounds.y + objectBounds.height)));
	}
}
