package grad.proj.utils;

public class SubImage implements Image {
	
	private Image originalImage;
	
	private int row;
	private int col;
	private int width;
	private int height;
	
	public SubImage(Image originalImage, int col, int row, int width, int height) {
		this.originalImage = originalImage;
		this.col = col;
		this.row = row;
		this.width = width;
		this.height = height;
		
		if( row < 0 ||
		    col < 0 || 
		   (row + height) > originalImage.getHeight() ||
		   (col + width) > originalImage.getWidth())
				throw new RuntimeException("the sub image bounds are out of the original image bounds");
	}

	@Override
	public int getPixelAt(int row, int col) {
		checkBounds(row, col);
		
		return originalImage.getPixelAt(this.row + row, this.col + col);
	}

	@Override
	public void setPixelAt(int row, int col, int rgb) {
		checkBounds(row, col);

		originalImage.setPixelAt(this.row + row, this.col + col, rgb);
	}

	
	private void checkBounds(int row, int col) {
		if(row < 0 || col < 0 || row >= height  || col >= width ){
			throw new IndexOutOfBoundsException("Access out of sub image bounds");
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
