package grad.proj.recognition.localization;

import grad.proj.recognition.train.Classifier;
import grad.proj.recognition.train.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.Image;
import grad.proj.utils.ImageMatConverter;
import grad.proj.utils.SubImage;

import java.awt.Rectangle;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SlidingWindowLocalizer {
	private static final int IMAGE_DIM = 500;
	private static final int WINDOW_DIM = 100;

	public Rectangle getObjectBounds(Image image, Classifier classifier,
			SurfFeatureVectorGenerator featureVectorGenerator, int classLabel) {
		int bestX = 0, bestY = 0;
		double bestDistance = Double.MAX_VALUE;
		Image resizedImage = this.resizeImage(image);

		for(int x = 0; x+WINDOW_DIM < IMAGE_DIM ; ++x ){
			for(int y = 0; y+WINDOW_DIM < IMAGE_DIM ; ++y){
				SubImage subimage = new SubImage(resizedImage, x, y, WINDOW_DIM, WINDOW_DIM);
				Mat featureVector = featureVectorGenerator.generateFeatureVector(subimage);
				System.out.println(featureVector.rows() + "  " + featureVector.cols());
				double distance = classifier.classify(featureVector, classLabel);
				if(distance < bestDistance){
					bestX = x;
					bestY = y;
					bestDistance = distance;
				}
			}
		}

		int rectX = (bestX*image.getWidth()) / IMAGE_DIM;
		int rectY = (bestY*image.getHeight()) / IMAGE_DIM;
		int rectWidth = (WINDOW_DIM*image.getWidth()) / IMAGE_DIM;
		int rectHeight = (WINDOW_DIM*image.getHeight()) / IMAGE_DIM;
		return new Rectangle(rectX,rectY,rectWidth,rectHeight);
	}

	private Image resizeImage(Image image){
		Mat inputImageMat = ImageMatConverter.ImageToMat(image);
		Mat resizedImageMat = new Mat(IMAGE_DIM, IMAGE_DIM, CvType.CV_8UC3);
		Size scaledImageSize = new Size(IMAGE_DIM, IMAGE_DIM);
		Imgproc.resize(inputImageMat, resizedImageMat, scaledImageSize);
		return ImageMatConverter.MatToImage(resizedImageMat);
	}
}
