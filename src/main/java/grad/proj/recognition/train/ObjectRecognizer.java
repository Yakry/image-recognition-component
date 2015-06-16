package grad.proj.recognition.train;

import java.util.ArrayList;
import java.util.List;

import grad.proj.utils.Image;

public class ObjectRecognizer {
	
	private Classifier objectClassifier;
	private FeatureVectorGenerator objectFeatureVectorGenerator;

	public ObjectRecognizer(Classifier classifier,
			FeatureVectorGenerator featureVectorGenerator) {
		
		this.objectClassifier = classifier;
		this.objectFeatureVectorGenerator = featureVectorGenerator;
		
	}

	double recognize(Image image) {
		return 0.0;
	}

	void trainToRecognize(List<Image> images) {
		
	}

	private static List<Double> toDoubleList(float[] arr){
		List<Double> res = new ArrayList<>(arr.length);
		
		for(float f : arr)
			res.add((double) f);
		
		return res;
	}
}
