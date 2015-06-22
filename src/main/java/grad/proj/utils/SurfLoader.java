package grad.proj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;

public class SurfLoader {

	public static void saveSurf(SurfFeatureVectorGenerator generator, File filename){
		Mat vocab = generator.getVocab();
		saveMat(vocab, filename);
	}
	
	public static SurfFeatureVectorGenerator loadSurf(File filename){
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		Mat vocab = loadMat(filename);
		generator.setVocab(vocab);
		return generator;
	}

	private static void saveMat(Mat vocab, File filename) {
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
	
	private static Mat loadMat(File filename) {
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
