package grad.proj.recognition.train;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MultiClassSVMClassifierTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

	@Test
	public void test() {
		Mat trainingData = new Mat(7,2,CvType.CV_32FC1);
		Mat classLabels = new Mat(7,1,CvType.CV_32FC1);
		
		// (0,0) belong to class 1.0
		classLabels.put(0, 0, 1.0);
		trainingData.put(0, 0, 0);
		trainingData.put(0, 1, 0);
		
		// (0.5,0.5) belong to class 1.0
		classLabels.put(1, 0, 1.0);
		trainingData.put(1, 0, 0.5);
		trainingData.put(1, 1, 0.5);
		
		// (-0.5,-0.5) belong to class 1.0
		classLabels.put(2, 0, 1.0);
		trainingData.put(2, 0, -0.5);
		trainingData.put(2, 1, -0.5);
		
		// (-1,0.5) belong to class 2.0
		classLabels.put(3, 0, 2.0);
		trainingData.put(3, 0, -1);
		trainingData.put(3, 1, 0.5);
		
		// (-0.5,1) belong to class 2.0
		classLabels.put(4, 0, 2.0);
		trainingData.put(4, 0, -0.5);
		trainingData.put(4, 1, 1);
		
		// (1,-0.5) belong to class 3.0
		classLabels.put(5, 0, 3.0);
		trainingData.put(5, 0, 1);
		trainingData.put(5, 1, -0.5);
		
		// (1,-0.5) belong to class 3.0
		classLabels.put(6, 0, 3.0);
		trainingData.put(6, 0, 0.5);
		trainingData.put(6, 1, -1);
		
		MultiClassSVMClassifier classifier = new MultiClassSVMClassifier();
		classifier.train(trainingData, classLabels);
		
		Mat inputVector = new Mat(1,2,CvType.CV_32FC1);
		
		inputVector.put(0, 0, 0.25);
		inputVector.put(0, 1, 0.25);
		double class1Value1 = classifier.classify(inputVector);
		
		inputVector.put(0, 0, -0.25);
		inputVector.put(0, 1, -0.25);
		double class1Value2 = classifier.classify(inputVector);
		
		inputVector.put(0, 0, -0.75);
		inputVector.put(0, 1, 0.75);
		double class2Value1 = classifier.classify(inputVector);
		
		inputVector.put(0, 0, 0.75);
		inputVector.put(0, 1, -0.75);
		double class3Value1 = classifier.classify(inputVector);
		
		assertTrue("class 1 vector 1 not recognized",
				class1Value1 == 1.0);
		assertTrue("class 1 vector 2 not recognized",
				class1Value2 == 1.0);
		assertTrue("class 2 vector 1 not recognized",
				class2Value1 == 2.0);
		assertTrue("class 3 vector 1 not recognized",
				class3Value1 == 3.0);
	}
}
