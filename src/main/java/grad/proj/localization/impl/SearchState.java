package grad.proj.localization.impl;

import java.awt.Rectangle;

import grad.proj.utils.imaging.Image;


public class SearchState implements Comparable<SearchState> {
	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public Integer minCoordinate[], maxCoordinate[];
	public Double quality;
	
	public SearchState(){
		minCoordinate = new Integer[4];
		maxCoordinate = new Integer[4];
		quality = 0.0;
	}
	
	public SearchState(Image image){
		minCoordinate = new Integer[4];
		maxCoordinate = new Integer[4];
		quality = 0.0;
		
		for(int i=0;i<4;++i)
			minCoordinate[i] = 0;
		
		maxCoordinate[TOP] = image.getHeight() - 1;
		maxCoordinate[BOTTOM] = image.getHeight() - 1;
		maxCoordinate[LEFT] = image.getWidth() - 1;
		maxCoordinate[RIGHT] = image.getWidth() - 1;
	}
	
	public boolean hasSubStates(){
		for(int i=0;i<4;++i){
			if(minCoordinate[i] != maxCoordinate[i])
				return true;
		}
		
		return false;
	}
	
	public void split(SearchState subState1, SearchState subState2){
		Integer maxRangeIndex = 0, maxRange = 0;
		for(int i=0;i<4;++i){
			subState1.minCoordinate[i] = subState2.minCoordinate[i] = minCoordinate[i];
			subState1.maxCoordinate[i] = subState2.maxCoordinate[i] = maxCoordinate[i];
			if((maxCoordinate[i] - minCoordinate[i]) > maxRange){
				maxRange = maxCoordinate[i]-minCoordinate[i];
				maxRangeIndex = i;
			}
		}
		
		subState1.minCoordinate[maxRangeIndex] = minCoordinate[maxRangeIndex];
		subState1.maxCoordinate[maxRangeIndex] = minCoordinate[maxRangeIndex] + (maxRange/2);
		subState2.minCoordinate[maxRangeIndex] = maxCoordinate[maxRangeIndex] - (maxRange/2);
		subState2.maxCoordinate[maxRangeIndex] = maxCoordinate[maxRangeIndex];
		
		subState1.maxCoordinate[TOP] = Math.min(subState1.maxCoordinate[TOP],
				subState1.maxCoordinate[BOTTOM]);
		subState1.minCoordinate[BOTTOM] = Math.max(subState1.minCoordinate[TOP],
				subState1.minCoordinate[BOTTOM]);
		subState1.maxCoordinate[LEFT] = Math.min(subState1.maxCoordinate[LEFT],
				subState1.maxCoordinate[RIGHT]);
		subState1.minCoordinate[RIGHT] = Math.max(subState1.minCoordinate[LEFT],
				subState1.minCoordinate[RIGHT]);
		
		subState2.maxCoordinate[TOP] = Math.min(subState2.maxCoordinate[TOP],
				subState2.maxCoordinate[BOTTOM]);
		subState2.minCoordinate[BOTTOM] = Math.max(subState2.minCoordinate[TOP],
				subState2.minCoordinate[BOTTOM]);
		subState2.maxCoordinate[LEFT] = Math.min(subState2.maxCoordinate[LEFT],
				subState2.maxCoordinate[RIGHT]);
		subState2.minCoordinate[RIGHT] = Math.max(subState2.minCoordinate[LEFT],
				subState2.minCoordinate[RIGHT]);
	}

	public Rectangle getRectangle(){
		return new Rectangle(maxCoordinate[LEFT], maxCoordinate[TOP],
				maxCoordinate[RIGHT] - maxCoordinate[LEFT],
				maxCoordinate[BOTTOM] - maxCoordinate[TOP]);
	}
	
	@Override
	public int compareTo(SearchState o) {
		return this.quality.compareTo(o.quality);
	}
}
