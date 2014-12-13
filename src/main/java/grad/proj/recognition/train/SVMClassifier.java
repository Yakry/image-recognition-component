package grad.proj.recognition.train;

import java.util.List;

import libsvm.*;

public class SVMClassifier implements Classifier {
	private static final long serialVersionUID = 1L;
	private svm_model model = null;
	
	@Override
	public double classify(List<Double> featureVector) {
		// constructing a featureArray from the featureVector
		int vectorSize = featureVector.size();
		svm_node featureArray[] = new svm_node[vectorSize];
		for(int i=0;i<vectorSize;++i){
			featureArray[i] = new svm_node();
			featureArray[i].index = i+1;
			featureArray[i].value = featureVector.get(i);
		}
		
		//prediction
		return svm.svm_predict(model, featureArray);
	}

	@Override
	public void train(List<List<Double>> featureVectors) {
		// initializing training parameters
		svm_parameter trainingPram = new svm_parameter();
		trainingPram.svm_type = svm_parameter.ONE_CLASS;
		trainingPram.kernel_type = svm_parameter.RBF;
		trainingPram.degree = 0; 	// not needed with RBF kernel
		trainingPram.gamma = 1; 	// initial default value
		trainingPram.coef0 = 0; 	// not needed with RBF kernel
		trainingPram.C = 1; 		// not needed with ONE_CLASS classification
		trainingPram.nu = 0; 		// initial default value
		trainingPram.p = 0; 		// not needed with ONE_CLASS classification
		trainingPram.weight = new double[1];
		trainingPram.weight[0] = 1.0;
		
		// constructing a featuresMatrix from the featureVectors
		int vectorsNum = featureVectors.size();
		int vectorSize = featureVectors.get(0).size();
		double classLabel[] = new double[vectorsNum];
		svm_node featuresMatrix[][] = new svm_node[vectorsNum][vectorSize];
		for(int i=0;i<vectorsNum;++i){
			classLabel[i] = 1;
			for(int j=0;j<vectorSize;++j){
				featuresMatrix[i][j] = new svm_node();
				featuresMatrix[i][j].index = j+1;
				featuresMatrix[i][j].value = featureVectors.get(i).get(j);
			}
		}
		
		// encapsulating the training data
		svm_problem trainingData = new svm_problem();
		trainingData.l = vectorsNum;
		trainingData.x = featuresMatrix;
		trainingData.y = classLabel;
		
		// training
		model = svm.svm_train(trainingData, trainingPram);
	}
}
