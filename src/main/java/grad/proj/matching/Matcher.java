package grad.proj.matching;

import java.util.Collection;
import java.util.List;

public interface Matcher<T> {
	public List<T> match(T instance, Collection<T> instances, int topN);
}
