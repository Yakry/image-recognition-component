package grad.proj.localization.impl;

import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.ObjectLocalizerTest;
import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;

public abstract class BranchAndBoundObjectLocalizerTest extends ObjectLocalizerTest {

	@Override
	public ObjectLocalizer createLocalizer() {
		return new BranchAndBoundObjectLocalizer(createQualityFunction());
	}

	public abstract QualityFunction createQualityFunction();
}
