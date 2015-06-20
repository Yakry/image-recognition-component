package grad.proj.utils;

public interface Image {
	int getPixelAt(int row, int col);
	void setPixelAt(int row, int col, int rgb);

	int getWidth();
	int getHeight();
}
