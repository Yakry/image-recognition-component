package grad.proj.utils;


public class ImageImpl implements Image {
	int[][] imageArr;

	public ImageImpl(int rows, int cols){
		imageArr = new int[rows][cols];
	}

	public ImageImpl(int[][] imageArr) {
		this.imageArr = imageArr;
	}

	@Override
	public int getPixelAt(int row, int col) {
		return imageArr[row][col];
	}

	@Override
	public void setPixelAt(int row, int col, int rgb) {
		imageArr[row][col] = rgb;
	}

	@Override
	public int getWidth() {
		return imageArr[0].length;
	}

	@Override
	public int getHeight() {
		return imageArr.length;
	}

}
