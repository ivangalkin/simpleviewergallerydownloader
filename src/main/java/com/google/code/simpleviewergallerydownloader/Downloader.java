package com.google.code.simpleviewergallerydownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.code.simpleviewergallerydownloader.DownloaderXMLParser.DownloaderParseException;
import com.google.code.simpleviewergallerydownloader.IDownloaderTransport.DownloaderTransportException;
import com.google.code.simpleviewergallerydownloader.schema.Image;
import com.google.code.simpleviewergallerydownloader.schema.SimpleviewerGallery;

/**
 * Download images from SimpleViewer Gallery
 * 
 * @see http://simpleviewer.net/simpleviewer/
 * 
 * @author ivangalkin
 * 
 */
public class Downloader {

	private IDownloaderListener listener = null;

	private IDownloaderTransport transport = null;

	/**
	 * Create {@link Downloader} object and start parsing and downloading
	 * immediately
	 * 
	 * @param xmlUrl
	 * @param downloadDirectory
	 * @param listener
	 */
	public Downloader(URL xmlUrl, File downloadDirectory,
			boolean enumeratePictures, IDownloaderListener listener) {
		this(xmlUrl, downloadDirectory, null, null, enumeratePictures, listener);
	}

	public Downloader(URL xmlUrl, File downloadDirectory, String username,
			String password, boolean enumeratePictures,
			IDownloaderListener listener) {

		if (xmlUrl == null || downloadDirectory == null || listener == null) {
			if (listener != null) {
				listener.onDownloadFinished();
			}
			throw new IllegalArgumentException(
					"Wrong initialization of Downloader object");
		}

		this.listener = listener;
		this.transport = (username == null || password == null) ? new SimpleDownloaderTransport()
				: new HttpDownloaderTransport(username, password);

		InputStream xmlContent = null;
		try {
			xmlContent = transport.download(xmlUrl);
		} catch (DownloaderTransportException e) {
			this.listener.onDownloadError("Unable to download XML file: "
					+ ExceptionUtils.getMessage(e));
			this.listener.onDownloadFinished();
			return;
		}
		SimpleviewerGallery gallery = null;
		try {
			gallery = new DownloaderXMLParser()
					.getSimpleviewerGallery(xmlContent);
		} catch (DownloaderParseException e1) {
			this.listener.onDownloadError("Unable to parse XML: "
					+ ExceptionUtils.getMessage(e1));
			this.listener.onDownloadFinished();
			return;
		}

		String urlPath = getGaleryPath(xmlUrl);
		String imagePath = gallery.getImagePath();
		List<Image> images = gallery.getImage();

		if (images == null || images.size() == 0) {
			this.listener.onDownloadError("List of images is empty");
			this.listener.onDownloadFinished();
			return;
		}

		listener.setTotalPicturesCount(images.size());
		String prefixTemplate = "%0" + String.valueOf(images.size()).length()
				+ "d_";

		for (int index = 0; index < images.size(); index++) {
			Image each = images.get(index);

			List<URL> imageURLs = getImageUrls(urlPath, imagePath, each);

			if (imageURLs.isEmpty()) {
				this.listener
						.onDownloadError("Could not create a well-formed URL for image "
								+ each.getFilename());
				continue;
			}

			listener.setNowDownloading(each.getFilename(), index);

			String filenameStorePrefix = enumeratePictures ? String.format(
					prefixTemplate, index + 1) : "";

			downloadImage(each.getFilename(), filenameStorePrefix, imageURLs,
					downloadDirectory);
		}
		this.listener.onDownloadFinished();
	}

	/**
	 * Check if argument imagePath relative or absolute is. If relative build
	 * image URL from base link
	 * 
	 * @param urlPath
	 * @param imagePath
	 * @param each
	 * @return
	 * @throws MalformedURLException
	 */
	private List<URL> getImageUrls(String urlPath, String imagePath, Image image) {

		List<URL> imageUrls = new LinkedList<URL>();
		if (imagePath == null || imagePath.isEmpty()) {
			try {
				// 1. image path is not given, filename is relative to urlPath
				URL url = DownloaderUtils.getURL(urlPath, image.getFilename());
				imageUrls.add(url);
			} catch (MalformedURLException e) {
			}

			try {
				// 2. image path is not given, filename is relative to default
				// forlder "images"
				URL url = DownloaderUtils.getURL(urlPath, "images", image
						.getFilename());
				imageUrls.add(url);
			} catch (MalformedURLException e) {
			}
		} else {
			try {
				// 3. image path is given, it is relative
				URL url = DownloaderUtils.getURL(urlPath, imagePath, image
						.getFilename());
				imageUrls.add(url);
			} catch (MalformedURLException e) {
			}

			try {
				// 4. image path is given, it is absolute
				URL url = DownloaderUtils
						.getURL(imagePath, image.getFilename());
				imageUrls.add(url);
			} catch (MalformedURLException e) {
			}
		}

		return imageUrls;
	}

	private void downloadImage(String filename, String filenameStorePrefix,
			List<URL> imageURLs, File downloadDirectory) {
		InputStream imageInputSteam = null;
		String properURL = null;

		for (URL each : imageURLs) {
			try {
				imageInputSteam = transport.download(each);
				properURL = each.toString();
				break;
			} catch (DownloaderTransportException e) {
			}
		}

		if (properURL != null) {
			this.listener.onDownloadInfo("File was found under " + properURL);
		} else {
			this.listener
					.onDownloadError("Could not build an appropriated URL for file "
							+ filename + " or network error");
			return;
		}

		FileOutputStream fileOutputStream = null;
		File outputFile = new File(downloadDirectory, filenameStorePrefix
				+ filename);
		try {
			fileOutputStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			this.listener.onDownloadError("Unable to create output file "
					+ outputFile.getAbsolutePath() + ": "
					+ ExceptionUtils.getMessage(e));
			IOUtils.closeQuietly(fileOutputStream);
			return;
		}

		try {
			IOUtils.copy(imageInputSteam, fileOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
			this.listener.onDownloadError("Error during reading from "
					+ properURL + " or writing to "
					+ outputFile.getAbsolutePath() + ": "
					+ ExceptionUtils.getMessage(e));
			IOUtils.closeQuietly(imageInputSteam);
			IOUtils.closeQuietly(fileOutputStream);
		}
	}

	/**
	 * Get path to the gallery e.g. extract
	 * <code>http://fotos.acme.eu/heide/</code> from
	 * <code>http://fotos.acme.eu/heide/gallery.xml</code>
	 * 
	 * @param xmlUrl
	 * @return
	 */
	protected String getGaleryPath(URL xmlUrl) {
		File file = new File(xmlUrl.getFile());

		String urlRepresentation = xmlUrl.toString();
		int filePosition = urlRepresentation.lastIndexOf(file.getName());

		String path = urlRepresentation.substring(0, filePosition);

		return path;
	}
}
