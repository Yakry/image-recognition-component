package grad.proj.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MeanSquareErrorMatcher implements Matcher<List<Double>> {

	@Override
	public <K extends List<Double>> List<K> match(List<Double> instance, Collection<K> instances, int topN)
	{
		final List<Double> _instance = instance;
		
		Comparator<K> acendingComparator = new Comparator<K>() {
			@Override
			public int compare(K vector1, K vector2) {
				double error1 = calculareMeanSquareError(_instance, vector1);
				double error2 = calculareMeanSquareError(_instance, vector2);
				double res = error1 - error2;
				if(res > 0) return 1;
				if(res < 0) return -1;
				return 0;
			}
		};
		
		Comparator<K> decendingComparator = acendingComparator.reversed();
		
		PriorityQueue<K> topNSoFar = new PriorityQueue<>(topN, decendingComparator);
		
		for(K vector : instances){
			if(topNSoFar.size() < topN){
				topNSoFar.add(vector);
			}
			else{
				K maxErrorSoFar = topNSoFar.peek();
				if(acendingComparator.compare(vector, maxErrorSoFar) < 0){
					topNSoFar.poll();
					topNSoFar.add(vector);
				}
			}
		}
		
		topN = Math.min(topN, topNSoFar.size());

		List<K> result = new ArrayList<K>(topN+1);
		for(int i=0; i<topN; i++){
			result.add(null);
		}
		
		int i = topN - 1;
		while(!topNSoFar.isEmpty()){
			result.set(i--, topNSoFar.poll());
		}
		
		return result;
	}
	
	private double calculareMeanSquareError(List<Double> A, List<Double> B){
		if(A.size() != B.size()){
			throw new RuntimeException("The feature vectors size does not match");
		}
		
		double error = 0;
		for(int i=0; i<A.size(); i++){
			double diff = A.get(i) - B.get(i);
			error += diff * diff;
		}
		
		return error;
	}

}
