package grad.proj.recognition.localization;

import grad.proj.recognition.train.ImageClassifier;
import grad.proj.utils.Image;
import grad.proj.utils.MatConverters;
import grad.proj.utils.SubImage;

import java.awt.Rectangle;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SlidingWindowObjectLocalizer implements ObjectLocalizer {
	private static final int IMAGE_DIM = 500;
	private static final int WINDOW_DIM = 400;

	@Override
	public Rectangle getObjectBounds(Image image, ImageClassifier classifier, int classLabel) {
		int bestX = 0, bestY = 0;
		double bestDistance = Double.MAX_VALUE;
		for(int x = 0; x+WINDOW_DIM < image.getWidth() ; x+=10){
			for(int y = 0; y+WINDOW_DIM < image.getHeight() ; y+=10){
				SubImage subimage = new SubImage(image, x, y, WINDOW_DIM, WINDOW_DIM);
				double distance = classifier.classify(subimage, classLabel);
				//System.out.println(distance);
				if(distance < bestDistance){
					bestX = x;
					bestY = y;
					bestDistance = distance;
				}
			}
		}
		int rectX = bestX;
		int rectY = bestY;
		int rectWidth = WINDOW_DIM;
		int rectHeight = WINDOW_DIM;
		
		/*
		int bestX = 0, bestY = 0;
		double bestDistance = Double.MAX_VALUE;
		Image resizedImage = this.resizeImage(image);

		for(int x = 0; x+WINDOW_DIM < IMAGE_DIM ; x+=10){
			for(int y = 0; y+WINDOW_DIM < IMAGE_DIM ; y+=10){
				SubImage subimage = new SubImage(resizedImage, x, y, WINDOW_DIM, WINDOW_DIM);
				double distance = classifier.classify(subimage, classLabel);
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
		*/
		return new Rectangle(rectX,rectY,rectWidth,rectHeight);
	}

	private Image resizeImage(Image image){
		Mat inputImageMat = MatConverters.ImageToMat(image);
		Mat resizedImageMat = new Mat(IMAGE_DIM, IMAGE_DIM, CvType.CV_8UC3);
		Size scaledImageSize = new Size(IMAGE_DIM, IMAGE_DIM);
		Imgproc.resize(inputImageMat, resizedImageMat, scaledImageSize);
		return MatConverters.MatToImage(resizedImageMat);
	}

}
