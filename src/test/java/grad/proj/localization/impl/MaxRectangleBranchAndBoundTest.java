package grad.proj.localization.impl;

public class MaxRectangleBranchAndBoundTest extends BranchAndBoundObjectLocalizerTest {

	@Override
	public QualityFunction createQualityFunction() {
		return new MaxRectangleQualityFunction();
	}

}
