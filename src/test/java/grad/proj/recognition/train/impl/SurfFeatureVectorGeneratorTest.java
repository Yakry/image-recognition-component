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
	
// TODO: move to a seperate file, this is not a test
//	@Test
//	public void generateTestDataFile() throws Exception{
//		File dataSetDirectory = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH
//											+ "\\train");
//		ArrayList<File> inputImagesFiles = new ArrayList<File>();
//		ArrayList<Integer> labels = new ArrayList<Integer>();
//		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
//		Integer currentLabel = 0;
//		Integer classesNum = 0;
//		Integer vectorsNum = 0;
//		Integer featuresNum = 0;
//
//		File[] classesDirectories = dataSetDirectory.listFiles();
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue; // for safety
//			File imageFiles[] = classDirectory.listFiles();
//			for(File imageFile : imageFiles){
//				inputImagesFiles.add(imageFile);
//				labels.add(currentLabel);
//			}
//			++currentLabel;
//		}
//		
//		List<Image> inputImages = new FilesImageList(inputImagesFiles);
//		
//		generator.prepareGenerator(inputImages);
//		classesNum = classesDirectories.length;
//		vectorsNum = inputImages.size();
//		featuresNum = generator.getFeatureVectorSize();
//		
//		FileWriter dataFile = new FileWriter("dataFile1_train.txt");
//		dataFile.write(classesNum.toString() + ' ');
//		dataFile.write(vectorsNum.toString() + ' ');
//		dataFile.write(featuresNum.toString() + '\n');
//		for(int index=0;index<inputImages.size();++index){
//			List<Double> featureVector = generator.generateFeatureVector(
//					inputImages.get(index));
//			dataFile.write(labels.get(index).toString() + ' ');
//			for(int i=0; i<featuresNum; i++)
//				dataFile.write(featureVector.get(i) + " ");
//			dataFile.write('\n');
//		}
//		
//		dataFile.close();
//	}

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
