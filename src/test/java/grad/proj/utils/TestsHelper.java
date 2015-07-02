package grad.proj.utils;

import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ArrayImage;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.util.Random;

public class TestsHelper {
	private static File DATASET_FOLDER = new File("datasets");
	private static File TEST_RESULTS = new File("testResults");
	
	public enum DataSet{
		calteckUniversity,
		mohsen1,
		mohsen2,
		satimage,
		svmguide4,
		vowel
	}
	
	public static DataSetLoader getDataSetLoader(DataSet dataset) {
		DataSetLoader dataSetLoader = new DataSetLoader(getDataSetFolder(dataset));
		if(dataSetLoader.hasImages()){
			if(!dataSetLoader.hasFeaturesFile() || !dataSetLoader.hasSurfGenerator()){
					dataSetLoader.generateAndSave();
			}
		}
		return dataSetLoader;
	}
	
	private static File getDataSetFolder(DataSet dataset) {
		return new File(DATASET_FOLDER, dataset.toString());
	}

	public static void drawRectangle(Rectangle objectBounds, Image drawableImage, Color color) {
		for(int i=0;i<objectBounds.getWidth();++i){
			drawableImage.setPixelAt(objectBounds.y, objectBounds.x + i, color.getRGB());
			drawableImage.setPixelAt(objectBounds.y + objectBounds.height, objectBounds.x + i, color.getRGB());
		}
		
		for(int i=0;i<objectBounds.getHeight();++i){
			drawableImage.setPixelAt(objectBounds.y + i, objectBounds.x, color.getRGB());
			drawableImage.setPixelAt(objectBounds.y + i, objectBounds.x + objectBounds.width, color.getRGB());
		}
	}

	public static Image createRandomTestImage(int width, int height){
		return TestsHelper.createTestImage(width, height, Long.MAX_VALUE);
	}

	public static Image createTestImage(int width, int height, long color) {
		Random random = new Random();
		Image originalImage = new ArrayImage(width, height);
		for(int row=0; row<originalImage.getHeight(); row++){
			for(int col=0; col<originalImage.getWidth(); col++){
				originalImage.setPixelAt(row, col, (color == Long.MAX_VALUE) ? random.nextInt() : (int)color);
			}
		}
		return originalImage;
	}
	
	public static File getTestResultsFolder(Class<?> testClass, String subFolder){
		File classResultsFolder = new File(TEST_RESULTS, testClass.getSimpleName());
		File classSubFolder = new File(classResultsFolder, subFolder);
		if(!classSubFolder.exists()){
			classSubFolder.mkdirs();
		}
		return classSubFolder;
	}

	
}
