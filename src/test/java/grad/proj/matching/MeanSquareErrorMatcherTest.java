package grad.proj.matching;

import static org.junit.Assert.*;

import org.junit.Test;

import grad.proj.classification.ArrayFeatureVector;
import grad.proj.classification.FeatureVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeanSquareErrorMatcherTest {

	@Test
	public void testSimpleMatchingTop2(){
		List<FeatureVector> vecs = new ArrayList<FeatureVector>();
		vecs.add(new ArrayFeatureVector(0.1, 0.2));
		vecs.add(new ArrayFeatureVector(0.3, 0.6));
		vecs.add(new ArrayFeatureVector(0.2, 0.2));
		
		Matcher<FeatureVector> matcher = new MeanSquareErrorMatcher();
		List<FeatureVector> resultIndicies = matcher.match(new ArrayFeatureVector(0.1, 0.1), vecs, 2);
		assertEquals(new ArrayFeatureVector(0.1, 0.2), resultIndicies.get(0));
		assertEquals(new ArrayFeatureVector(0.2, 0.2), resultIndicies.get(1));	
	}
	
	@Test
	public void testAnotherSimpleMatchingTop2(){
		List<FeatureVector> vecs = new ArrayList<FeatureVector>();
		vecs.add(new ArrayFeatureVector(0.1, 0.2));
		vecs.add(new ArrayFeatureVector(0.3, 0.6));
		vecs.add(new ArrayFeatureVector(0.2, 0.2));
		
		Matcher<FeatureVector> matcher = new MeanSquareErrorMatcher();
		List<FeatureVector> resultIndicies = matcher.match(new ArrayFeatureVector(0.5, 0.6), vecs, 2);
		assertEquals(new ArrayFeatureVector(0.3, 0.6), resultIndicies.get(0));
		assertEquals(new ArrayFeatureVector(0.2, 0.2), resultIndicies.get(1));	
	}
}
