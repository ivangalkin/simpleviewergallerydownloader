package com.google.code.simpleviewergallerydownloader.schema;

import java.util.ArrayList;
import java.util.List;

public class SimpleviewerGallery {

	private String imagePath;
	private List<Image> images;

	public SimpleviewerGallery() {
		imagePath = new String();
		images = new ArrayList<Image>();
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<Image> getImages() {
		return images;
	}

	public void addImage(Image image) {
		images.add(image);
	}
}
