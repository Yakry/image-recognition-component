package grad.proj.classification.impl;

import static org.junit.Assert.assertEquals;
import grad.proj.classification.FeatureVector;
import grad.proj.classification.FeatureVectorGenerator;
import grad.proj.classification.FeatureVectorGeneratorTest;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.imaging.Image;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SurfFeatureVectorGeneratorTest extends FeatureVectorGeneratorTest {

	@Override
	public FeatureVectorGenerator createGenerator() {
		return new SurfFeatureVectorGenerator();
	}
	
	@Test
	public void testGenerateFeatureVectorForClearImageWithNoCorners(){
		Image image = TestsHelper.createTestImage(100, 100, Color.white.getRGB());
		
		FeatureVectorGenerator generator = createGenerator();
		
		Map<String, List<Image>> trainingData = new HashMap<>();
		trainingData.put("class0", Arrays.asList(image));

		generator.prepareGenerator(trainingData);
		
		FeatureVector featureVector = generator.generateFeatureVector(image);
		
		assertEquals("feature vector for clear images by surf should be 0", 0, featureVector.size());
	}
}
