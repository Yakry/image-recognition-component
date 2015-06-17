package grad.proj.recognition.train;

import static org.junit.Assert.*;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class SurfFeatureVectorGeneratorTest {

	private static final String IMG1_BIG = "SURF_IMG_1_BIG.jpg";
	private static final String IMG1_SMALL = "SURF_IMG_1_SMALL.jpg";
	// path relative to local machine
	private static final String DATA_FILES_PATH = "E:\\dataset";

	static{ System.load(Paths.get(System.getenv("OPENCV3_HOME"), "build", "java", System.getProperty("os.arch").contains("64") ? "x64" : "x86", System.mapLibraryName(Core.NATIVE_LIBRARY_NAME)).toString()); }

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

		Mat generatedTry1 = generator.generateFeatureVector(img1Big);
		
		Mat generatedTry2 = generator.generateFeatureVector(img1Big);
		
		for(int i=0; i<generatedTry1.cols(); i++)
			assertEquals(generatedTry1.get(0, i)[0], generatedTry2.get(0, i)[0], 0.01);
		
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
		
		generator.prepareGenerator(Arrays.asList(img1Big, img1Small));

		Mat generated = generator.generateFeatureVector(img1Big);
		
		Mat generatedFromDifferentImage = generator.generateFeatureVector(img1Small);
		
		System.out.println(generatedFromDifferentImage.cols() + " " + generatedFromDifferentImage.rows());
		
		for(int i=0; i<generated.cols(); i++)
			assertNotEquals(generated.get(0, i)[0], generatedFromDifferentImage.get(0, i)[0]);
		
		//assertFalse(Arrays.equals(generated, generatedFromDifferentImage));
	}
	
	@Test
	public void generateTestDataFile() throws Exception{
		File dataSetDirectory = new File(DATA_FILES_PATH + "\\train");
		ArrayList<File> inputImagesFiles = new ArrayList<File>();
		ArrayList<Integer> labels = new ArrayList<Integer>();
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		Integer currentLabel = 0;
		Integer classesNum = 0;
		Integer vectorsNum = 0;
		Integer featuresNum = 0;

		File[] classesDirectories = dataSetDirectory.listFiles();
		for(File classDirectory : classesDirectories){
			if(!classDirectory.isDirectory())
				continue; // for safety
			File imageFiles[] = classDirectory.listFiles();
			for(File imageFile : imageFiles){
				inputImagesFiles.add(imageFile);
				labels.add(currentLabel);
			}
			++currentLabel;
		}
		
		List<Image> inputImages = new FilesImageList(inputImagesFiles);
		
		generator.prepareGenerator(inputImages);
		classesNum = classesDirectories.length;
		vectorsNum = inputImages.size();
		featuresNum = generator.getFeatureVectorSize();
		
		FileWriter dataFile = new FileWriter("src\\test\\java\\grad"
				+ "\\proj\\recognition\\train\\dataFile1_train.txt");
		dataFile.write(classesNum.toString() + ' ');
		dataFile.write(vectorsNum.toString() + ' ');
		dataFile.write(featuresNum.toString() + '\n');
		for(int index=0;index<inputImages.size();++index){
			Mat featureVector = generator.generateFeatureVector(
					inputImages.get(index));
			dataFile.write(labels.get(index).toString() + ' ');
			for(int i=0; i<featuresNum; i++)
				dataFile.write(featureVector.get(0, i)[0] + " ");
			dataFile.write('\n');
		}
		
		dataFile.close();
	}
}
