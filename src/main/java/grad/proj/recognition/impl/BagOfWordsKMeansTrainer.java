package grad.proj.recognition.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

/**
 * port of BOWKMeansTrainer of OpenCV
 *
 */
class BagOfWordsKMeansTrainer{
	
	protected ArrayList<Mat> descriptors = new ArrayList<Mat>();
	protected int size;
	
	protected int clusterCount;
	protected TermCriteria termcrit;
	protected int attempts;
	protected int flags;
	
	public BagOfWordsKMeansTrainer(int _clusterCount){
		this(_clusterCount, new TermCriteria(), 3, Core.KMEANS_PP_CENTERS);
	}
	
	public BagOfWordsKMeansTrainer(int _clusterCount, TermCriteria _termcrit, int _attempts, int _flags ){
		clusterCount = _clusterCount;
		termcrit = _termcrit;
		attempts = _attempts;
		flags = _flags;
	}
	
	public void add(Mat _descriptors)
	{
	    if( !descriptors.isEmpty() )
	    {
	        size += _descriptors.rows();
	    }
	    else
	    {
	        size = _descriptors.rows();
	    }
	
	    descriptors.add(_descriptors);
	}

	public ArrayList<Mat> getDescriptors()
	{
	    return descriptors;
	}

	int descripotorsCount()
	{
	    return descriptors.isEmpty() ? 0 : size;
	}

	void clear()
	{
	    descriptors.clear();
	}

	
	public Mat cluster()
	{
		int descCount = 0;
		for(int i = 0; i < descriptors.size(); i++ )
			descCount += descriptors.get(i).rows();
		
		Mat mergedDescriptors = new Mat(descCount, descriptors.get(0).cols(), descriptors.get(0).type() );
		
		for( int i = 0, start = 0; i < descriptors.size(); i++ )
		{
			Mat submut = mergedDescriptors.rowRange((int)start, (int)(start + descriptors.get(i).rows()));
			descriptors.get(i).copyTo(submut);
			start += descriptors.get(i).rows();
		}
		
		return this.cluster( mergedDescriptors );
	}

	public Mat cluster(Mat _descriptors)
	{
		Mat labels = new Mat(), vocabulary = new Mat();
		Core.kmeans( _descriptors, clusterCount, labels, termcrit, attempts, flags, vocabulary );
		return vocabulary;
	}

	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
	}
	
	public int getClusterCount() {
		return clusterCount;
	}
}
