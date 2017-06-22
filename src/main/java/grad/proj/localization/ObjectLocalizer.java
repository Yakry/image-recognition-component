package grad.proj.localization;

import grad.proj.classification.Classifier;
import grad.proj.utils.imaging.Image;

import java.awt.*;

public interface ObjectLocalizer {
    double DISCARDING_ERROR_THRESHOLD = -1;

    Rectangle getObjectBounds(Image image, Classifier<Image> classifier, String classLabel);
}
