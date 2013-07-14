package com.google.code.simpleviewergallerydownloader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.code.simpleviewergallerydownloader.schema.Image;
import com.google.code.simpleviewergallerydownloader.schema.SimpleviewerGallery;

public class DownloaderXMLParser {

	public static class DownloaderParseException extends Exception {
		private static final long serialVersionUID = 5079583038082821921L;

		public DownloaderParseException(Throwable e) {
			super(e);
		}

		public DownloaderParseException(String msg) {
			super(msg);
		}
	}

	private static final String XPATH_IMAGEPATH = "/simpleviewergallery/@imagepath";
	private static final String XPATH_IMAGES = "/simpleviewergallery/image";
	private static final String XPATH_FILENAME = "./filename/text()";
	private static final String XPATH_IMAGEURL = "./@imageurl";

	private final XPathExpression xPathImages;
	private final XPathExpression xPathImagePath;
	private final XPathExpression xPathFilename;
	private final XPathExpression xPathImageURL;

	private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private final XPathFactory xPathFactory = XPathFactory.newInstance();

	public DownloaderXMLParser() throws DownloaderParseException {
		XPath xPath = xPathFactory.newXPath();

		try {
			xPathImages = xPath.compile(XPATH_IMAGES);
			xPathImagePath = xPath.compile(XPATH_IMAGEPATH);
			xPathFilename = xPath.compile(XPATH_FILENAME);
			xPathImageURL = xPath.compile(XPATH_IMAGEURL);
		} catch (XPathExpressionException e) {
			throw new DownloaderParseException(e);
		}
	}

	/**
	 * Recursively convert all tags and attributes to lower case
	 */
	private void prepareDocument(Document doc, Node nodeInput) {
		if (nodeInput.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap namedNodeMap = nodeInput.getAttributes();
			for (int i = 0; i < namedNodeMap.getLength(); i++) {
				Node attribute = namedNodeMap.item(i);
				doc.renameNode(attribute, attribute.getNamespaceURI(),
						attribute.getNodeName().toLowerCase());
			}
			doc.renameNode(nodeInput, nodeInput.getNamespaceURI(), nodeInput
					.getNodeName().toLowerCase());
		}

		if (nodeInput.hasChildNodes()) {
			for (int child = 0; child < nodeInput.getChildNodes().getLength(); child++) {
				prepareDocument(doc, nodeInput.getChildNodes().item(child));
			}
		}
	}

	public SimpleviewerGallery parse(InputStream is) throws DOMException,
			XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

		Document doc = builder.parse(is);
		prepareDocument(doc, doc);

		SimpleviewerGallery gallery = new SimpleviewerGallery();

		// image path

		Node imagePathNode = (Node) xPathImagePath.evaluate(doc,
				XPathConstants.NODE);
		String imagePath = (imagePathNode == null) ? "" : imagePathNode
				.getNodeValue();
		gallery.setImagePath(imagePath);

		// images

		NodeList imageNodes = (NodeList) xPathImages.evaluate(doc,
				XPathConstants.NODESET);

		if (imageNodes == null) {
			return gallery;
		}

		for (int i = 0; i < imageNodes.getLength(); i++) {
			Node currentResult = imageNodes.item(i);

			Node imageURLNode = (Node) xPathImageURL.evaluate(currentResult,
					XPathConstants.NODE);
			Node fileNameNode = (Node) xPathFilename.evaluate(currentResult,
					XPathConstants.NODE);

			if (imageURLNode != null) {
				// Simpleviewer Gallery version 1.x
				Image image = new Image(imageURLNode.getNodeValue());
				gallery.addImage(image);
			} else if (fileNameNode != null) {
				// Simpleviewer Gallery version 2.x
				Image image = new Image(fileNameNode.getNodeValue());
				gallery.addImage(image);
			} else {
				// Unknown version
				continue;
			}
		}

		return gallery;
	}

	public SimpleviewerGallery getSimpleviewerGallery(InputStream is)
			throws DownloaderParseException {
		SimpleviewerGallery gallery = null;
		try {
			gallery = this.parse(is);
		} catch (Exception e) {
			throw new DownloaderParseException(e);
		}

		if (gallery.getImages().isEmpty()) {
			throw new DownloaderParseException(
					"Description XML is empty or has an unknown format. "
							+ "Please create a new issue ticket at http://code.google.com/p/simpleviewergallerydownloader/issues/list");
		}

		return gallery;
	}

}
