package grad.proj.localization;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

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
	
	public Map<String, Rectangle> getObjectsBounds(Image image){
		Map<String, Rectangle> bounds = new HashMap<>();
		
		for(String classLabel : classifier.getClasses()){
			bounds.put(classLabel, localizer.getObjectBounds(image, classifier, classLabel));
		}
		
		return bounds;
	}
}
