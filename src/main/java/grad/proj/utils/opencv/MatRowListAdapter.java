package grad.proj.utils.opencv;

import java.util.AbstractList;

import org.opencv.core.Mat;


/**
 * Represents a row in Mat as a List<Double>
 * @author Mohamed Kamal
 *
 */
class MatRowListAdapter extends AbstractList<Double> implements MatRepresentation{
	private int row;
	private Mat mat;

	public MatRowListAdapter(Mat mat){
		this.row = 0;
		this.mat = mat;
	}
	
	public MatRowListAdapter(Mat mat, int row){
		this.row = row;
		this.mat = mat;
	}

	@Override
	public Mat getMat(){
		return mat;
	}
	
	
	@Override
	public Double get(int index) {
		return mat.get(row, index)[0];
	}

	@Override
	public int size() {
		return mat.cols();
	}
	
}