package grad.proj.recognition.train;

import java.io.Serializable;
import java.util.List;

interface Classifier extends Serializable {
	double classify(List<Double> featureVector);

	void trainToClassify(List<List<Double> > featureVectors);
}
