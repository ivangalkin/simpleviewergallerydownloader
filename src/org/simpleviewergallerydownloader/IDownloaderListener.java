package org.simpleviewergallerydownloader;

/**
 * Listener for {@link Downloader}'s events
 * 
 * @author ivangalkin
 * 
 */
public interface IDownloaderListener {

	public void setTotalPicturesCount(int count);

	public void setNowDownloading(String image, int number);

	public void onDownloadingError(String errorMessage);

}
