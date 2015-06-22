package grad.proj.utils;

import grad.proj.recognition.FeatureVectorGenerator;
import grad.proj.recognition.ImageClassifier;
import grad.proj.recognition.impl.LinearNormalizer;
import grad.proj.recognition.impl.SVMClassifier;
import grad.proj.recognition.impl.SurfFeatureVectorGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DataSetsTestsHelper {
	private static File DATASET_FOLDER = new File("datasets");
	
	public enum DataSet{
		calteckUniversity,
		satimage,
		svmguide4,
		vowel
	}
	
	public static DataSetLoader getDataSetLoader(DataSet dataset) {
		return new DataSetLoader(getDataSetFolder(dataset));
	}
	
	private static File getDataSetFolder(DataSet dataset) {
		return new File(DATASET_FOLDER, dataset.toString());
	}

	
}
