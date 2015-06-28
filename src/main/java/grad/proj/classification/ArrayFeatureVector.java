package grad.proj.classification;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ArrayFeatureVector implements FeatureVector{

	private static final long serialVersionUID = 4085576664769318781L;
	
	private List<Double> elements;
	 
	 public ArrayFeatureVector(int size) {
		 elements = Arrays.asList(new Double[size]);
	}
	 
	public ArrayFeatureVector(Double ...elements) {
		this.elements = Arrays.asList(elements);
	}

	@Override
	public double get(int index) {
		return elements.get(index);
	}

	@Override
	public void set(int index, double value) {
		elements.set(index, value);
	}

	@Override
	public int size() {
		return elements.size();
	}
	
	@Override
	public Iterator<Double> iterator() {
		return elements.iterator();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj instanceof FeatureVector){
			FeatureVector obj2 = (FeatureVector)obj;
			
			if(size() != obj2.size())
				return false;
			
			for(int i=0; i<size(); i++){
				if(get(i) != obj2.get(i))
					return false;
			}
			
			return true;
		}
		
		return super.equals(obj);
	}
}
