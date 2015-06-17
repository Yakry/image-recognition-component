package grad.proj.recognition.train;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVLoadingTest {

	// Loads the native library of OpenCV, this code should be in the main class if exists
	static{ System.load(Paths.get(System.getenv("OPENCV3_HOME"), "build", "java", System.getProperty("os.arch").contains("64") ? "x64" : "x86", System.mapLibraryName(Core.NATIVE_LIBRARY_NAME)).toString()); }

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
