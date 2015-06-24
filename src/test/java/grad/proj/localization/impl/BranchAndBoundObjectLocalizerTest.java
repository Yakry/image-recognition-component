package grad.proj.localization.impl;

import grad.proj.classification.ImageClassifier;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;
import grad.proj.classification.impl.SurfFeatureVectorGenerator;
import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;
import grad.proj.localization.impl.QualityFunction;
import grad.proj.localization.impl.SurfLinearSvmQualityFunction;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.TestsHelper;
import grad.proj.utils.TestsHelper.DataSet;
import grad.proj.utils.DataSetLoader.Type;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;
import grad.proj.utils.opencv.RequiresLoadingTestBaseClass;

import java.awt.Rectangle;
import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.opencv.core.Mat;

public class BranchAndBoundObjectLocalizerTest extends RequiresLoadingTestBaseClass {

	@Test
	public void testBranchAndBoundOnCaltech(){
		testBranchAndBoundOnOneClass(DataSet.calteckUniversity, "apple");
	}
	
	@Test
	public void testBranchAndBoundOnMohsen(){
		testBranchAndBoundOnOneClass(DataSet.mohsen, "mouse");
	}
	
	public void testBranchAndBoundOnOneClass(DataSet dataSet, String testClass) {
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(dataSet);
		ImageClassifier classifier = dataSetLoader.loadTrainedClassifier();
		
		List<Image> testingData = dataSetLoader.loadClassImages(Type.Localization, testClass);
		
		ObjectLocalizer localizer = new BranchAndBoundObjectLocalizer(new SurfLinearSvmQualityFunction());
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingData){
			System.out.println("starting search " + searchIndex);
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, testClass);
			TestsHelper.drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File("branchAndBound" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}
}
