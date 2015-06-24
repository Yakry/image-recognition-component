package grad.proj.utils;

import java.io.File;

import org.opencv.core.Mat;

import grad.proj.recognition.impl.SurfFeatureVectorGenerator;
import grad.proj.utils.opencv.MatLoader;

public class SurfLoader {

	public static void saveSurf(SurfFeatureVectorGenerator generator, File filename){
		Mat vocab = generator.getVocab();
		MatLoader.saveMat(vocab, filename);
	}
	
	public static SurfFeatureVectorGenerator loadSurf(File filename){
		SurfFeatureVectorGenerator generator = new SurfFeatureVectorGenerator();
		Mat vocab = MatLoader.loadMat(filename);
		generator.setVocab(vocab);
		return generator;
	}
}
