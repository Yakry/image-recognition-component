package grad.proj.recognition.train;

import grad.proj.Image;

public class ObjectRecognizer {
	
	private Classifier objectClassifier;
	private FeatureVectorGenerator objectFeatureVectorGenerator;

	public ObjectRecognizer(Classifier classifier,
			FeatureVectorGenerator featureVectorGenerator) {
		
		this.objectClassifier = classifier;
		this.objectFeatureVectorGenerator = featureVectorGenerator;
		
	}

	double recognize(Image image) {
		throw new UnsupportedOperationException();
	}

	void trainToRecognize(Image... images) {
		throw new UnsupportedOperationException();
	}

}
