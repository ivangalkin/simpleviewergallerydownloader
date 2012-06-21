package com.google.code.simpleviewergallerydownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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

	private static final String SLASH = "/";

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
			IDownloaderListener listener) {

		if (xmlUrl == null || downloadDirectory == null || listener == null) {
			throw new IllegalArgumentException(
					"Wrong initialization of Downloader object");
		}

		this.listener = listener;
		this.transport = new SimpleDownloaderTransport();

		InputStream xmlContent = null;
		try {
			xmlContent = transport.download(xmlUrl);
		} catch (DownloaderTransportException e) {
			this.listener.onDownloadingError("Unable to download XML file");
			return;
		}
		SimpleviewerGallery gallery = null;
		try {
			gallery = new DownloaderXMLParser()
					.getSimpleviewerGallery(xmlContent);
		} catch (DownloaderParseException e1) {
			listener.onDownloadingError("Unable to parse XML");
			return;
		}

		String urlPath = getGaleryPath(xmlUrl);
		String imagePath = getImagePath(gallery);
		List<Image> images = gallery.getImage();

		if (images == null || images.size() == 0) {
			listener.onDownloadingError("List of images is empty");
			return;
		}

		listener.setTotalPicturesCount(images.size());

		for (int index = 0; index < images.size(); index++) {
			Image each = images.get(index);

			List<URL> imageURLs = getImageUrls(urlPath, imagePath, each);

			if (imageURLs.isEmpty()) {
				listener
						.onDownloadingError("Could not create a well-formed URL for image "
								+ each.getFilename());
				continue;
			}

			listener.setNowDownloading(each.getFilename(), index);

			downloadImage(each.getFilename(), imageURLs, downloadDirectory);
		}
	}

	/**
	 * 
	 * @param parts
	 *            parts, which we build an URL from
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 */
	private URL joinURL(String... parts) throws MalformedURLException,
			UnsupportedEncodingException, URISyntaxException {
		List<String> partsList = new LinkedList<String>();
		for (int i = 0; i < parts.length; i++) {
			String part = StringUtils.removeEnd(parts[i], SLASH);
			// assume the first part is a valid URL
			// partsList.add((i == 0) ? part : URLEncoder.encode(part,
			// "UTF-8"));
			partsList.add(part);
		}
		String joined = StringUtils.join(partsList, SLASH);

		String decodedURL = URLDecoder.decode(joined, "UTF-8");
		URL url = new URL(decodedURL);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
				url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		System.out.println(uri.toASCIIString());
		return uri.toURL();

		// System.out.println(joined);
		// return new URL(joined);
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
				URL url = joinURL(urlPath, image.getFilename());
				imageUrls.add(url);
			} catch (Exception e) {
			}

			try {
				// 2. image path is not given, filename is relative to default
				// forlder "images"
				URL url = joinURL(urlPath, "images", image.getFilename());
				imageUrls.add(url);
			} catch (Exception e) {
			}
		} else {
			try {
				// 3. image path is given, it is relative
				URL url = joinURL(urlPath, imagePath, image.getFilename());
				imageUrls.add(url);
			} catch (Exception e) {
			}

			try {
				// 4. image path is given, it is absolute
				URL url = joinURL(imagePath, image.getFilename());
				imageUrls.add(url);
			} catch (Exception e) {
			}
		}

		return imageUrls;
	}

	private void downloadImage(String filename, List<URL> imageURLs,
			File downloadDirectory) {
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
			this.listener.onDownloadingError("File was found under "
					+ properURL);
		} else {
			this.listener
					.onDownloadingError("Could not build an appropriated URL for file "
							+ filename + " or network error");
			return;
		}

		FileOutputStream fileOutputStream = null;
		File outputFile = new File(downloadDirectory, filename);
		try {
			fileOutputStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			this.listener.onDownloadingError("Unable to create output file "
					+ outputFile.getAbsolutePath());
			IOUtils.closeQuietly(fileOutputStream);
			return;
		}

		try {
			IOUtils.copy(imageInputSteam, fileOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
			this.listener.onDownloadingError("Error during reading from "
					+ properURL + " or writing to "
					+ outputFile.getAbsolutePath());
			IOUtils.closeQuietly(imageInputSteam);
			IOUtils.closeQuietly(fileOutputStream);
		}
	}

	/**
	 * Extract path to images directory
	 * 
	 * @param gallery
	 * @return
	 */
	private String getImagePath(SimpleviewerGallery gallery) {
		String imagePath = gallery.getImagePath();
		// if (!imagePath.isEmpty() && !imagePath.endsWith(SLASH)) {
		// imagePath = imagePath.concat(SLASH);
		// }
		return imagePath;
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

		// if (!path.endsWith(SLASH)) {
		// path = path.concat(SLASH);
		// }

		return path;
	}
}
