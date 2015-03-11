package grad.proj.utils;

import java.awt.Color;
import java.io.File;
import java.nio.file.Paths;

import grad.proj.Image;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubImageTest {
	 
	private static final String IMAGE_LOADING_TEST_PNG = "ImageLoadingTest.png";

	@Test
	public void testLoadingImage() throws Exception{
		assertNotNull(getClass().getResource(IMAGE_LOADING_TEST_PNG));
		
		File imageFile = new File(getClass().getResource(IMAGE_LOADING_TEST_PNG).toURI());
		
		Image originalImage = ImageLoader.loadImage(imageFile);
		Image subImage = new SubImage(originalImage, 1, 0, 1, 2);

		assertEquals(originalImage.getPixelAt(1, 0), subImage.getPixelAt(0, 0));
		assertEquals(originalImage.getPixelAt(1, 1), subImage.getPixelAt(0, 1));
	}
}
