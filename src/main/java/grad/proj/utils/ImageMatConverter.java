package grad.proj.utils;

import java.awt.Color;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImageMatConverter {

	public static Mat ImageToMat(Image image) {
		/*
		 *
		 * For more about CvType: http://docs.opencv.org/modules/core/doc/basic_structures.html
		 * basically CV_ 8 UC 3 means a tuple of 3 elements, each element is an unsigned char
		 */
		Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		for(int row=0; row<image.getHeight(); row++){
			for(int col=0; col<image.getWidth(); col++){
				Color c = new Color(image.getPixelAt(row,  col));
				byte[] data = new byte[3];
				data[0] = (byte)c.getRed();
				data[1] = (byte)c.getGreen();
				data[2] = (byte)c.getBlue();

				imageMat.put(row, col, data);
			}
		}
		return imageMat;
	}

	public static Image MatToImage(Mat mat){
		Image image = new ImageImpl(mat.rows(), mat.cols());
		for(int r = 0; r < mat.rows() ; ++r){
			for(int c = 0; c < mat.cols() ; ++c){
				Color color = new Color((int)mat.get(r, c)[0],
						(int)mat.get(r, c)[1],
						(int)mat.get(r, c)[2]);
				image.setPixelAt(r, c, color.getRGB());
			}
		}

		return image;
	}
}
