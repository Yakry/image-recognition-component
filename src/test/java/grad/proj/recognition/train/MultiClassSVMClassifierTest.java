package grad.proj.recognition.train;

import static org.junit.Assert.*;
import grad.proj.utils.DataFileLoader;

import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MultiClassSVMClassifierTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	@Test
	public void testSimpleData() {
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
		//classifier.train(trainingData, classLabels);
		
		Mat inputVector = new Mat(1,2,CvType.CV_32FC1);
		
		inputVector.put(0, 0, 0.25);
		inputVector.put(0, 1, 0.25);
		double class1Value1 = classifier.classify(inputVector, true);
		
		inputVector.put(0, 0, -0.25);
		inputVector.put(0, 1, -0.25);
		double class1Value2 = classifier.classify(inputVector, true);
		
		inputVector.put(0, 0, -0.75);
		inputVector.put(0, 1, 0.75);
		double class2Value1 = classifier.classify(inputVector, true);
		
		inputVector.put(0, 0, 0.75);
		inputVector.put(0, 1, -0.75);
		double class3Value1 = classifier.classify(inputVector, true);
		
		assertTrue("class 1 vector 1 not recognized",
				class1Value1 == 1.0);
		assertTrue("class 1 vector 2 not recognized",
				class1Value2 == 1.0);
		assertTrue("class 2 vector 1 not recognized",
				class2Value1 == 2.0);
		assertTrue("class 3 vector 1 not recognized",
				class3Value1 == 3.0);
	}

	@Test
	public void testRealData() {
		// training and testing data already scaled
		List<List<Double> > trainingData = DataFileLoader.
				loadMixedFeatureVectors("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\satimage_scale_train.txt");
		
		List<List<Double> >  testingData = DataFileLoader.
				loadMixedFeatureVectors("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\satimage_scale_test.txt");

		Mat trainingVectors = new Mat(trainingData.size(),
				trainingData.get(0).size()-1,CvType.CV_32FC1);
		Mat trainingClassLables = new Mat(
				trainingData.size(),1,CvType.CV_32FC1);
		
		for(int i=0;i<trainingData.size();++i){
			trainingClassLables.put(i, 0, trainingData.get(i).get(0));
			for(int j=1;j<trainingData.get(0).size();++j)
				trainingVectors.put(i, j-1, trainingData.get(i).get(j));
		}
		
		MultiClassSVMClassifier classifier = new MultiClassSVMClassifier();
		//classifier.train(trainingVectors, trainingClassLables);
		
		Mat testVector = new Mat(1,testingData.get(0).size()-1,CvType.CV_32FC1);
		double correctLabels = 0;
		
		for(int i=0;i<testingData.size();++i){
			for(int j=1;j<testingData.get(i).size();++j)
				testVector.put(0, j-1, trainingData.get(i).get(j));
			
			double classLabel = testingData.get(i).get(0);
			double predictedLabel = classifier.classify(testVector, true);
			correctLabels += ((classLabel == predictedLabel)?1:0);
		}
		
		System.out.println(correctLabels);
		System.out.println((correctLabels*100)/testingData.size() + "%");
		assertTrue("correct predicted labels percentage below 85%",
				((correctLabels*100)/testingData.size()) >= 85.0);
	}
}
