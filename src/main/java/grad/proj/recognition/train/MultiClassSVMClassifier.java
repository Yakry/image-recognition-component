package grad.proj.recognition.train;

import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.CvParamGrid;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

@SuppressWarnings("unused")
public class MultiClassSVMClassifier {
	private static final long serialVersionUID = 1L;
	private CvSVM svm = null;
	
	public double classify(Mat featureVector, boolean flag) {
		return svm.predict(featureVector, flag);
	}
	
	// all class feature vectors
	public void train(List<Mat> featureVectors, int classIndex) {
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
		cGrid.set_max_val(0);
		cGrid.set_step(1);
		
		CvParamGrid coeffGrid = new CvParamGrid();
		cGrid.set_min_val(0);
		cGrid.set_max_val(0);
		cGrid.set_step(1);
		
		CvParamGrid nuGrid = new CvParamGrid();
		cGrid.set_min_val(0.5);
		cGrid.set_max_val(0.5);
		cGrid.set_step(1);
		
		CvParamGrid pGrid = new CvParamGrid();
		cGrid.set_min_val(0);
		cGrid.set_max_val(0);
		cGrid.set_step(1);
		
		
		int trainingDataRows = 0;
		int trainingDataCols = featureVectors.get(0).cols();
		for(Mat classData : featureVectors)
			trainingDataRows += classData.rows();
		
		Mat trainingData = new Mat(trainingDataRows,
				trainingDataCols,CvType.CV_32FC1);
		Mat classLabels = new Mat(trainingDataRows, 1, CvType.CV_32FC1);
		int curRow = 0;
		
		for(int i=0; i<featureVectors.size(); ++i){
			for(int r=0; r<featureVectors.get(i).rows(); ++r){
				for(int c=0; c<trainingDataCols; ++c)
					trainingData.put(curRow, c, featureVectors.get(i).get(r, c));
				classLabels.put(curRow++, 1, ((i==classIndex)?1:0));
			}
		}
		
		svm = new CvSVM();
		//svm.train(featureVectors, classLabels, new Mat(), new Mat(), trainingPram);
		svm.train_auto(trainingData, classLabels, new Mat(), new Mat(),
				trainingPram, 5, cGrid, gammaGrid, pGrid, nuGrid,
				coeffGrid, degreeGrid, true);
	}
}
