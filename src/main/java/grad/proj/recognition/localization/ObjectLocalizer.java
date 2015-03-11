package grad.proj.recognition.localization;

import grad.proj.Image;
import grad.proj.recognition.train.ObjectRecognizer;

public interface ObjectLocalizer {

	public Image getObjectImage(Image image, ObjectRecognizer recognizer);
}
