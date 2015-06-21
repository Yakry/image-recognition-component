package grad.proj.recognition.train.impl;

public class ImageClassifierTest {

//	@Test
//	public void testRealImages() throws Exception{
//		File trainDataSetDirectory = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH
//												+ "\\train");
//		ArrayList<File> inputImagesFiles = new ArrayList<File>();
//		ArrayList<Integer> labels = new ArrayList<Integer>();
//		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
//		SVMClassifier classifier = new SVMClassifier();
//		Integer currentLabel = 0;
//		Integer classesNum = 0;
//		Integer featuresNum = 0;
//		ArrayList<Integer> classVectorsNum = new ArrayList<Integer>();
//
//		File[] classesDirectories = trainDataSetDirectory.listFiles();
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue;
//			File imageFiles[] = classDirectory.listFiles();
//			classVectorsNum.add(imageFiles.length);
//			for(File imageFile : imageFiles){
//				inputImagesFiles.add(imageFile);
//				labels.add(currentLabel);
//			}
//			++currentLabel;
//		}
//		
//		List<Image> inputImages = new FilesImageList(inputImagesFiles);
//		generator.prepareGenerator(inputImages);
//		
//		classesNum = classesDirectories.length;
//		featuresNum = generator.getFeatureVectorSize();
//		
//		List<Mat> trainingData = new ArrayList<Mat>(classesNum);
//		int index = 0;
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue; // for safety
//			Mat classTrainingData = new Mat(classVectorsNum.get(index++),
//					featuresNum, CvType.CV_32FC1);
//			File imageFiles[] = classDirectory.listFiles();
//			int row = 0;
//			for(File imageFile : imageFiles){
//				List<Double> featureVector = generator.generateFeatureVector(ImageLoader.loadImage(imageFile));
//				for(int col = 0;col<featuresNum;++col)
//					classTrainingData.put(row, col, featureVector.get(0, col)[0]);
//				++row;
//			}
//			trainingData.add(classTrainingData);
//		}
//		
//		classifier.train(trainingData);
//		
//		File testDataSetDirectory = new File(TestsDataSetsHelper.CLASSIFIER_FILES_PATH
//												+ "\\test");
//		double correctLabels = 0;
//		double numberOfRows = 0;
//		int classLabel = 0;
//		
//		classesDirectories = testDataSetDirectory.listFiles();
//		
//		for(File classDirectory : classesDirectories){
//			if(!classDirectory.isDirectory())
//				continue; // for safety
//			File imageFiles[] = classDirectory.listFiles();
//			numberOfRows += imageFiles.length;
//			for(File imageFile : imageFiles){
//				List<Double> testVector = generator.generateFeatureVector(ImageLoader.loadImage(imageFile));
//				double predictedLabel = classifier.classify(testVector);
//				correctLabels += ((classLabel == predictedLabel)?1:0);
//			}
//			++classLabel;
//		}
//		
//		System.out.println("MultiClassSVMClassifierTest::testRealData:");
//		System.out.println("number of vectors: " + numberOfRows);
//		System.out.println("number of correctly classified vectors: " +
//				correctLabels);
//		System.out.println("percentage: " + (correctLabels*100)/numberOfRows +
//				"%");
//		assertTrue("correct predicted labels percentage below 75%",
//				((correctLabels*100)/numberOfRows) >= 75.0);
//	}
}
