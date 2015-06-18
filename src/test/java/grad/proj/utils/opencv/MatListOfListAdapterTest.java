package grad.proj.utils.opencv;


import static org.junit.Assert.*;
import grad.proj.recognition.RequiresLoadingTestBaseClass;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MatListOfListAdapterTest extends RequiresLoadingTestBaseClass{

	@Test
	public void testColMatAdapter() {
		Mat mat = Mat.eye(5, 5, CvType.CV_8UC1);
		for(int i=0; i<5; i++)
			for(int j=0; j<5; j++)
				mat.get(i, j)[0] = i+j;
		
		List<List<Double>> matAdapter = new MatListOfListAdapter(mat);

		assertEquals(mat.rows(), matAdapter.size());
		
		for(int i=0; i<matAdapter.size(); i++){
			List<Double> singleList = matAdapter.get(i);
			assertEquals(mat.cols(), singleList.size());
			
			for(int j=0; j<singleList.size(); j++){
				assertEquals(mat.get(i, j)[0], singleList.get(j) , 1e-15);
			}
		}
	}

}
