package grad.proj.recognition.train.impl;

import static org.junit.Assert.assertTrue;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataFileLoader;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class SVMClassifierTest {
	
	static{ System.load(Paths.get(System.getenv("OPENCV3_HOME"), "build", "java", System.getProperty("os.arch").contains("64") ? "x64" : "x86", System.mapLibraryName(Core.NATIVE_LIBRARY_NAME)).toString()); }
	
	// path relative to local machine
	private static final String DATA_FILES_PATH = "E:\\dataset";

	@Test
	public void testSimpleData() {
		List<Mat> trainingData = new ArrayList<Mat>(2);
		// adding class 0 matrix
		trainingData.add(new Mat(3,2,CvType.CV_32FC1));
		// adding class 1 matrix
		trainingData.add(new Mat(2,2,CvType.CV_32FC1));
		
		// (0,0) belong to class 0
		trainingData.get(0).put(0, 0, 0);
		trainingData.get(0).put(0, 1, 0);
		
		// (0.5,0.5) belong to class 0
		trainingData.get(0).put(1, 0, 0.5);
		trainingData.get(0).put(1, 1, 0.5);
		
		// (-0.5,-0.5) belong to class 0
		trainingData.get(0).put(2, 0, -0.5);
		trainingData.get(0).put(2, 1, -0.5);
		
		// (-1,0.5) belong to class 1
		trainingData.get(1).put(3, 0, -1);
		trainingData.get(1).put(3, 1, 0.5);
		
		// (-0.5,1) belong to class 1
		trainingData.get(1).put(4, 0, -0.5);
		trainingData.get(1).put(4, 1, 1);
		
		SVMClassifier classifier = new SVMClassifier();
		classifier.train(trainingData);
		
		Mat inputVector = new Mat(1,2,CvType.CV_32FC1);
		
		inputVector.put(0, 0, 0.25);
		inputVector.put(0, 1, 0.25);
		double class0Value1 = classifier.classify(inputVector);
		
		inputVector.put(0, 0, -0.25);
		inputVector.put(0, 1, -0.25);
		double class0Value2 = classifier.classify(inputVector);
		
		inputVector.put(0, 0, -0.75);
		inputVector.put(0, 1, 0.75);
		double class1Value1 = classifier.classify(inputVector);
		
		System.out.println("MultiClassSVMClassifierTest::testSimpleData:");
		System.out.println("class0Value1 " + class0Value1);
		System.out.println("class0Value2 " + class0Value2);
		System.out.println("class1Value1 " + class1Value1);
		
		assertTrue("class 0 vector 1 not recognized",
				class0Value1 == 0);
		assertTrue("class 0 vector 2 not recognized",
				class0Value2 == 0);
		assertTrue("class 1 vector 1 not recognized",
				class1Value1 == 1);
	}

	@Test
	public void testRealData() {
		// training and testing data already scaled
		List<List<List<Double>>> trainingDataList = DataFileLoader.
				loadDataSeprated("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\satimage_scale_train.txt");
		
		List<List<Double>> testingDataList = DataFileLoader.
				loadDataCombined("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\satimage_scale_test.txt");

		List<Mat> trainingData = new ArrayList<Mat>(trainingDataList.size());
		
		for(List<List<Double>> classData : trainingDataList){
			Mat mat = new Mat(classData.size(),
					classData.get(0).size(), CvType.CV_32FC1);
			for(int r=0;r<classData.size();++r)
				for(int c=0;c<classData.get(0).size();++c)
					mat.put(r, c, classData.get(r).get(c));
			trainingData.add(mat);
		}
		
		SVMClassifier classifier = new SVMClassifier();
		classifier.train(trainingData);
		

		Mat testVector = new Mat(1,testingDataList.get(0).size() - 1,
				CvType.CV_32FC1);
		double correctLabels = 0;
		double numberOfRows = testingDataList.size();
		
		for(List<Double> testingData : testingDataList){
			int classLabel = testingData.get(0).intValue();
			for(int c = 1; c<testingData.size(); ++c)
				testVector.put(0, c - 1, testingData.get(c));
				
			double predictedLabel = classifier.classify(testVector);
			correctLabels += ((classLabel == predictedLabel)?1:0);
		}
		
		System.out.println("MultiClassSVMClassifierTest::testRealData:");
		System.out.println("number of vectors: " + numberOfRows);
		System.out.println("number of correctly classified vectors: " +
				correctLabels);
		System.out.println("percentage: " + (correctLabels*100)/numberOfRows +
				"%");
		assertTrue("correct predicted labels percentage below 75%",
				((correctLabels*100)/numberOfRows) >= 75.0);
	}
	
	@Test
	public void testRealImages(){
		File trainDataSetDirectory = new File(DATA_FILES_PATH + "\\train");
		ArrayList<File> inputImagesFiles = new ArrayList<File>();
		ArrayList<Integer> labels = new ArrayList<Integer>();
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		SVMClassifier classifier = new SVMClassifier();
		Integer currentLabel = 0;
		Integer classesNum = 0;
		Integer featuresNum = 0;
		ArrayList<Integer> classVectorsNum = new ArrayList<Integer>();

		File[] classesDirectories = trainDataSetDirectory.listFiles();
		for(File classDirectory : classesDirectories){
			if(!classDirectory.isDirectory())
				continue; // for safety
			File imageFiles[] = classDirectory.listFiles();
			classVectorsNum.add(imageFiles.length);
			for(File imageFile : imageFiles){
				inputImagesFiles.add(imageFile);
				labels.add(currentLabel);
			}
			++currentLabel;
		}
		
		List<Image> inputImages = new FilesImageList(inputImagesFiles);
		generator.prepareGenerator(inputImages);
		
		classesNum = classesDirectories.length;
		featuresNum = generator.getFeatureVectorSize();
		
		List<Mat> trainingData = new ArrayList<Mat>(classesNum);
		int index = 0;
		for(File classDirectory : classesDirectories){
			if(!classDirectory.isDirectory())
				continue; // for safety
			Mat classTrainingData = new Mat(classVectorsNum.get(index++),
					featuresNum, CvType.CV_32FC1);
			File imageFiles[] = classDirectory.listFiles();
			int row = 0;
			for(File imageFile : imageFiles){
				Mat featureVector = generator.generateFeatureVector(
						ImageLoader.loadImage(imageFile));
				for(int col = 0;col<featuresNum;++col)
					classTrainingData.put(row, col, featureVector.get(0, col)[0]);
				++row;
			}
			trainingData.add(classTrainingData);
		}
		
		classifier.train(trainingData);
		
		File testDataSetDirectory = new File(DATA_FILES_PATH + "\\test");
		double correctLabels = 0;
		double numberOfRows = 0;
		int classLabel = 0;
		
		classesDirectories = testDataSetDirectory.listFiles();
		
		for(File classDirectory : classesDirectories){
			if(!classDirectory.isDirectory())
				continue; // for safety
			File imageFiles[] = classDirectory.listFiles();
			numberOfRows += imageFiles.length;
			for(File imageFile : imageFiles){
				Mat testVector = generator.generateFeatureVector(
						ImageLoader.loadImage(imageFile));
				double predictedLabel = classifier.classify(testVector);
				correctLabels += ((classLabel == predictedLabel)?1:0);
			}
			++classLabel;
		}
		
		System.out.println("MultiClassSVMClassifierTest::testRealData:");
		System.out.println("number of vectors: " + numberOfRows);
		System.out.println("number of correctly classified vectors: " +
				correctLabels);
		System.out.println("percentage: " + (correctLabels*100)/numberOfRows +
				"%");
		assertTrue("correct predicted labels percentage below 75%",
				((correctLabels*100)/numberOfRows) >= 75.0);
	}
}
