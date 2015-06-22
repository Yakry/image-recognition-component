package grad.proj.localization.impl;

import grad.proj.localization.ObjectLocalizer;
import grad.proj.recognition.ImageClassifier;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.SubImage;
import grad.proj.utils.opencv.MatConverters;

import java.awt.Rectangle;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SlidingWindowObjectLocalizer implements ObjectLocalizer {
	private static final int MIN_IMAGE_DIM = 500;
	private static final int MAX_IMAGE_DIM = 1000;
	private static final int IMAGE_DIM_STEP = 250;
	private static final int WINDOW_DIM = 300;
	private static final int WINDOW_STEP = 10;

	@Override
	public Rectangle getObjectBounds(Image image, ImageClassifier classifier, int classLabel) {
		Rectangle globalBestBounds = new Rectangle();
		double globalBestScore = Double.MAX_VALUE;
		for(int imageDim = MIN_IMAGE_DIM; imageDim <= MAX_IMAGE_DIM; imageDim += IMAGE_DIM_STEP){
			Image scaledImage = this.scaleImage(image, imageDim, imageDim);
			int localBestBoundsX = 0, localBestBoundsY = 0;
			double localBestScore = Double.MAX_VALUE;
			for(int x = 0; x+WINDOW_DIM < imageDim; x += WINDOW_STEP){
				for(int y=0; y+WINDOW_DIM < imageDim; y += WINDOW_STEP){
					SubImage subimage = new SubImage(scaledImage, x, y, WINDOW_DIM, WINDOW_DIM);
					double score = classifier.classify(subimage, classLabel);
					if(score < localBestScore){
						localBestBoundsX = x;
						localBestBoundsY = y;
						localBestScore = score;
					}
				}
			}
			
			if(localBestScore <= globalBestScore){
				globalBestBounds.setBounds((localBestBoundsX*image.getWidth()) / imageDim,
						(localBestBoundsY*image.getHeight()) / imageDim,
						(WINDOW_DIM*image.getWidth()) / imageDim,
						(WINDOW_DIM*image.getHeight()) / imageDim);
				globalBestScore = localBestScore;
			}
		}
		
		return globalBestBounds;
	}

	private Image scaleImage(Image image, int width, int height){
		Mat inputImageMat = MatConverters.ImageToMat(image);
		Mat scaledImageMat = new Mat(height, width, CvType.CV_8UC3);
		Size scaledImageSize = new Size(width, height);
		Imgproc.resize(inputImageMat, scaledImageMat, scaledImageSize);
		return MatConverters.MatToImage(scaledImageMat);
	}

}
