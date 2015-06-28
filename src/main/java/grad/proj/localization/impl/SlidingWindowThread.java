package grad.proj.localization.impl;

import grad.proj.classification.Classifier;
import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.SubImage;

import java.awt.Rectangle;

public class SlidingWindowThread extends Thread {
	private Image image;
	private Classifier<Image> classifier;
	private String classLabel;
	
	private int windowDim;
	private int windowStep;
	
	private double bestError;
	private Rectangle bestBounds;

	public SlidingWindowThread(Image image, Classifier<Image> classifier, String classLabel, int windowDim, int windowStep) {
		this.image = image;
		this.classifier = classifier;
		this.classLabel = classLabel;
		
		this.windowDim = windowDim;
		this.windowStep = windowStep;
		bestError = Double.MAX_VALUE;
	}
	
	@Override
	public void run() {
		int bestBoundsX = 0, bestBoundsY = 0;
		bestError = Double.MAX_VALUE;
		for(int x = 0; x+windowDim < image.getWidth(); x += windowStep){
			for(int y=0; y+windowDim < image.getHeight(); y += windowStep){
				SubImage subimage = new SubImage(image, x, y, windowDim, windowDim);
				double error = classifier.classify(subimage, classLabel);
				if(error < bestError){
					bestBoundsX = x;
					bestBoundsY = y;
					bestError = error;
				}
			}
		}
		
		bestBounds = new Rectangle(bestBoundsX,bestBoundsY,windowDim,windowDim);
	}
	
	public double getBestError(){
		return bestError;
	}
	
	public Rectangle getBestBounds(){
		return bestBounds;
	}
}
