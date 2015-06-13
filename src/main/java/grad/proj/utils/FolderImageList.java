package grad.proj.utils;

import grad.proj.Image;

import java.io.File;
import java.util.AbstractList;

public class FolderImageList extends AbstractList<Image>{
	
	private File[] files;
	
	public FolderImageList(File folder) {
		this.files = folder.listFiles();
	}
	
	@Override
	public Image get(int index) {
		return ImageLoader.loadImage(files[index]);
	}

	@Override
	public int size() {
		return files.length;
	}

}
