package grad.proj.utils.opencv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MatLoader {

	public static void saveMat(Mat vocab, File filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(vocab.rows() + " " + vocab.cols() + "\n");
			for(int i=0; i<vocab.rows(); i++){
				for(int j=0; j<vocab.cols(); j++){
					writer.write(vocab.get(i, j)[0] + " ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static Mat loadMat(File filename) {
		Mat mat = null;
		try {
			FileInputStream fileInput = new FileInputStream(filename);
			
			@SuppressWarnings("resource")
			Scanner input = new Scanner(fileInput);
	
			int rows = input.nextInt();
			int cols = input.nextInt();
			mat = new Mat(rows, cols, CvType.CV_32F);
			for(int i=0; i<rows; i++){
				for(int j=0; j<cols; j++){
					mat.put(i, j, input.nextDouble());
				}
			}
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return mat;
	}

}
