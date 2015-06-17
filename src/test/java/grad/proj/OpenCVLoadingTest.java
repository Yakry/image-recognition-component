package grad.proj;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVLoadingTest {
	
	// Loads the native library of OpenCV, this code should be in the main class if exists
	static{ System.load(Paths.get(System.getenv("OPENCV3_HOME"), "build", "java", System.getProperty("os.arch").contains("64") ? "x64" : "x86", System.mapLibraryName(Core.NATIVE_LIBRARY_NAME)).toString()); }

	// Tests whether OpenCV was configured and loaded successfully or not
	// if this test fails, go back to the Readme for how to configure OpenCV on eclipse
	@Test
	public void testLoaded() {
		// Create an identity matrix
		Mat mat = Mat.eye(2, 2, CvType.CV_8UC1);
		assertEquals(1, mat.get(0, 0)[0], 1e-15);
		assertEquals(0, mat.get(0, 1)[0], 1e-15);
		assertEquals(0, mat.get(1, 0)[0], 1e-15);
		assertEquals(1, mat.get(1, 1)[0], 1e-15);
	}

}
