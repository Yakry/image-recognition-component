package grad.proj.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public abstract class FeatureVectorGeneratorTest extends RequiresLoadingTestBaseClass {

	public abstract FeatureVectorGenerator createGenerator();
	
	@Test
	public void testFeatureVectorForSameImageDoesnotChange() throws Exception {

		Image image = TestsHelper.createRandomTestImage(100, 100);
		
		FeatureVectorGenerator generator = createGenerator();
		
		Map<String, List<Image>> trainingData = new HashMap<>();
		trainingData.put("class0", Arrays.asList(image));
		
		generator.prepareGenerator(trainingData);

		FeatureVector generatedTry1 = generator.generateFeatureVector(image);
		FeatureVector generatedTry2 = generator.generateFeatureVector(image);

		assertEquals(generatedTry1.size(), generator.getFeatureVectorSize());
		assertEquals(generatedTry2.size(), generator.getFeatureVectorSize());
		
		assertEquals(generatedTry1, generatedTry2);
	}
	

	@Test
	public void testFeatureVectorForDifferentImagesShouldBeDifferent() throws Exception {
		Image image1 = TestsHelper.createRandomTestImage(100, 100);
		Image image2 = TestsHelper.createRandomTestImage(100, 100);
		
		FeatureVectorGenerator generator = createGenerator();

		Map<String, List<Image>> trainingData = new HashMap<>();
		trainingData.put("class0", Arrays.asList(image1, image2));
		
		generator.prepareGenerator(trainingData);

		FeatureVector generated = generator.generateFeatureVector(image1);
		
		FeatureVector generatedFromDifferentImage = generator.generateFeatureVector(image2);
		
		assertEquals(generated.size(), generator.getFeatureVectorSize());
		assertEquals(generatedFromDifferentImage.size(), generator.getFeatureVectorSize());

		assertNotEquals(generated, generatedFromDifferentImage);
	}

}
