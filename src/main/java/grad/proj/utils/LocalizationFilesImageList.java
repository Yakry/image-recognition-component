package grad.proj.utils;

import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.AbstractMap.SimpleEntry;

class LocalizationFilesImageList extends AbstractList<SimpleEntry<Image, Map<String, Rectangle>>>{
	private List<File> files;
	
	public LocalizationFilesImageList(List<File> files) {
		this.files = files;
	}

	@Override
	public SimpleEntry<Image, Map<String, Rectangle>> get(int index) {
		String imagePath = files.get(index).getAbsolutePath();
		String pPath = imagePath.substring(0, imagePath.lastIndexOf('.')) + ".txt"; 
		Image image = ImageLoader.loadImage(imagePath);
		Properties p = new Properties();
		InputStream in;
		Map<String, Rectangle> bounds = new HashMap<>();
		try {
			in = new FileInputStream(pPath);
			p.load(in);
			for(String key : p.stringPropertyNames()){
				String[] splitBounds = p.getProperty(key).split(",");
				bounds.put(key, new Rectangle(Integer.valueOf(splitBounds[0]),
											  Integer.valueOf(splitBounds[1]),
											  Integer.valueOf(splitBounds[2]),
											  Integer.valueOf(splitBounds[3])));
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SimpleEntry<Image, Map<String,Rectangle>>(image, bounds);
	}

	@Override
	public int size() {
		return files.size();
	}
}