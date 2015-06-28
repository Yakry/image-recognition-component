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
//	private Matcher<FeatureVector> featureVectorMatcher;
//	
//	public ImageMatcher(FeatureVectorGenerator featureVectorGenerator, Matcher<FeatureVector> featureVectorMatcher) {
//		this.featureVectorGenerator = featureVectorGenerator;
//		this.featureVectorMatcher = featureVectorMatcher;
//	}
//
//	@Override
//	public List<Integer> match(Image instance, List<Image> instances, int topN) {
//		FeatureVector featureVector = featureVectorGenerator.generateFeatureVector(instance);
//
//		if(featureVector.size() == 0)
//			return new ArrayList<Integer>();
//		
//		List<FeatureVector> featureVectors = new ArrayList<FeatureVector>();
//		
//		for(Image image : instances){
//			FeatureVector generated = featureVectorGenerator.generateFeatureVector(image);
//			
//			if(generated.size() > 0)
//				featureVectors.add(generated);
//		}
//		
//		return featureVectorMatcher.match(featureVector, featureVectors, topN);
//	}
//
//}
