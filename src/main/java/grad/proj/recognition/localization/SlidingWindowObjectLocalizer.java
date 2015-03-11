package grad.proj.recognition.localization;

import grad.proj.Image;
import grad.proj.recognition.train.ObjectRecognizer;
import grad.proj.utils.SubImage;

public class SlidingWindowObjectLocalizer implements ObjectLocalizer{

	private static final double WINDOW_WIDTH_RATIO = 0.2;
	private static final double WINDOW_HEIGHT_RATIO = 0.2;
	
	
	@Override
	public Image getObjectImage(Image image, ObjectRecognizer recognizer) {
		/*
		 * The idea is simple:
		 * 1. determine the window size based on the ratios and the image size
		 * 2. loop over the image rows and cols
		 * 		create an image of each window using the current x, y, width, height
		 * 		input the image to the recognizer with the recognize and see the error ratio
		 * 		determmine the best image that gives min error and return it 
		 */
		
		return null;
	}
	
	private static Image getImageOf(Image image, int x, int y, int width, int height){
		return new SubImage(image, x, y, width, height);
	}
}
