package grad.proj.recognition.localization;

import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.DataFilesPathWrapper;
import grad.proj.utils.FilesImageList;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class BranchAndBoundLocalizerTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testBranchAndBound(){
		File trainFolder = new File(DataFilesPathWrapper.CLASSIFIER_FILES_PATH + "\\train\\apple");
//		File testFolder = new File(DataFilesPathWrapper.CLASSIFIER_FILES_PATH + "\\test\\apple");
		
		ArrayList<File> trainImagesFiles = new ArrayList<File>();
		for(File file : trainFolder.listFiles()){
			trainImagesFiles.add(file);
		}
		List<Image> trainImages = new FilesImageList(trainImagesFiles);
		
		SVMClassifier svm = new SVMClassifier();

		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		featureVectorGenerator.prepareGenerator(trainImages);
		
		Mat trainImagesMat = new Mat(trainImages.size(), featureVectorGenerator.getFeatureVectorSize(), CvType.CV_32SC1);
		int cur = 0;
		for(Image image : trainImages){
			Mat f = featureVectorGenerator.generateFeatureVector(image);
			for(int i=0; i<featureVectorGenerator.getFeatureVectorSize(); i++)
				trainImagesMat.get(cur, i)[0] = f.get(0, i)[0];
			cur++;
		}
		svm.train(Arrays.asList(trainImagesMat, Mat.eye(1, 64, CvType.CV_32SC1)));
		
		Image testImage = ImageLoader.loadImage(new File(DataFilesPathWrapper.CLASSIFIER_FILES_PATH + "\\test\\apple\\test05.jpg"));
		
		Rectangle objectBounds = new BranchAndBoundLocalizer().getObjectBounds(testImage, svm.svmArray[0], featureVectorGenerator);
		
		Rectangle real = new Rectangle(504, 21, 358, 293);

		System.out.println("errorLeft = " + Math.abs(real.x - objectBounds.x));
		System.out.println("errorTop = " + Math.abs(real.y - objectBounds.y));
		System.out.println("errorRight = " + Math.abs((real.x+real.width) - (objectBounds.x + objectBounds.width)));
		System.out.println("errorBottom = " + Math.abs((real.y+real.height) - (objectBounds.y + objectBounds.height)));
	}
}
