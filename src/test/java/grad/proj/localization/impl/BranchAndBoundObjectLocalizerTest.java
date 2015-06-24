package grad.proj.localization.impl;

import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;
import grad.proj.localization.impl.QualityFunction;
import grad.proj.localization.impl.SurfLinearSvmQualityFunction;
import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
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
	public void testBranchAndBound() throws Exception {
		SurfFeatureVectorGenerator featureVectorGenerator = new SurfFeatureVectorGenerator();
		SVMClassifier svmClassifier = new SVMClassifier(new LinearNormalizer());
		
		ImageClassifier classifier = new ImageClassifier(featureVectorGenerator, svmClassifier);
		
		DataSetLoader dataSetLoader = TestsHelper.getDataSetLoader(DataSet.calteckUniversity);
		Map<String, List<Image>> trainingData = dataSetLoader.loadImages( Type.Train, "mouse", "clipper", "toy1", "toy2");
		List<Image> testingData = dataSetLoader.loadClassImages(Type.Localization, "mouse");
		classifier.train(trainingData);
		
		String classLabel = "mouse";
		Mat supportVectors = svmClassifier.getSupportVector(classLabel);
		
		int searchIndex = 1;
		for(Image sampleTestImage : testingData){
			System.out.println("starting search " + searchIndex);
			featureVectorGenerator.generateFeatureVector(sampleTestImage);
			Mat imageKeypoints = featureVectorGenerator.getKeypointsClusterIdxMat();
			QualityFunction qualityFunction = new SurfLinearSvmQualityFunction(supportVectors, imageKeypoints);
			BranchAndBoundObjectLocalizer localizer = new BranchAndBoundObjectLocalizer(qualityFunction);
			
			Rectangle objectBounds = localizer.getObjectBounds(sampleTestImage, classifier, classLabel);
			TestsHelper.drawRectangle(objectBounds, sampleTestImage);
			ImageLoader.saveImage(sampleTestImage, "jpg", new File("branchAndBound" + searchIndex + ".jpg"));
			++searchIndex;
		}
	}
}
