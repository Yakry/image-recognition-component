package grad.proj.localization.impl;

import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.ObjectLocalizerTest;
import grad.proj.localization.impl.BranchAndBoundObjectLocalizer;
import grad.proj.localization.impl.SurfLinearSvmQualityFunction;

public class BranchAndBoundObjectLocalizerTest extends ObjectLocalizerTest {

	@Override
	public ObjectLocalizer getLocalizer() {
		return new BranchAndBoundObjectLocalizer(new SurfLinearSvmQualityFunction());
	}	
}
