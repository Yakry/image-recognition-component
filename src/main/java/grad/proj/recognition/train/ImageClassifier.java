package grad.proj.recognition.train;

import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageClassifier implements Classifier<Image> {
	private static final long serialVersionUID = 8364985649869770754L;
	
	private FeatureVectorGenerator featureVectorGenerator;
	private FeatureVectorClassifier classifier;
	
	public ImageClassifier(FeatureVectorGenerator featureVectorGenerator, FeatureVectorClassifier classifier) {
		this.featureVectorGenerator = featureVectorGenerator;
		this.classifier = classifier;
	}

	@Override
	public int classify(Image instance) {
		List<Double> featureVector = featureVectorGenerator.generateFeatureVector(instance);
		return classifier.classify(featureVector);
	}

	@Override
	public double classify(Image instance, int classLabel) {
		List<Double> featureVector = featureVectorGenerator.generateFeatureVector(instance);
		return classifier.classify(featureVector, classLabel);
	}

	@Override
	public void train(List<List<Image>> trainingData) {
		List<List<List<Double>>> trainingDataAsDouble = new ArrayList<>();
		
		List<Image> allimages = new ArrayList<>();
		for (List<Image> clazz : trainingData) 
			allimages.addAll(clazz);
		
		featureVectorGenerator.prepareGenerator(allimages);
		
		for (List<Image> clazz : trainingData) {
			List<List<Double>> classTrainingData = new ArrayList<>();
			for (Image image : clazz) {
				List<Double> featureVector = featureVectorGenerator.generateFeatureVector(image);
				classTrainingData.add(featureVector);
			}
			trainingDataAsDouble.add(classTrainingData);
		}
		
		classifier.train(trainingDataAsDouble);
		return;
	}

	@Override
	public int getClassesNo() {
		return classifier.getClassesNo();
	}

}
