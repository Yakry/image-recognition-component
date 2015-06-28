package grad.proj.classification;

import grad.proj.utils.imaging.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FeatureVectorImageClassifier implements Classifier<Image> {
	private static final long serialVersionUID = 8364985649869770754L;
	
	private FeatureVectorGenerator featureVectorGenerator;
	private Classifier<FeatureVector> classifier;
	
	public FeatureVectorImageClassifier(FeatureVectorGenerator featureVectorGenerator, Classifier<FeatureVector> classifier) {
		this.featureVectorGenerator = featureVectorGenerator;
		this.classifier = classifier;
	}

	@Override
	public String classify(Image instance) {
		FeatureVector featureVector = featureVectorGenerator.generateFeatureVector(instance);
		return classifier.classify(featureVector);
	}

	@Override
	public double classify(Image instance, String classLabel) {
		FeatureVector featureVector = featureVectorGenerator.generateFeatureVector(instance);
		if(featureVector.size() != featureVectorGenerator.getFeatureVectorSize())
			return Double.MAX_VALUE;
		return classifier.classify(featureVector, classLabel);
	}

	@Override
	public <CollectionImage extends Collection<? extends Image>> void train(Map<String, CollectionImage> trainingData)  {
		Map<String, List<FeatureVector>> trainingDataAsDouble = new HashMap<>();
		
		featureVectorGenerator.prepareGenerator(trainingData);
		
		for (Entry<String, CollectionImage> clazzEntry : trainingData.entrySet()) {
			String className = clazzEntry.getKey();
			CollectionImage clazz = clazzEntry.getValue();
			
			List<FeatureVector> classTrainingData = new ArrayList<>();
			for (Image image : clazz) {
				FeatureVector featureVector = featureVectorGenerator.generateFeatureVector(image);
				classTrainingData.add(featureVector);
			}
			
			trainingDataAsDouble.put(className, classTrainingData);
		}
		
		classifier.train(trainingDataAsDouble);
	}

	@Override
	public Set<String> getClasses() {
		return classifier.getClasses();
	}

	public FeatureVectorGenerator getFeatureVectorGenerator() {
		return featureVectorGenerator;
	}

	public Classifier<FeatureVector> getClassifier() {
		return classifier;
	}
}
