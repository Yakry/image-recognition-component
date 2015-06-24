package grad.proj.classification.impl;

import grad.proj.classification.FeatureVectorClassifier;
import grad.proj.classification.FeatureVectorClassifierTest;
import grad.proj.classification.impl.LinearNormalizer;
import grad.proj.classification.impl.SVMClassifier;

public class SVMClassifierTest  extends FeatureVectorClassifierTest{
	
	public FeatureVectorClassifier createClassifier(){
		return new SVMClassifier(new LinearNormalizer());
	}
}
