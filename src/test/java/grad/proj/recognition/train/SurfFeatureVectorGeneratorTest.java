package grad.proj.recognition.train;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import grad.proj.Image;
import grad.proj.utils.ImageLoader;

import org.junit.Test;
import org.opencv.core.Core;

public class SurfFeatureVectorGeneratorTest {

	private static final String IMG1_BIG = "SURF_IMG_1_BIG.jpg";
	private static final String IMG1_SMALL = "SURF_IMG_1_SMALL.jpg";

	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

	@Test
	public void testFeatureVectorForSameImageDoesnotChange() throws Exception {
		final URL IMG1_BIG_URL = getClass().getResource(IMG1_BIG);
		final URL IMG1_SMALL_URL = getClass().getResource(IMG1_SMALL);

		assertNotNull(IMG1_BIG_URL);
		assertNotNull(IMG1_SMALL_URL);

		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();

		Image img1Big = ImageLoader.loadImage(new File(IMG1_BIG_URL.toURI()));
		//Image img1Small = ImageLoader.loadImage(new File(IMG1_SMALL_URL.toURI()));
		
		generator.prepareGenerator(img1Big);

		float[] generatedTry1 = generator.generateFeatureVector(img1Big);
		
		float[] generatedTry2 = generator.generateFeatureVector(img1Big);
		
		assertArrayEquals(generatedTry1, generatedTry2, 0.0001f);

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
		
		generator.prepareGenerator(img1Big, img1Small);

		float[] generated = generator.generateFeatureVector(img1Big);
		
		float[] generatedFromDifferentImage = generator.generateFeatureVector(img1Small);
		
		assertFalse(Arrays.equals(generated, generatedFromDifferentImage));
	}
}
