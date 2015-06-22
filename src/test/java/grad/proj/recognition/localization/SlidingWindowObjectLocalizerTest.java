package grad.proj.recognition.localization;

import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.ImageClassifier;
import grad.proj.recognition.train.impl.LinearNormalizer;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;
import grad.proj.utils.TestsDataSetsHelper;
import grad.proj.utils.TestsDataSetsHelper.DataSet;
import grad.proj.utils.TestsDataSetsHelper.Type;

import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import org.junit.Test;

public class SlidingWindowObjectLocalizerTest extends RequiresLoadingTestBaseClass {
	
	@Test
	public void testSlidingWindow() throws Exception {
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		List<List<Image>> trainingData = TestsDataSetsHelper.loadDataSetImages(DataSet.calteckUniversity, Type.Train, "apple", "can");
		List<Image> testingClassData = TestsDataSetsHelper.loadDataSetClassImages(DataSet.calteckUniversity, Type.Test, "apple");
		
		classifier.train(trainingData);
		
		ObjectLocalizer localizer = new SlidingWindowObjectLocalizer();
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingClassData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, 0);
			BranchAndBoundObjectLocalizerTest.drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File("slidingWindow" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}
}
