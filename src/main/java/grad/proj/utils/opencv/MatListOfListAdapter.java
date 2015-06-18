package grad.proj.utils.opencv;

import java.util.AbstractList;
import java.util.List;

import org.opencv.core.Mat;

/**
 * Represents the Mat as a List<List<Double>>
 * @author Mohamed Kamal
 *
 */
public class MatListOfListAdapter extends AbstractList<List<Double>> implements MatRepresentation{

	private Mat mat;
	
	public MatListOfListAdapter(Mat mat){
		this.mat = mat;
	}
	
	@Override
	public Mat getMat(){
		return mat;
	}
	
	@Override
	public List<Double> get(int index) {
		return new MatRowListAdapter(mat, index);
	}

	@Override
	public int size() {
		return mat.rows();
	}
	

}
