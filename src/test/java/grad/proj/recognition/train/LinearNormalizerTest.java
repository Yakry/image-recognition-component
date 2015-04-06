package grad.proj.recognition.train;

import static org.junit.Assert.assertTrue;
import grad.proj.utils.DataFileLoader;

import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;

public class LinearNormalizerTest {
	static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	
	@Test
	public void testNormalizer() {
		List<List<List<Double> > > scaledAllClassData = DataFileLoader.
				loadDataSeprated("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\splice_scale.txt");
		List<List<List<Double> > > unscaledAllClassData = DataFileLoader.
				loadDataSeprated("src\\test\\java\\grad\\proj\\"
						+ "recognition\\train\\splice.txt");
		
		for(int classLabel = 0; classLabel<scaledAllClassData.size();++classLabel){
			List<List<Double> > scaledfeatureVectors = scaledAllClassData.get(classLabel);
			List<List<Double> > unscaledfeatureVectors = unscaledAllClassData.get(classLabel);
			LinearNormalizer normalizer = new LinearNormalizer();
			List<List<Double> > myScaledfeatureVectors =
					normalizer.reset(unscaledfeatureVectors,-1.0,1.0);
			
			assertTrue("Normalized data has wrong size",
					scaledfeatureVectors.size() == 
					myScaledfeatureVectors.size() );
			
			for(int i=0;i<scaledfeatureVectors.size();++i){
				assertTrue("Input data has wrong size",
						scaledfeatureVectors.get(0).size() == 
						scaledfeatureVectors.get(i).size() );
				assertTrue("Normalized data has wrong feature vector size",
						scaledfeatureVectors.get(i).size() == 
						myScaledfeatureVectors.get(i).size() );
				
				for(int j=0;j<scaledfeatureVectors.get(i).size();++j)
					assertTrue("Normalized data has wrong values " +
							scaledfeatureVectors.get(i).get(j) + "  " +
							myScaledfeatureVectors.get(i).get(j),
							Math.abs( scaledfeatureVectors.get(i).get(j) - 
							myScaledfeatureVectors.get(i).get(j)) <= 1e-6);
			}
		}
	}
}
