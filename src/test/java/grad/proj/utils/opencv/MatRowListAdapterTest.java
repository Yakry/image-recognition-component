package grad.proj.utils.opencv;


import static org.junit.Assert.*;
import grad.proj.recognition.RequiresLoadingTestBaseClass;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class MatRowListAdapterTest extends RequiresLoadingTestBaseClass{

	@Test
	public void testColMatAdapter() {
		Mat mat = Mat.eye(1, 5, CvType.CV_8UC1);
		for(int i=0; i<5; i++)
			mat.get(0, i)[0] = i;
		
		List<Double> adapter = new MatRowListAdapter(mat);
		
		assertEquals(mat.cols(), adapter.size());
		
		for(int i=0; i<adapter.size(); i++){
			assertEquals(mat.get(0, i)[0], adapter.get(i) , 1e-15);
		}
	}

}
