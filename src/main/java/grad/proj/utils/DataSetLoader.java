package grad.proj.utils;

import grad.proj.utils.DataSetsTestsHelper.Type;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DataSetLoader {
	private static String IMAGES_FOLDER_NAME = "images";
	
	// special class that holds test images that has more than one image, used with object localizer tests
	private static String COMBINED_CLASS = "combined";
	
	private File datasetFolder;
	
	public DataSetLoader(File datasetFolder) {
		this.datasetFolder = datasetFolder;
	}
	
	public List<Image> loadClassImages(Type type, String className){
		File classImagesFolder = getImagesClassFolder(type, className);
		
		if(!classImagesFolder.exists())
			return new ArrayList<>();
		
		File imageFiles[] = classImagesFolder.listFiles();
		List<File> images = new ArrayList<File>();
		for(File imageFile : imageFiles){
			images.add(imageFile);
		}
		
		return new FilesImageList(images);
	}
	
	public List<List<Image>> loadImages(DataSetsTestsHelper.Type type, String ...classes){
		List<List<Image>> data = new ArrayList<List<Image>>();
	
		File imagesMainFolder = getImagesFolder(type);
		
		if(classes.length == 0){
			classes = imagesMainFolder.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return (name != COMBINED_CLASS) && (new File(dir, name).isDirectory());
				}
			});
		}
		
		for(String className : classes){
			data.add(loadClassImages(type, className));
		}
		
		return data;
	}
	
	private File getImagesClassFolder(DataSetsTestsHelper.Type type,
			String className) {
		File imagesMainFolder = getImagesFolder(type);
		File classImagesFolder = new File(imagesMainFolder, className);
		return classImagesFolder;
	}
	
	private File getImagesFolder(DataSetsTestsHelper.Type type) {
		File imagesMainFolder = new File(datasetFolder, IMAGES_FOLDER_NAME);
		File typeFolder = new File(imagesMainFolder, type.toString());
		return typeFolder;
	}

}
