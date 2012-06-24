package com.google.code.simpleviewergallerydownloader;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.xml.sax.SAXException;

import com.google.code.simpleviewergallerydownloader.schema.SimpleviewerGallery;

public class DownloaderXMLParser {

	public static class DownloaderParseException extends Exception {
		private static final long serialVersionUID = 5079583038082821921L;

		public DownloaderParseException(Throwable e) {
			super(e);
		}
	}

	private static final String SCHEMA_PACKAGE = "com.google.code.simpleviewergallerydownloader.schema";

	// private static final String GALLERY_SCHEMA = "gallery.xsd";
	// private static final String XML_SCHEMA =
	// "http://www.w3.org/2001/XMLSchema";

	/**
	 * Instantiate JAXB unmarshaller, parse XML and return an
	 * {@link SimpleviewerGallery} object
	 * 
	 * @param xmlUrl
	 * @return
	 * @throws SAXException
	 * @throws JAXBException
	 */
	public SimpleviewerGallery getSimpleviewerGallery(InputStream xmlContent)
			throws DownloaderParseException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SCHEMA_PACKAGE);

			XMLInputFactory xif = XMLInputFactory.newInstance();
			XMLStreamReader xsr = xif.createXMLStreamReader(xmlContent);
			xsr = new MyStreamReaderDelegate(xsr);

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// SchemaFactory sf = SchemaFactory.newInstance(XML_SCHEMA);
			// URL schemaURL = Downloader.class.getResource(GALLERY_SCHEMA);
			// Schema schema = sf.newSchema(schemaURL);
			// unmarshaller.setSchema(schema);

			SimpleviewerGallery gallery = (SimpleviewerGallery) unmarshaller
					.unmarshal(xsr);
			return gallery;
		} catch (JAXBException e) {
			throw new DownloaderParseException(e);
		} catch (XMLStreamException e) {
			throw new DownloaderParseException(e);
		}
	}

	/**
	 * Additional robustness against case sensitive tags and attributes
	 * 
	 * @author ivangalkin
	 * @see {@link http
	 *      ://blog.bdoughan.com/2010/12/case-insensitive-unmarshalling.html}
	 */
	private static class MyStreamReaderDelegate extends StreamReaderDelegate {
		public MyStreamReaderDelegate(XMLStreamReader xsr) {
			super(xsr);
		}

		@Override
		public String getAttributeLocalName(int index) {
			return super.getAttributeLocalName(index).toLowerCase();
		}

		@Override
		public String getLocalName() {
			return super.getLocalName().toLowerCase();
		}

	}
}
