package grad.proj.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;

public class MeanSquareErrorMatcher implements Matcher<List<Double>> {

	@Override
	public List<Integer> match(List<Double> instance, List<List<Double>> instances, int topN) {
		if(topN > instances.size()){
			throw new RuntimeException("topN can't be more than size of instances");
		}

		final List<Double> _instance = instance;
		final List<List<Double>> _instances = instances;
		
		Comparator<Integer> acendingComparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer index1, Integer index2) {
				List<Double> vector1 = _instances.get(index1);
				List<Double> vector2 = _instances.get(index2);
				double error1 = calculareMeanSquareError(_instance, vector1);
				double error2 = calculareMeanSquareError(_instance, vector2);
				double res = error1 - error2;
				if(res > 0) return 1;
				if(res < 0) return -1;
				return 0;
			}
		};
		
		Comparator<Integer> decendingComparator = acendingComparator.reversed();
		
		SortedSet<Integer> a;
		
		PriorityQueue<Integer> topNSoFar = new PriorityQueue<>(topN, decendingComparator);
		
		for(int i=0; i<instances.size(); i++){
			if(topNSoFar.size() < topN){
				topNSoFar.add(i);
			}
			else{
				Integer maxErrorSoFar = topNSoFar.peek();
				if(acendingComparator.compare(i, maxErrorSoFar) < 0){
					topNSoFar.poll();
					topNSoFar.add(i);
				}
			}
		}
		
		Integer[] result = new Integer[topN];
		int i = topN - 1;
		while(!topNSoFar.isEmpty()){
			result[i--] = topNSoFar.poll();
		}
		
		return Arrays.asList(result);
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
