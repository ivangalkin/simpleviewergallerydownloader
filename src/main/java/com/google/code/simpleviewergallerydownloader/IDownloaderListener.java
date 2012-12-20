package com.google.code.simpleviewergallerydownloader;

/**
 * Listener for {@link Downloader}'s events
 * 
 * @author ivangalkin
 * 
 */
public interface IDownloaderListener {

	public void setTotalPicturesCount(int count);

	public void setNowDownloading(String image, int number);

	public void onDownloadError(String errorMessage);

	public void onDownloadInfo(String infoMessage);

	public void onDownloadFinished();
}
