package grad.proj.classification.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SurfFeatureVectorGeneratorTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testFeatureVectorForSameImageDoesnotChange() throws Exception {

		Image image = TestsHelper.createRandomTestImage(100, 100);
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		
		Map<String, List<Image>> trainingData = new HashMap<>();
		trainingData.put("class0", Arrays.asList(image));
		
		generator.prepareGenerator(trainingData);

		List<Double> generatedTry1 = generator.generateFeatureVector(image);
		List<Double> generatedTry2 = generator.generateFeatureVector(image);

		assertEquals(generatedTry1.size(), generator.getFeatureVectorSize());
		assertEquals(generatedTry2.size(), generator.getFeatureVectorSize());
		
		for(int i=0; i<generatedTry1.size(); i++)
			assertEquals(generatedTry1.get(i), generatedTry2.get(i), 0.01);
	}
	

	@Test
	public void testFeatureVectorForDifferentImagesShouldBeDifferent() throws Exception {
		Image image1 = TestsHelper.createRandomTestImage(100, 100);
		Image image2 = TestsHelper.createRandomTestImage(100, 100);
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();

		Map<String, List<Image>> trainingData = new HashMap<>();
		trainingData.put("class0", Arrays.asList(image1, image2));
		
		generator.prepareGenerator(trainingData);

		List<Double> generated = generator.generateFeatureVector(image1);
		
		List<Double> generatedFromDifferentImage = generator.generateFeatureVector(image2);
		
		assertEquals(generated.size(), generator.getFeatureVectorSize());
		assertEquals(generatedFromDifferentImage.size(), generator.getFeatureVectorSize());
		
		for(int i=0; i<generated.size(); i++)
			assertNotEquals(generated.get(i), generatedFromDifferentImage.get(i), 0.0);
	}

	@Test
	public void testGenerateFeatureVectorForClearImageWithNoCorners(){
		System.out.println(Color.white.getRGB());
		Image image = TestsHelper.createTestImage(100, 100, Color.white.getRGB());
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		
		Map<String, List<Image>> trainingData = new HashMap<>();
		trainingData.put("class0", Arrays.asList(image));

		generator.prepareGenerator(trainingData);
		
		List<Double> featureVector = generator.generateFeatureVector(image);
		
		assertEquals("feature vector for clear images by surf should be 0", 0, featureVector.size());
	}
}
