package grad.proj.recognition.train;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import grad.proj.recognition.RequiresLoadingTestBaseClass;
import grad.proj.recognition.train.impl.LinearNormalizer;
import grad.proj.recognition.train.impl.SVMClassifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;
import grad.proj.utils.ImageLoader;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;
import org.opencv.core.Core;

public class ObjectRecognizerTest extends RequiresLoadingTestBaseClass {

	private static final String IMG1_BIG = "SURF_IMG_1_BIG.jpg";
	private static final String IMG1_SMALL = "SURF_IMG_1_SMALL.jpg";

	@Test
	public void testTrainingOnOneImageAndRecognizeIt() throws Exception {
	ObjectRecognizer recognizer = new ObjectRecognizer(new SVMClassifier(new LinearNormalizer()),
														new SurfFeatureVectorGenerator());
		
		final URL IMG1_BIG_URL = getClass().getResource(IMG1_BIG);
		
		assertNotNull(IMG1_BIG_URL);

		Image img1Big = ImageLoader.loadImage(new File(IMG1_BIG_URL.toURI()));
		
		recognizer.trainToRecognize(Arrays.asList(img1Big));
		
		System.out.println(recognizer.recognize(img1Big));
		assertTrue(recognizer.recognize(img1Big) > 0.99);
	}

}
