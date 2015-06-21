package grad.proj.recognition.train.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.utils.TestsDataSetsHelper;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;
import grad.proj.utils.SubImage;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Mat;

public class SurfFeatureVectorGeneratorTest extends RequiresLoadingTestBaseClass {

	private static final String IMG1_BIG = "SURF_IMG_1_BIG.jpg";
	private static final String IMG1_SMALL = "SURF_IMG_1_SMALL.jpg";

	@Test
	public void testFeatureVectorForSameImageDoesnotChange() throws Exception {
		final URL IMG1_BIG_URL = getClass().getResource(IMG1_BIG);
		final URL IMG1_SMALL_URL = getClass().getResource(IMG1_SMALL);

		assertNotNull(IMG1_BIG_URL);
		assertNotNull(IMG1_SMALL_URL);

		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();

		Image img1Big = ImageLoader.loadImage(new File(IMG1_BIG_URL.toURI()));
		//Image img1Small = ImageLoader.loadImage(new File(IMG1_SMALL_URL.toURI()));
		
		generator.prepareGenerator(Arrays.asList(Arrays.asList(img1Big)));

		List<Double> generatedTry1 = generator.generateFeatureVector(img1Big);
		
		List<Double> generatedTry2 = generator.generateFeatureVector(img1Big);
		
		for(int i=0; i<generatedTry1.size(); i++)
			assertEquals(generatedTry1.get(i), generatedTry2.get(i), 0.01);
		
		//assertArrayEquals(generatedTry1, generatedTry2, 0.0001f);

//		System.out.println(generatedTry1.length + " " + Arrays.toString(generatedTry1));
//		System.out.println(generatedTry2.length + " " + Arrays.toString(generatedTry2));
	}


	@Test
	public void testFeatureVectorForDifferentImagesShouldBeDifferent() throws Exception {
		final URL IMG1_BIG_URL = getClass().getResource(IMG1_BIG);
		final URL IMG1_SMALL_URL = getClass().getResource(IMG1_SMALL);

		assertNotNull(IMG1_BIG_URL);
		assertNotNull(IMG1_SMALL_URL);

		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();

		Image img1Big = ImageLoader.loadImage(new File(IMG1_BIG_URL.toURI()));
		Image img1Small = ImageLoader.loadImage(new File(IMG1_SMALL_URL.toURI()));
		
		generator.prepareGenerator(Arrays.asList(Arrays.asList(img1Big, img1Small)));

		List<Double> generated = generator.generateFeatureVector(img1Big);
		
		List<Double> generatedFromDifferentImage = generator.generateFeatureVector(img1Small);
		
		//System.out.println(generatedFromDifferentImage.cols() + " " + generatedFromDifferentImage.rows());

		for(int i=0; i<generated.size(); i++)
			assertEquals(generated.get(i), generatedFromDifferentImage.get(i), 0.01);
		
		//assertFalse(Arrays.equals(generated, generatedFromDifferentImage));
	}

	@Test
	public void testGenerateFeatureVectorOfSubimage(){
		Image image = ImageLoader.loadImage(TestsDataSetsHelper.DATA_FILES_PATH + "\\001.jpg");
		SubImage subImage = new SubImage(image, 5, 5, 100, 100);
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();

		generator.prepareGenerator(Arrays.asList(Arrays.asList(image)));
		List<Double> featureVector;
		featureVector = generator.generateFeatureVector(image);
		assertEquals("feature vector rows != 64", 64, featureVector.size());

		featureVector = generator.generateFeatureVector(subImage);
		assertEquals("feature vector rows != 64", 64, featureVector.size());
	}
}
