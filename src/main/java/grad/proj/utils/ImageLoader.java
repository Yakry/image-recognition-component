package grad.proj.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static Image loadImage(String imagePath) {
		return loadImage(new File(imagePath));
	}
	
	public static Image loadImage(File imageFile) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] pixels = new int[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[y][x] = image.getRGB(x, y);
			}
		}

		return new ImageImpl(pixels);
	}

}
