package grad.proj.recognition.train;

import static org.junit.Assert.*;
import grad.proj.utils.DataFileLoader;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MultiClassSVMClassifierTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
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
		
		MultiClassSVMClassifier classifier = new MultiClassSVMClassifier();
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
				loadIsolatedFeatureVectors("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\satimage_scale_train.txt");

		List<Mat> trainingData = new ArrayList<Mat>(trainingDataList.size());
		
		for(List<List<Double>> classData : trainingDataList){
			Mat mat = new Mat(classData.size(),
					classData.get(0).size(), CvType.CV_32FC1);
			for(int r=0;r<classData.size();++r)
				for(int c=0;c<classData.get(0).size();++c)
					mat.put(r, c, classData.get(r).get(c));
			trainingData.add(mat);
		}
		
		MultiClassSVMClassifier classifier = new MultiClassSVMClassifier();
		classifier.train(trainingData);
		

		Mat testVector = new Mat(1,trainingDataList.get(0).get(0).size(),
				CvType.CV_32FC1);
		double correctLabels = 0;
		double numberOfRows = 0;
		
		for(int i=0;i<trainingDataList.size();++i){
			List<List<Double>> classData = trainingDataList.get(i);
			for(int r=0;r<classData.size();++r){
				for(int c=0;c<classData.get(r).size();++c)
					testVector.put(0, c, classData.get(r).get(c));
				
				double predictedLabel = classifier.classify(testVector);
				correctLabels += ((i == predictedLabel)?1:0);
				numberOfRows += 1;
			}
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
