package grad.proj.matching;

import java.util.ArrayList;
import java.util.List;

import grad.proj.recognition.train.FeatureVectorGenerator;
import grad.proj.utils.Image;

public class ImageMatcher implements Matcher<Image> {

	private FeatureVectorGenerator featureVectorGenerator;
	private Matcher<List<Double>> featureVectorMatcher;
	
	public ImageMatcher(FeatureVectorGenerator featureVectorGenerator, Matcher<List<Double>> featureVectorMatcher) {
		this.featureVectorGenerator = featureVectorGenerator;
		this.featureVectorMatcher = featureVectorMatcher;
	}

	@Override
	public List<Integer> match(Image instance, List<Image> instances, int topN) {
		List<Double> featureVector = featureVectorGenerator.generateFeatureVector(instance);
		List<List<Double>> featureVectors = new ArrayList<List<Double>>();
		
		for(Image image : instances){
			featureVectors.add(featureVectorGenerator.generateFeatureVector(image));
		}
		
		return featureVectorMatcher.match(featureVector, featureVectors, topN);
	}

}
