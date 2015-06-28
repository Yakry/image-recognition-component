package grad.proj.localization.impl;

import grad.proj.classification.Classifier;
import grad.proj.localization.ObjectLocalizer;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.opencv.MatConverters;

import java.awt.Rectangle;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SlidingWindowObjectLocalizer implements ObjectLocalizer {
	private static final int MIN_IMAGE_DIM = 300;
	private static final int MAX_IMAGE_DIM = 600;
	private static final int IMAGE_DIM_STEP = 100;
	private static final int WINDOW_DIM = 200;
	private static final int WINDOW_STEP = 10;

	@Override
	public Rectangle getObjectBounds(Image image, Classifier<Image> classifier, String classLabel) {
		Rectangle globalBestBounds = new Rectangle();
		double globalBestError = Double.MAX_VALUE;
		SlidingWindowThread slidingWindowThreads[] =
				new SlidingWindowThread[((MAX_IMAGE_DIM - MIN_IMAGE_DIM) / IMAGE_DIM_STEP) + 1];
		
		for(int imageDim = MAX_IMAGE_DIM, i = 0; imageDim >= MIN_IMAGE_DIM; imageDim -= IMAGE_DIM_STEP, ++i){
			Image scaledImage = this.scaleImage(image, imageDim, imageDim);
			slidingWindowThreads[i] = new SlidingWindowThread(scaledImage, classifier,
					classLabel, WINDOW_DIM, WINDOW_STEP);
			slidingWindowThreads[i].start();
		}
		
		for(int imageDim = MAX_IMAGE_DIM, i = 0; imageDim >= MIN_IMAGE_DIM; imageDim -= IMAGE_DIM_STEP, ++i){
			try{
				slidingWindowThreads[i].join();
			} catch(InterruptedException e){
				throw new RuntimeException(e.getMessage());
			}
			
			if(slidingWindowThreads[i].getBestError() <= globalBestError){
				int x = (int) slidingWindowThreads[i].getBestBounds().getX();
				int y = (int) slidingWindowThreads[i].getBestBounds().getY();
				int width = (int) slidingWindowThreads[i].getBestBounds().getWidth();
				int height = (int) slidingWindowThreads[i].getBestBounds().getHeight();
				
				globalBestBounds.setBounds(
						(x*image.getWidth()) / imageDim,
						(y*image.getHeight()) / imageDim,
						(width*image.getWidth()) / imageDim,
						(height*image.getHeight()) / imageDim);
				globalBestError = slidingWindowThreads[i].getBestError();
			}
		}
		
//		for(int imageDim = MIN_IMAGE_DIM; imageDim <= MAX_IMAGE_DIM; imageDim += IMAGE_DIM_STEP){
//			Image scaledImage = this.scaleImage(image, imageDim, imageDim);
//			int localBestBoundsX = 0, localBestBoundsY = 0;
//			double localBestError = Double.MAX_VALUE;
//			for(int x = 0; x+WINDOW_DIM < imageDim; x += WINDOW_STEP){
//				for(int y=0; y+WINDOW_DIM < imageDim; y += WINDOW_STEP){
//					SubImage subimage = new SubImage(scaledImage, x, y, WINDOW_DIM, WINDOW_DIM);
//					double score = classifier.classify(subimage, classLabel);
//					if(score < localBestScore){
//						localBestBoundsX = x;
//						localBestBoundsY = y;
//						localBestScore = score;
//					}
//				}
//			}
//			
//			if(localBestError <= globalBestError){
//				globalBestBounds.setBounds((localBestBoundsX*image.getWidth()) / imageDim,
//						(localBestBoundsY*image.getHeight()) / imageDim,
//						(WINDOW_DIM*image.getWidth()) / imageDim,
//						(WINDOW_DIM*image.getHeight()) / imageDim);
//				globalBestError = localBestError;
//			}
//		}
		
		if(globalBestError > DISCARDING_ERROR_THRESHOLD)
			return null;
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
