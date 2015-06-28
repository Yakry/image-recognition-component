package grad.proj.localization;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import grad.proj.classification.Classifier;
import grad.proj.utils.imaging.Image;

public class ObjectsLocalizer {
	private ObjectLocalizer localizer;
	private Classifier<Image> classifier;
	
	public ObjectsLocalizer(ObjectLocalizer localizer,
			Classifier<Image> classifier) {
		this.localizer = localizer;
		this.classifier = classifier;
	}
	
	public Map<String, Rectangle> getObjectsBounds(Image image){
		Map<String, Rectangle> bounds = new HashMap<>();
		
		for(String classLabel : classifier.getClasses()){
			Rectangle objectBounds = localizer.getObjectBounds(image, classifier, classLabel);
			if(objectBounds != null)
				bounds.put(classLabel, objectBounds);
		}
		
		return bounds;
	}
}
