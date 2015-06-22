package grad.proj.utils;

import grad.proj.utils.imaging.Image;
import grad.proj.utils.imaging.ImageLoader;

import java.io.File;
import java.util.AbstractList;
import java.util.List;

public class FilesImageList extends AbstractList<Image>{
	
	private List<File> files;
	
	public FilesImageList(List<File> files) {
		this.files = files;
	}
	
	@Override
	public Image get(int index) {
		return ImageLoader.loadImage(files.get(index).getAbsolutePath());
	}

	@Override
	public int size() {
		return files.size();
	}

}
