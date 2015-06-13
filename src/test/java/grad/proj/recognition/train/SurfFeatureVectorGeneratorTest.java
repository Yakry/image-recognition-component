package grad.proj.recognition.train;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import grad.proj.Image;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.ImageLoader;

import org.junit.Test;
import org.opencv.core.Core;

public class SurfFeatureVectorGeneratorTest {

	private static final String IMG1_BIG = "SURF_IMG_1_BIG.jpg";
	private static final String IMG1_SMALL = "SURF_IMG_1_SMALL.jpg";
	// path relative to local machine
	private static final String DATA_FILES_PATH = "E:\\Education\\University\\Lectures and Materials\\4th Year - 2nd Term\\GP\\ImagesDataSet";

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
		
		generator.prepareGenerator(Arrays.asList(img1Big));

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
		
		generator.prepareGenerator(Arrays.asList(img1Big, img1Small));

		float[] generated = generator.generateFeatureVector(img1Big);
		
		float[] generatedFromDifferentImage = generator.generateFeatureVector(img1Small);
		
		assertFalse(Arrays.equals(generated, generatedFromDifferentImage));
	}
	
	@Test
	public void generateDataFile() throws Exception{
		File dataSetDirectory = new File(DATA_FILES_PATH + "\\train");
		ArrayList<File> inputImagesFiles = new ArrayList<File>();
		ArrayList<Integer> labels = new ArrayList<Integer>();
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		Integer currentLabel = 0;
		Integer classesNum = 0;
		Integer vectorsNum = 0;
		Integer featuresNum = 0;

		File[] subDirectoriesNames = dataSetDirectory.listFiles();
		 
		for(File objectDirectory : subDirectoriesNames){

			if(!objectDirectory.isDirectory())
				continue; // for safety
			
			File imageFiles[] = objectDirectory.listFiles();
			
			for(File imageFile : imageFiles){
				inputImagesFiles.add(imageFile);
				labels.add(currentLabel);
			}
			++currentLabel;
		}
		
		List<Image> inputImages = new FilesImageList(inputImagesFiles);
		
		generator.prepareGenerator(inputImages);
		classesNum = subDirectoriesNames.length;
		vectorsNum = inputImages.size();
		featuresNum = 64;
		
		FileWriter dataFile = new FileWriter("src\\test\\java\\grad"
				+ "\\proj\\recognition\\train\\dataFile1.txt");
		dataFile.write(classesNum.toString() + ' ');
		dataFile.write(vectorsNum.toString() + ' ');
		dataFile.write(featuresNum.toString() + '\n');
		for(int index=0;index<inputImages.size();++index){
			float featureVector[] = generator.generateFeatureVector(
					inputImages.get(index));
			
			// not the right way of testing it
			//assertEquals("featureVector length not equal 64", 
			//		64, featureVector.length);
			
			dataFile.write(labels.get(index).toString() + ' ');
			for(Float val : featureVector)
				dataFile.write(val.toString() + ' ');
			dataFile.write('\n');
		}
		
		dataFile.close();
	}
}
