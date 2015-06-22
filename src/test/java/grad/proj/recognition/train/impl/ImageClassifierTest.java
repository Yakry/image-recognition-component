package grad.proj.recognition.train.impl;

import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.ImageClassifier;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.Image;
import grad.proj.utils.DataSetsTestsHelper;
import grad.proj.utils.DataSetsTestsHelper.DataSet;
import grad.proj.utils.DataSetsTestsHelper.Type;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageClassifierTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testClassifiySingleAppleAndCanTrainImages(){
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		DataSetLoader dataSetLoader = DataSetsTestsHelper.getDataSetLoader(DataSet.calteckUniversity);
		List<List<Image>> trainingData = dataSetLoader.loadImages( Type.Train, "apple", "can");
		classifier.train(trainingData);
		
		double classifiedCorrectly = 0;
		int totalImages = 0;

		// I couldn't use the testing images because they are for branch and bound, so they contain other objects
		for(int clazz=0; clazz < trainingData.size(); clazz++){
			for(Image image : trainingData.get(clazz)){
				classifiedCorrectly += (classifier.classify(image) == clazz) ? 1 : 0;
			}			
			totalImages += trainingData.get(clazz).size();
		}
		classifiedCorrectly /= totalImages;
		
		System.out.println("classifiedCorrectly: " + classifiedCorrectly);
		
		assertTrue("classifiedCorrectly < 90", classifiedCorrectly >= 0.9);
	}
	
//	@Test
//	public void testRealImages() throws Exception{
//		File trainDataSetDirectory = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH
//												+ "\\train");
//		ArrayList<File> inputImagesFiles = new ArrayList<File>();
//		ArrayList<Integer> labels = new ArrayList<Integer>();
//		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
//		SVMClassifier classifier = new SVMClassifier();
//		Integer currentLabel = 0;
//		Integer classesNum = 0;
//		Integer featuresNum = 0;
//		ArrayList<Integer> classVectorsNum = new ArrayList<Integer>();
//
//		File[] classesDirectories = trainDataSetDirectory.listFiles();
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue;
//			File imageFiles[] = classDirectory.listFiles();
//			classVectorsNum.add(imageFiles.length);
//			for(File imageFile : imageFiles){
//				inputImagesFiles.add(imageFile);
//				labels.add(currentLabel);
//			}
//			++currentLabel;
//		}
//		
//		List<Image> inputImages = new FilesImageList(inputImagesFiles);
//		generator.prepareGenerator(inputImages);
//		
//		classesNum = classesDirectories.length;
//		featuresNum = generator.getFeatureVectorSize();
//		
//		List<Mat> trainingData = new ArrayList<Mat>(classesNum);
//		int index = 0;
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue; // for safety
//			Mat classTrainingData = new Mat(classVectorsNum.get(index++),
//					featuresNum, CvType.CV_32FC1);
//			File imageFiles[] = classDirectory.listFiles();
//			int row = 0;
//			for(File imageFile : imageFiles){
//				List<Double> featureVector = generator.generateFeatureVector(ImageLoader.loadImage(imageFile));
//				for(int col = 0;col<featuresNum;++col)
//					classTrainingData.put(row, col, featureVector.get(0, col)[0]);
//				++row;
//			}
//			trainingData.add(classTrainingData);
//		}
//		
//		classifier.train(trainingData);
//		
//		File testDataSetDirectory = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH
//												+ "\\test");
//		double correctLabels = 0;
//		double numberOfRows = 0;
//		int classLabel = 0;
//		
//		classesDirectories = testDataSetDirectory.listFiles();
//		
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue; // for safety
//			File imageFiles[] = classDirectory.listFiles();
//			numberOfRows += imageFiles.length;
//			for(File imageFile : imageFiles){
//				List<Double> testVector = generator.generateFeatureVector(ImageLoader.loadImage(imageFile));
//				double predictedLabel = classifier.classify(testVector);
//				correctLabels += ((classLabel == predictedLabel)?1:0);
//			}
//			++classLabel;
//		}
//		
//		System.out.println("MultiClassSVMClassifierTest::testRealData:");
//		System.out.println("number of vectors: " + numberOfRows);
//		System.out.println("number of correctly classified vectors: " +
//				correctLabels);
//		System.out.println("percentage: " + (correctLabels*100)/numberOfRows +
//				"%");
//		assertTrue("correct predicted labels percentage below 75%",
//				((correctLabels*100)/numberOfRows) >= 75.0);
//	}
}
