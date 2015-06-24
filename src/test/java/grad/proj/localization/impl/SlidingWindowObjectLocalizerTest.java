package grad.proj.localization.impl;

import grad.proj.localization.ObjectLocalizer;
import grad.proj.localization.ObjectLocalizerTest;
import grad.proj.localization.impl.SlidingWindowObjectLocalizer;

public class SlidingWindowObjectLocalizerTest extends ObjectLocalizerTest {

	@Override
	public ObjectLocalizer createLocalizer() {
		return new SlidingWindowObjectLocalizer();
	}
}
