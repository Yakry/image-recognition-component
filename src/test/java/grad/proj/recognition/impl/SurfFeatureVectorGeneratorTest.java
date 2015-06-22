package grad.proj.recognition.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageImpl;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.imaging.SubImage;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.opencv.core.Mat;

public class SurfFeatureVectorGeneratorTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testFeatureVectorForSameImageDoesnotChange() throws Exception {

		Image image = TestsHelper.createRandomTestImage(100, 100);
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		generator.prepareGenerator(Arrays.asList(Arrays.asList(image)));

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
		generator.prepareGenerator(Arrays.asList(Arrays.asList(image1, image2)));

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
		generator.prepareGenerator(Arrays.asList(Arrays.asList(image)));
		
		List<Double> featureVector = generator.generateFeatureVector(image);
		
		assertEquals("feature vector for clear images by surf should be 0", 0, featureVector.size());
	}
}
