package grad.proj.classification.impl;

import grad.proj.classification.Classifier;
import grad.proj.classification.FeatureVector;
import grad.proj.classification.FeatureVectorClassifierTest;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;

public class SVMClassifierTest  extends FeatureVectorClassifierTest{
	
	public Classifier<FeatureVector> createClassifier(){
		return new SVMClassifier(new LinearNormalizer());
	}
}
