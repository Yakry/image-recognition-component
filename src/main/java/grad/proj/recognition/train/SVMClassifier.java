package grad.proj.recognition.train;

import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

public class SVMClassifier implements Classifier {
	private static final long serialVersionUID = 1L;
	private CvSVM svm = null;
	
	@Override
	public double classify(List<Double> featureVector) {
		// constructing a featureArray from the featureVector
		int vectorSize = featureVector.size();
		Mat inputData = new Mat(1, vectorSize, CvType.CV_32FC1);
		for(int i=0;i<vectorSize;++i)
			inputData.put(0, i, featureVector.get(i));
		
		//prediction
		return svm.predict(inputData);
	}

	@Override
	public void train(List<List<Double>> featureVectors) {
		// setting the training parameters
		CvSVMParams trainingPram = new CvSVMParams();
		trainingPram.set_svm_type(CvSVM.ONE_CLASS);
		trainingPram.set_kernel_type(CvSVM.RBF);
		trainingPram.set_degree(0); // not needed with RBF kernel
		trainingPram.set_gamma(1); // initial default value
		trainingPram.set_coef0(0); // not needed with RBF kernel
		trainingPram.set_C(1); // not needed with ONE_CLASS classification
		trainingPram.set_nu(0.5); // initial default value
		trainingPram.set_p(0); // not needed with ONE_CLASS classification
		trainingPram.set_term_crit(new TermCriteria(TermCriteria.COUNT, 
											1000, TermCriteria.EPS));
		
		// constructing a featureMatrix from the featureVectors
		int vectorNum = featureVectors.size();
		int vectorSize = featureVectors.get(0).size();
		Mat label = new Mat(1,vectorNum, CvType.CV_32FC1);
		Mat trainingData = new Mat(vectorNum, vectorSize, CvType.CV_32FC1);
		for(int i=0;i<vectorNum;++i){
			label.put(0, i, 1.0);
			for(int j=0;j<vectorSize;++j)
				trainingData.put(i, j, featureVectors.get(i).get(j));
		}
		
		svm = new CvSVM();
		svm.train(trainingData, label, new Mat(), new Mat(), trainingPram);
	}
}
