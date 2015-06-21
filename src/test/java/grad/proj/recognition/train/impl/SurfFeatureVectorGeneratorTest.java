package grad.proj.recognition.train.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.utils.ImageImpl;
import grad.proj.utils.SubImageTest;
import grad.proj.utils.TestsDataSetsHelper;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;
import grad.proj.utils.SubImage;
import grad.proj.utils.TestsDataSetsHelper.DataSet;
import grad.proj.utils.TestsDataSetsHelper.Type;

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

		Image image = SubImageTest.createRandomTestImage(100, 100);
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		generator.prepareGenerator(Arrays.asList(Arrays.asList(image)));

		List<Double> generatedTry1 = generator.generateFeatureVector(image);
		List<Double> generatedTry2 = generator.generateFeatureVector(image);
		
		for(int i=0; i<generatedTry1.size(); i++)
			assertEquals(generatedTry1.get(i), generatedTry2.get(i), 0.01);
	}
	

	@Test
	public void testFeatureVectorForDifferentImagesShouldBeDifferent() throws Exception {
		Image image1 = SubImageTest.createRandomTestImage(100, 100);
		Image image2 = SubImageTest.createRandomTestImage(100, 100);
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		generator.prepareGenerator(Arrays.asList(Arrays.asList(image1, image2)));

		List<Double> generated = generator.generateFeatureVector(image1);
		
		List<Double> generatedFromDifferentImage = generator.generateFeatureVector(image2);
		
		for(int i=0; i<generated.size(); i++)
			assertNotEquals(generated.get(i), generatedFromDifferentImage.get(i), 0.0);
	}

	@Test
	public void testGenerateFeatureVectorForClearImageWithNoCorners(){
		Image image = SubImageTest.createRandomTestImage(100, 100, Color.white.getRGB());
		
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		generator.prepareGenerator(Arrays.asList(Arrays.asList(image)));
		
		List<Double> featureVector = generator.generateFeatureVector(image);
		
		assertEquals("feature vector for clear images by surf should be 0", 0, featureVector.size());
	}
}
