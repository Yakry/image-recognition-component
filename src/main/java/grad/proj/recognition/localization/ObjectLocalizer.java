package grad.proj.recognition.localization;

import grad.proj.recognition.train.ObjectRecognizer;
import grad.proj.utils.Image;

public interface ObjectLocalizer {

	public Image getObjectImage(Image image, ObjectRecognizer recognizer);
}
