package grad.proj.localization.impl;

public class SurfLinearSvmBranchAndBoundTest extends BranchAndBoundObjectLocalizerTest {

	@Override
	public QualityFunction createQualityFunction() {
		return new SurfLinearSvmQualityFunction();
	}

}
