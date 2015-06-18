package grad.proj.utils;


public class SubImage implements Image {
	
	private Image originalImage;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	public SubImage(Image originalImage, int x, int y, int width, int height) {
		this.originalImage = originalImage;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getPixelAt(int row, int col) {
		if(row >= height  || col >= width ){
			throw new RuntimeException("Access out of sub image bounds");
		}
		
		return originalImage.getPixelAt(x + row, y + col);
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
