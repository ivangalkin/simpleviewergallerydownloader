package org.simpleviewergallerydownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.simpleviewergallerydownloader.schema.Image;
import org.simpleviewergallerydownloader.schema.SimpleviewerGallery;
import org.xml.sax.SAXException;

/**
 * Download images from SimpleViewer Gallery
 * 
 * @see http://simpleviewer.net/simpleviewer/
 * 
 * @author ivangalkin
 * 
 */
public class Downloader {
	/**
	 * The default buffer size to use.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static final String SCHEMA_PACKAGE = "org.simpleviewergallerydownloader.schema";
	private static final String GALLERY_SCHEMA = "gallery.xsd";
	private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String SLASH = "/";

	private IDownloaderListener listener = null;

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

		try {
			SimpleviewerGallery gallery = getGalleryImages(xmlUrl);

			String urlPath = getGaleryPath(xmlUrl);

			String imagePath = getImagePath(gallery);

			List<Image> images = gallery.getImage();

			if (images == null || images.size() == 0) {
				listener.onDownloadingError("List of images seems to be empty");
				return;
			}

			listener.setTotalPicturesCount(images.size());

			for (int index = 0; index < images.size(); index++) {
				Image each = images.get(index);
				try {

					URL imageURL = getImageUrl(urlPath, imagePath, each);

					listener.setNowDownloading(each.getFilename(), index);

					downloadImage(imageURL, downloadDirectory);
				} catch (MalformedURLException e) {
					listener
							.onDownloadingError("Could not create a well-formed URL for image "
									+ each.getFilename());
				}
			}
		} catch (SAXException e) {
			listener.onDownloadingError("Unable to initialize XML parsing");
		} catch (JAXBException e) {
			listener.onDownloadingError("Unable to initialize XML parsing");
		}
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
	private URL getImageUrl(String urlPath, String imagePath, Image each)
			throws MalformedURLException {
		try {
			URL imagePathUrl = new URL(imagePath);
			// if imagePath is absolute
			return new URL(imagePathUrl.toString() + each.getFilename());
		} catch (MalformedURLException e) {
			// if imagePath is relative
			return new URL(urlPath + imagePath + each.getFilename());
		}
	}

	private void downloadImage(URL imageURL, File downloadDirectory) {
		InputStream imageInputSteam = null;
		try {
			imageInputSteam = imageURL.openStream();
		} catch (IOException e) {
			e.printStackTrace();
			listener.onDownloadingError("Unable to open stream "
					+ imageURL.toString());

			return;
		}

		FileOutputStream fileOutputStream = null;
		File urlFile = new File(imageURL.getFile());
		File outputFile = new File(downloadDirectory, urlFile.getName());
		try {
			fileOutputStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			listener.onDownloadingError("Unable to create output file "
					+ outputFile.getAbsolutePath());
			return;
		}

		try {
			copy(imageInputSteam, fileOutputStream);
		} catch (IOException e) {
			listener.onDownloadingError("Error during reading from " + imageURL
					+ " or writing to " + outputFile.getAbsolutePath());
		} finally {
			try {
				imageInputSteam.close();
				fileOutputStream.close();
			} catch (Exception e) {
			}
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
		if (!imagePath.endsWith(SLASH)) {
			imagePath = imagePath.concat(SLASH);
		}
		return imagePath;
	}

	/**
	 * Instantiate JAXB unmarshaller, parse XML and return an
	 * {@link SimpleviewerGallery} object
	 * 
	 * @param xmlUrl
	 * @return
	 * @throws SAXException
	 * @throws JAXBException
	 */
	private SimpleviewerGallery getGalleryImages(URL xmlUrl)
			throws SAXException, JAXBException {
		SchemaFactory sf = SchemaFactory.newInstance(XML_SCHEMA);
		URL schemaURL = Downloader.class.getResource(GALLERY_SCHEMA);
		Schema schema = sf.newSchema(schemaURL);
		JAXBContext jaxbContext = JAXBContext.newInstance(SCHEMA_PACKAGE);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		SimpleviewerGallery gallery = (SimpleviewerGallery) unmarshaller
				.unmarshal(xmlUrl);
		return gallery;
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

		if (!path.endsWith(SLASH)) {
			path = path.concat(SLASH);
		}

		return path;
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since 1.1
	 * 
	 * @see org.apache.wicket.util.io.IOUtils
	 */
	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
