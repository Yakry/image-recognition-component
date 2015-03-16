package grad.proj.recognition.train;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.CvParamGrid;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

@SuppressWarnings("unused")
public class MultiClassSVMClassifier {
	private static final long serialVersionUID = 1L;
	private CvSVM svm = null;
	private Normalizer normalizer = null;
	
	public double classify(Mat featureVector) {
		return svm.predict(featureVector);
	}
	
	// all class feature vectors
	public void train(Mat featureVectors, Mat classLabels) {
		// setting the training parameters
		CvSVMParams trainingPram = new CvSVMParams();
		trainingPram.set_svm_type(CvSVM.C_SVC);
		trainingPram.set_kernel_type(CvSVM.LINEAR);
		trainingPram.set_degree(0); // not needed with linear kernel
		trainingPram.set_gamma(1); // not needed with linear kernel
		trainingPram.set_coef0(0); // not needed with linear kernel
		trainingPram.set_C(1); // initial default value
		trainingPram.set_nu(0.5); // not needed with C_SVC classification
		trainingPram.set_p(0); // not needed with C_SVC classification
		trainingPram.set_term_crit(new TermCriteria(TermCriteria.COUNT, 
											1000, TermCriteria.EPS));
		
		/*
		// initializing the normalizer
		normalizer = new LinearNormalizer();
		// normalizing the featureVectors
		List<List<Double>> normalizedFeatureVectors =
				normalizer.reset(featureVectors,0.0,1.0);
		*/
		
		/*
		// constructing a featureMatrix from the featureVectors
		int vectorNum = normalizedFeatureVectors.size();
		int vectorSize = normalizedFeatureVectors.get(0).size();
		Mat label = new Mat(1,vectorNum, CvType.CV_32FC1);
		Mat trainingData = new Mat(vectorNum, vectorSize, CvType.CV_32FC1);
		for(int i=0;i<vectorNum;++i){
			label.put(0, i, 1.0);
			for(int j=0;j<vectorSize;++j)
				trainingData.put(i, j, normalizedFeatureVectors.get(i).get(j));
		}
		*/
		
		CvParamGrid cGrid = new CvParamGrid();
		cGrid.set_min_val(1/16);
		cGrid.set_max_val(16);
		cGrid.set_step(2);
		
		CvParamGrid gammaGrid = new CvParamGrid();
		cGrid.set_min_val(1/16);
		cGrid.set_max_val(16);
		cGrid.set_step(2);
		
		CvParamGrid degreeGrid = new CvParamGrid();
		cGrid.set_min_val(0);
		cGrid.set_max_val(1);
		cGrid.set_step(1);
		
		CvParamGrid coeffGrid = new CvParamGrid();
		cGrid.set_min_val(0);
		cGrid.set_max_val(1);
		cGrid.set_step(1);
		
		CvParamGrid nuGrid = new CvParamGrid();
		cGrid.set_min_val(0);
		cGrid.set_max_val(1);
		cGrid.set_step(1);
		
		CvParamGrid pGrid = new CvParamGrid();
		cGrid.set_min_val(0);
		cGrid.set_max_val(1);
		cGrid.set_step(1);
		
		svm = new CvSVM();
		//svm.train(featureVectors, classLabels, new Mat(), new Mat(), trainingPram);
		svm.train_auto(featureVectors, classLabels, new Mat(), new Mat(),
				trainingPram, 5, cGrid, gammaGrid, pGrid, nuGrid,
				coeffGrid, degreeGrid, true);
	}
}
