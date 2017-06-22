package grad.proj.localization;

import grad.proj.classification.Classifier;
import grad.proj.database_manager.UserController;
import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;
import grad.proj.localization.impl.SurfLinearSvmQualityFunction;
import grad.proj.utils.DataSetLoader;
import grad.proj.utils.imaging.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ObjectsLocalizer {
    public static ObjectsLocalizer objectsLocalizer = new ObjectsLocalizer();
    private ObjectLocalizer localizer;
    private Classifier<Image> classifier;

    public ObjectsLocalizer(ObjectLocalizer localizer,
                            Classifier<Image> classifier) {
        this.localizer = localizer;
        this.classifier = classifier;
    }

    private ObjectsLocalizer() {
        this.localizer = new BranchAndBoundObjectLocalizer(new
                SurfLinearSvmQualityFunction());
        this.classifier = DataSetLoader.getDataSetLoader()
                .loadTrainedClassifier();
    }

    public static ObjectsLocalizer getObjectsLocalizer() {
        return objectsLocalizer;
    }

    public Classifier<Image> getClassifier() {
        return classifier;
    }

    public Map<String, Rectangle> getObjectsBounds(Image image) {
        Map<String, Rectangle> bounds = new HashMap<>();

        for (String classLabel : classifier.getClasses()) {
            if (!UserController.getConcernedItems().contains(classLabel))
                continue;
            Rectangle objectBounds = localizer.getObjectBounds(image,
                    classifier, classLabel);
            if (objectBounds != null)
                bounds.put(classLabel, objectBounds);
        }

        return bounds;
    }
}
