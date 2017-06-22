package grad.proj.localization.impl;

import grad.proj.classification.Classifier;
import grad.proj.localization.ObjectLocalizer;
import grad.proj.utils.imaging.Image;

import java.awt.*;
import java.util.PriorityQueue;

public class BranchAndBoundObjectLocalizer implements ObjectLocalizer {

	private QualityFunction qualityFunction;
	
	public BranchAndBoundObjectLocalizer(QualityFunction qualityFunction) {
		this.qualityFunction = qualityFunction;
	}

	@Override
	public Rectangle getObjectBounds(Image image, Classifier<Image> classifier, String classLabel) {
		int counter = qualityFunction.getMaxIterations();
		
		Object preprocessedInfo = qualityFunction.preprocess(image, classifier, classLabel);
		
		PriorityQueue<SearchState> searchQueue = new PriorityQueue<SearchState>();
		
		SearchState startState = new SearchState(image);
		startState.quality = qualityFunction.evaluate(startState, image, classifier, classLabel, preprocessedInfo);
		
		searchQueue.add(startState);
		while(searchQueue.peek().hasSubStates() && (counter > 0)){
			SearchState subState1 = new SearchState();
			SearchState subState2 = new SearchState();
			searchQueue.poll().split(subState1, subState2);
			subState1.quality = qualityFunction.evaluate(subState1, image, classifier, classLabel, preprocessedInfo);
			subState2.quality = qualityFunction.evaluate(subState2, image, classifier, classLabel, preprocessedInfo);
			searchQueue.add(subState1);
			searchQueue.add(subState2);
			--counter;
		}
		
		SearchState target = searchQueue.peek();
        System.out.println(classLabel + " with quality: " + target.quality + "\nRect: " + target
                .getRectangle());
        if (target.quality > DISCARDING_ERROR_THRESHOLD)
            return null;
        return target.getRectangle();
	}
}
