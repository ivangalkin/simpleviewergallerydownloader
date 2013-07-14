package com.google.code.simpleviewergallerydownloader.schema;

/**
 * Currently only 'imageURL' property is essential for image download. However
 * in this class we let some place for logic extension.
 * 
 * @author ivangalkin
 * 
 */
public class Image {

	private String imageURL;

	public Image(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getImageURL() {
		return imageURL;
	}
}
