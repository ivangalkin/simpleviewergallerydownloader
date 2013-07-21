package com.google.code.simpleviewergallerydownloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

	private static final String XPATH_ROOT_SIMPLEVIEWER = "/simpleviewergallery";
	private static final String XPATH_ROOT_AUTOVIEWER = "/gallery";
	private static final String XPATH_IMAGEPATH = "./@imagepath";
	private static final String XPATH_IMAGES = "./image";
	private static final String XPATH_FILENAME = "./filename/text()";
	private static final String XPATH_IMAGEURL = "./@imageurl";
	private static final String XPATH_URL = "./url/text()";

	private final List<XPathExpression> xPathRoots;
	private final XPathExpression xPathImages;
	private final XPathExpression xPathImagePath;
	private final List<XPathExpression> xPathURLs;

	private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private final XPathFactory xPathFactory = XPathFactory.newInstance();

	public DownloaderXMLParser() throws DownloaderParseException {
		final XPath xPath = xPathFactory.newXPath();

		try {
			xPathRoots = new ArrayList<XPathExpression>() {
				private static final long serialVersionUID = 5816806284340915249L;
				{
					// Simpleviewer
					add(xPath.compile(XPATH_ROOT_SIMPLEVIEWER));
					// Autoviewer
					add(xPath.compile(XPATH_ROOT_AUTOVIEWER));
				}
			};
			xPathImages = xPath.compile(XPATH_IMAGES);
			xPathImagePath = xPath.compile(XPATH_IMAGEPATH);
			xPathURLs = new ArrayList<XPathExpression>() {
				private static final long serialVersionUID = -7787794328029149578L;
				{
					// Simpleviewer Gallery version 1.x
					add(xPath.compile(XPATH_FILENAME));
					// Simpleviewer Gallery version 2.x
					add(xPath.compile(XPATH_IMAGEURL));
					// Autoviewer
					add(xPath.compile(XPATH_URL));
				}
			};
		} catch (XPathExpressionException e) {
			throw new DownloaderParseException(e);
		}
	}

	private Node getFirstValidEvaluation(Node node,
			List<XPathExpression> expressions) throws XPathExpressionException {
		Node evaluationResult = null;
		for (XPathExpression each : expressions) {
			evaluationResult = (Node) each.evaluate(node, XPathConstants.NODE);
			if (evaluationResult != null) {
				return evaluationResult;
			}
		}
		return null;
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
			SAXException, IOException, DownloaderParseException {
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

		Document doc = builder.parse(is);
		prepareDocument(doc, doc);

		// root node

		Node rootNode = getFirstValidEvaluation(doc, xPathRoots);
		if (rootNode == null) {
			throw new DownloaderParseException(
					"Unknown XML format: Root element has to be <simpleviewergallery> or <gallery> (case insensitive)!");
		}

		SimpleviewerGallery gallery = new SimpleviewerGallery();

		// image path

		Node imagePathNode = (Node) xPathImagePath.evaluate(rootNode,
				XPathConstants.NODE);
		String imagePath = (imagePathNode == null) ? "" : imagePathNode
				.getNodeValue();
		gallery.setImagePath(imagePath);

		// images

		NodeList imageNodes = (NodeList) xPathImages.evaluate(rootNode,
				XPathConstants.NODESET);

		if (imageNodes == null) {
			return gallery;
		}

		for (int i = 0; i < imageNodes.getLength(); i++) {
			Node currentImageNode = imageNodes.item(i);
			Node currentImageURLNode = getFirstValidEvaluation(
					currentImageNode, xPathURLs);

			if (currentImageURLNode != null) {
				Image image = new Image(currentImageURLNode.getNodeValue());
				gallery.addImage(image);
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
