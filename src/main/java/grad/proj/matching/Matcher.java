package grad.proj.matching;

import java.util.List;

public interface Matcher<T> {
	public List<Integer> match(T instance, List<T> instances, int topN);
}
