package grad.proj.utils;


class ImageImpl implements Image {
	int[][] imageArr;
	
	public ImageImpl(int[][] imageArr) {
		this.imageArr = imageArr;
	}
	@Override
	public int getPixelAt(int row, int col) {
		return imageArr[row][col];
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
