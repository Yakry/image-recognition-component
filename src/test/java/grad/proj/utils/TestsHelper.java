package grad.proj.utils;

import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageImpl;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.util.Random;

public class TestsHelper {
	private static File DATASET_FOLDER = new File("datasets");
	
	public enum DataSet{
		calteckUniversity,
		mohsen,
		satimage,
		svmguide4,
		vowel
	}
	
	public static DataSetLoader getDataSetLoader(DataSet dataset) {
		return new DataSetLoader(getDataSetFolder(dataset));
	}
	
	private static File getDataSetFolder(DataSet dataset) {
		return new File(DATASET_FOLDER, dataset.toString());
	}

	public static void drawRectangle(Rectangle objectBounds, Image drawableImage) {
		for(int i=0;i<objectBounds.getWidth();++i){
			drawableImage.setPixelAt(objectBounds.y, objectBounds.x + i, Color.GREEN.getRGB());
			drawableImage.setPixelAt(objectBounds.y + objectBounds.height, objectBounds.x + i, Color.GREEN.getRGB());
		}
		
		for(int i=0;i<objectBounds.getHeight();++i){
			drawableImage.setPixelAt(objectBounds.y + i, objectBounds.x, Color.GREEN.getRGB());
			drawableImage.setPixelAt(objectBounds.y + i, objectBounds.x + objectBounds.width, Color.GREEN.getRGB());
		}
	}

	public static Image createRandomTestImage(int width, int height){
		return TestsHelper.createTestImage(width, height, Long.MAX_VALUE);
	}

	public static Image createTestImage(int width, int height, long color) {
		Random random = new Random();
		Image originalImage = new ImageImpl(width, height);
		for(int row=0; row<originalImage.getHeight(); row++){
			for(int col=0; col<originalImage.getWidth(); col++){
				originalImage.setPixelAt(row, col, (color == Long.MAX_VALUE) ? random.nextInt() : (int)color);
			}
		}
		return originalImage;
	}

	
}
