package grad.proj.recognition.train.impl;

import java.util.ArrayList;

import org.opencv.core.Mat;

class BOWTrainer {
	protected ArrayList<Mat> descriptors = new ArrayList<Mat>();
	protected int size;
	
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

}