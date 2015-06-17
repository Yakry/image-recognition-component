package grad.proj.recognition.train.impl;

import grad.proj.recognition.train.Classifier;

import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.SVM;

public class SVMClassifier implements Classifier {
	private static final long serialVersionUID = 1L;
	private SVM svmArray[] = null;
	private LinearNormalizer normalizer = null;
	
//	extracted from opencv c++ code
	private static final int ROW_SAMPLE = 0;
	private static final int RAW_OUTPUT = 1;
	
	public int classify(Mat featureVector) {
		int classLabel = 0;
		double bestDistance = Double.MIN_VALUE;
		featureVector = normalizer.normalize(featureVector);
		
		for(int i=0; i<svmArray.length; ++i){
			Mat predictRes = new Mat();
			svmArray[i].predict(featureVector, predictRes, RAW_OUTPUT);
			
			double distanceFromMargin = predictRes.get(0, 0)[0];
			boolean belongToClass = distanceFromMargin < 0;
			//System.out.println(predict + " " + svmArray[i].predict(featureVector));
//			 System.out.println(distanceFromMargin);
			
			if(belongToClass){
				distanceFromMargin = Math.abs(distanceFromMargin); 
				if(distanceFromMargin > bestDistance){
					classLabel = i;
					bestDistance = distanceFromMargin;
				}
			}
		}
		
		//System.out.println(bestError);
		
		return classLabel;
	}
	
	public double classify(Mat featureVector, int classLabel){
		if(classLabel >= svmArray.length)
			throw new RuntimeException("invalid class label " + classLabel);
		
		featureVector = normalizer.normalize(featureVector);
		return svmArray[classLabel].predict(featureVector);
	}
	
	// all classes feature vectors
	public void train(List<Mat> trainingData) {
		if(trainingData.size() < 2)
			throw new RuntimeException("number of classes below minimum " +
					trainingData.size());
		
		int trainingDataRows = 0;
		int trainingDataCols = trainingData.get(0).cols();
		for(Mat classData : trainingData)
			trainingDataRows += classData.rows();
		
		Mat trainingDataMat = new Mat(trainingDataRows,
				trainingDataCols,CvType.CV_32FC1);
		Mat labels = new Mat(trainingDataRows, 1, CvType.CV_32FC1);
		int curRow = 0;
		
		for(int i=0; i<trainingData.size(); ++i){
			for(int r=0; r<trainingData.get(i).rows(); ++r){
				for(int c=0; c<trainingDataCols; ++c)
					trainingDataMat.put(curRow, c, trainingData.get(i).get(r, c)[0]);
				labels.put(curRow++, 0, i);
			}
		}
		
		normalizer = new LinearNormalizer();
		normalizer.reset(trainingDataMat, -1, 1);
		
		svmArray = new SVM[trainingData.size()];
		for(int i=0; i<trainingData.size(); ++i)
			svmArray[i] = constructSVM(trainingDataMat, labels, i);
	}
	
	private static SVM constructSVM(Mat trainingData, Mat Labels, int classLabel){
		// setting the training parameters
//		CvSVMParams trainingPram = new CvSVMParams();
//		trainingPram.set_svm_type(CvSVM.C_SVC);
//		trainingPram.set_kernel_type(CvSVM.LINEAR);
//		trainingPram.set_degree(0); // not needed with linear kernel
//		trainingPram.set_gamma(1); // not needed with linear kernel
//		trainingPram.set_coef0(0); // not needed with linear kernel
//		trainingPram.set_C(1); // initial default value
//		trainingPram.set_nu(0); // not needed with C_SVC classification
//		trainingPram.set_p(0); // not needed with C_SVC classification
//		trainingPram.set_term_crit(new TermCriteria(TermCriteria.COUNT, 
//											1000, TermCriteria.EPS));
		
//		CvParamGrid cGrid = constructParamGrid(1.0/16.0, 16, 2);
//		CvParamGrid gammaGrid = constructParamGrid(1, 1, 0);
//		CvParamGrid degreeGrid = constructParamGrid(1, 1, 0);
//		CvParamGrid coeffGrid = constructParamGrid(1, 1, 0);
//		CvParamGrid nuGrid = constructParamGrid(1, 1, 0);
//		CvParamGrid pGrid = constructParamGrid(1, 1, 0);
		
		Mat binaryLabels = new Mat(trainingData.rows(), 1, CvType.CV_32SC1);
		for(int i=0; i<trainingData.rows(); ++i)
			binaryLabels.put(i, 0, ((Labels.get(i, 0)[0] == classLabel) ? 1.0 : -1.0));
		
		SVM svm = SVM.create();
		svm.setType(SVM.C_SVC);
		svm.setKernel(SVM.LINEAR);
		svm.setC(1.0);
		
		svm.setTermCriteria(new TermCriteria(TermCriteria.COUNT, 
				2000, TermCriteria.EPS));
		
		
		svm.train(trainingData, ROW_SAMPLE, binaryLabels);
		
//		svm.train_auto(trainingData, binaryLabels, new Mat(), new Mat(),
//				trainingPram, 10, cGrid, gammaGrid, pGrid, nuGrid,
//				coeffGrid, degreeGrid, true);
		
		return svm;
	}
	
//	private static CvParamGrid constructParamGrid(double minVal,
//			double maxVal, double step){
//		CvParamGrid grid = new CvParamGrid();
//		grid.set_min_val(minVal);
//		grid.set_max_val(maxVal);
//		grid.set_step(step);
//		return grid;
//	}
}
