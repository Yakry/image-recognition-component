package grad.proj.recognition;

import java.io.File;
import java.nio.file.Paths;

import org.opencv.core.Core;

public class Loader {
	private static boolean loaded;
	
	public boolean isLoaded(){
		return loaded;
	}
	
	public static void load(){
		String openCvFolder = System.getenv("OPENCV3_HOME");
		if(openCvFolder == null){
			throw new RuntimeException("Couldn't get environment variable OPENCV3_HOME");
		}
		String archictureFolder = System.getProperty("os.arch").contains("64") ? "x64" : "x86";
		File nativeDllFullPath = Paths.get(openCvFolder, "build", "java", archictureFolder, System.mapLibraryName(Core.NATIVE_LIBRARY_NAME)).toFile();
		
		if(!nativeDllFullPath.exists()){
			throw new RuntimeException("Couldn't find opencv native library with path " + nativeDllFullPath);
		}
		
		System.load(nativeDllFullPath.getAbsolutePath());
		
		loaded = true;
	}
}
