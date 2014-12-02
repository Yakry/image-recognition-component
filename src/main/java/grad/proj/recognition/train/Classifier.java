package grad.proj.recognition.train;

import java.io.Serializable;

interface Classifier extends Serializable {
	double classify(double[] featureVector);

	void trainToClassify(double[]...featuresVectors);
}
