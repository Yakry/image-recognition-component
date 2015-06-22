package grad.proj.recognition.localization;

import grad.proj.recognition.train.ImageClassifier;
import grad.proj.utils.Image;

import java.awt.Rectangle;
import java.util.PriorityQueue;

public class BranchAndBoundObjectLocalizer implements ObjectLocalizer {

	private QualityFunction qualityFunction;
	
	public BranchAndBoundObjectLocalizer(QualityFunction qualityFunction) {
		this.qualityFunction = qualityFunction;
	}

	@Override
	public Rectangle getObjectBounds(Image image, ImageClassifier classifier,int classLabel) {
		int counter = 10000;
		PriorityQueue<SearchState> searchQueue = new PriorityQueue<SearchState>();
		SearchState startState = new SearchState(image);
		startState.quality = qualityFunction.evaluate(image, startState, classifier, classLabel);
		searchQueue.add(startState);
		while(searchQueue.peek().hasSubStates() && (counter > 0)){
			SearchState subState1 = new SearchState();
			SearchState subState2 = new SearchState();
			searchQueue.poll().split(subState1, subState2);
			subState1.quality = qualityFunction.evaluate(image, subState1, classifier, classLabel);
			subState2.quality = qualityFunction.evaluate(image, subState2, classifier, classLabel);
			searchQueue.add(subState1);
			searchQueue.add(subState2);
			--counter;
		}
		
		SearchState target = searchQueue.peek();
		return target.getRectangle();
	}
}
