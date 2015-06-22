package grad.proj.utils.opencv;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class LoaderTest extends RequiresLoadingTestBaseClass{

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
