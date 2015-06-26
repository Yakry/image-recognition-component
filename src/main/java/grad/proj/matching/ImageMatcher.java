//package grad.proj.matching;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import grad.proj.classification.FeatureVectorGenerator;
//import grad.proj.utils.imaging.Image;
//
//public class ImageMatcher implements Matcher<Image> {
//
//	private FeatureVectorGenerator featureVectorGenerator;
//	private Matcher<List<Double>> featureVectorMatcher;
//	
//	public ImageMatcher(FeatureVectorGenerator featureVectorGenerator, Matcher<List<Double>> featureVectorMatcher) {
//		this.featureVectorGenerator = featureVectorGenerator;
//		this.featureVectorMatcher = featureVectorMatcher;
//	}
//
//	@Override
//	public List<Integer> match(Image instance, List<Image> instances, int topN) {
//		List<Double> featureVector = featureVectorGenerator.generateFeatureVector(instance);
//
//		if(featureVector.size() == 0)
//			return new ArrayList<Integer>();
//		
//		List<List<Double>> featureVectors = new ArrayList<List<Double>>();
//		
//		for(Image image : instances){
//			List<Double> generated = featureVectorGenerator.generateFeatureVector(image);
//			
//			if(generated.size() > 0)
//				featureVectors.add(generated);
//		}
//		
//		return featureVectorMatcher.match(featureVector, featureVectors, topN);
//	}
//
//}
