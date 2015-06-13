package grad.proj.recognition.train;

import java.util.ArrayList;
import java.util.List;

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
		List<Double> vec = toDoubleList(objectFeatureVectorGenerator.generateFeatureVector(image));
		double classify = objectClassifier.classify(vec);
		return classify;
	}

	void trainToRecognize(List<Image> images) {
		objectFeatureVectorGenerator.prepareGenerator(images);
		
		List<List<Double>> trainingSet = new ArrayList<>(images.size());
		for(Image image : images){
			trainingSet.add(toDoubleList(objectFeatureVectorGenerator.generateFeatureVector(image)));
		}
		
		objectClassifier.train(trainingSet);
	}

	private static List<Double> toDoubleList(float[] arr){
		List<Double> res = new ArrayList<>(arr.length);
		
		for(float f : arr)
			res.add((double) f);
		
		return res;
	}
}
