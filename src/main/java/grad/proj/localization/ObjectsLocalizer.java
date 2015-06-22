package grad.proj.localization;

import java.awt.Rectangle;

import grad.proj.recognition.ImageClassifier;
import grad.proj.utils.imaging.Image;

public class ObjectsLocalizer {
	private ObjectLocalizer localizer;
	private ImageClassifier classifier;
	
	public ObjectsLocalizer(ObjectLocalizer localizer,
			ImageClassifier classifier) {
		this.localizer = localizer;
		this.classifier = classifier;
	}
	
	public Rectangle[] getObjectsBounds(Image image){
		Rectangle[] bounds = new Rectangle[classifier.getClassesNo()];
		
		for(int i=0; i<classifier.getClassesNo(); i++){
			bounds[i] = localizer.getObjectBounds(image, classifier, i);
		}
		
		return bounds;
	}
}
