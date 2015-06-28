package grad.proj.matching;

import java.util.Collection;
import java.util.List;

public interface Matcher<T> {
	public<K extends T> List<K> match(T instance, Collection<K> instances, int topN);
}
