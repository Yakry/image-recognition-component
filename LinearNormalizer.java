package grad;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Collections;

public class LinearNormalizer implements Normalizer {
	public List<Double> Max ;
    public List<Double> Min ;

@Override
public List<List<Double>> reset(List<List<Double>> featureVectors) {
// TODO Auto-generated method stub
	  double largest = 0.0;
      double smallest =0.0;
      double newnumber = 0.0;
      int maxofelements = 0;
      int counter = 0;
   
     for (int row = 0; row < featureVectors.size(); row++)//get max column if it is not square matrix
     { 
         counter = 0;
     for (int col = 0; col < featureVectors.get(row).size(); col++)
        {
             counter++;
              
             if (counter > maxofelements)
             {
                 maxofelements = counter;
             }
        }

     }
                      
     Max=new ArrayList<Double>(counter);//array of maximum elements in each column in the matrix
     Min=new ArrayList<Double>(counter);
     
     for (int col=0; col < counter; col++) //get the max of each column
           {       
              largest = Integer.MIN_VALUE;
                                   
          for (int row=0; row < featureVectors.size(); row++)   
           {           
                    
                   if (col >= featureVectors.get(row).size())
                      {
                          continue;
                      }
                                                           
                  newnumber = featureVectors.get(row).get(col);
                                                 
                 if (newnumber > largest)
                      {
                         largest = newnumber;
                      }
                                      
               }
       
               
             Max.add(largest);
             
              
           }
   /////////////////////////////////////////////////////////////////////
     for (int col=0; col < counter; col++) //get the min of each column
       {       
          smallest = Integer.MAX_VALUE;
                             
    for (int row=0; row < featureVectors.size(); row++)   
     {           
                
               if (col >= featureVectors.get(row).size())
                  {
                      continue;
                  }
                                                       
              newnumber = featureVectors.get(row).get(col);
                                           
           if (newnumber < smallest)
                  {
                     smallest = newnumber;
                  }
                                  
           }
   
           
         Min.add(smallest);
          
       }

     for (int i=0;i<featureVectors.size();i++)
			for (int j=0;j<featureVectors.get(i).size();j++)
		{
				
			featureVectors.get(i).set(j, (featureVectors.get(i).get(j)-Min.get(j))/(Max.get(j)-Min.get(j)));
	
					
		}

return featureVectors;
}

@Override
public List<Double> normalize(List<Double> featureVector) {
for (int i=0;i<featureVector.size();i++)
{

	featureVector.set(i, (featureVector.get(i)-Min.get(i))/(Max.get(i)-Min.get(i)));
			}
return featureVector;
}

}
