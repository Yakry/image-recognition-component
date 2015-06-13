package grad.proj.recognition.train;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import grad.proj.Image;
import grad.proj.utils.ImageLoader;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.junit.Test;
import org.opencv.core.Core;

public class ObjectRecognizerTest {

	private static final String IMG1_BIG = "SURF_IMG_1_BIG.jpg";
	private static final String IMG1_SMALL = "SURF_IMG_1_SMALL.jpg";

	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	@Test
	public void testTrainingOnOneImageAndRecognizeIt() throws Exception {
	ObjectRecognizer recognizer = new ObjectRecognizer(new MultiClassSVMClassifier(),
														new SurfFeatureVectorGenerator());
		
		final URL IMG1_BIG_URL = getClass().getResource(IMG1_BIG);
		
		assertNotNull(IMG1_BIG_URL);

		Image img1Big = ImageLoader.loadImage(new File(IMG1_BIG_URL.toURI()));
		
		recognizer.trainToRecognize(Arrays.asList(img1Big));
		
		System.out.println(recognizer.recognize(img1Big));
		assertTrue(recognizer.recognize(img1Big) > 0.99);
	}

}
