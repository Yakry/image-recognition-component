package grad.proj.utils;

import java.awt.Color;
import java.io.File;
import java.nio.file.Paths;

import grad.proj.Image;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageLoaderTest {
	 
	private static final String IMAGE_LOADING_TEST_PNG = "ImageLoadingTest.png";

	@Test
	public void testLoadingImage() throws Exception{
		assertNotNull(getClass().getResource(IMAGE_LOADING_TEST_PNG));
		
		File imageFile = new File(getClass().getResource(IMAGE_LOADING_TEST_PNG).toURI());
		
		Image loadedImage = ImageLoader.loadImage(imageFile);

		assertEquals(new Color(0,0,0).getRGB(), loadedImage.getPixelAt(0, 0));
		assertEquals(new Color(237,28,36).getRGB(), loadedImage.getPixelAt(0, 1));
		assertEquals(new Color(34,177,76).getRGB(), loadedImage.getPixelAt(1, 0));
		assertEquals(new Color(0,162,232).getRGB(), loadedImage.getPixelAt(1, 1));
	}
}
