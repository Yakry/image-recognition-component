package grad.proj.utils.imaging;

import grad.proj.utils.TestsHelper;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.SubImage;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubImageTest {
	
	@Test
	public void testGetPixelWithCorrectBounds() throws Exception{

		Image originalImage = TestsHelper.createRandomTestImage(10, 10);
		
		int startRow = 3, startCol = 5, width = 4, height = 4;
		
		Image subImage = new SubImage(originalImage, startCol, startRow, width, height);
		
		for(int row=0; row<subImage.getHeight(); row++){
			for(int col=0; col<subImage.getWidth(); col++){
				assertEquals(originalImage.getPixelAt(row + startRow, col + startCol), subImage.getPixelAt(row, col));
			}
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetPixelWithOutOfBounds() throws Throwable{
		Image originalImage = TestsHelper.createRandomTestImage(10, 10);
		
		int startRow = 3, startCol = 5, width = 4, height = 4;
		
		Image subImage = new SubImage(originalImage, startCol, startRow, width, height);
		subImage.getPixelAt(5, 3);
	}

	@SuppressWarnings("unused")
	@Test(expected=RuntimeException.class)
	public void testConstructSubImageOutOfBoundsOfOriginalImage() throws Throwable{
		Image originalImage = TestsHelper.createRandomTestImage(10, 10);
		
		int startRow = 3, startCol = 5, width = 12, height = 4;
		
		Image subImage = new SubImage(originalImage, startCol, startRow, width, height);
	}
}
