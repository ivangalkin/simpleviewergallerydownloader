package com.google.code.simpleviewergallerydownloader.test;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.google.code.simpleviewergallerydownloader.DownloaderXMLParser;
import com.google.code.simpleviewergallerydownloader.DownloaderXMLParser.DownloaderParseException;
import com.google.code.simpleviewergallerydownloader.schema.SimpleviewerGallery;

public class TestDownloaderXMLParser {
	@Test
	public void testXMLParse0() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery0.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(68, gallery.getImage().size());
	}
	
	@Test
	public void testXMLParse1() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery1.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(23, gallery.getImage().size());
	}
	
	@Test
	public void testXMLParse2() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery2.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(61, gallery.getImage().size());
	}
}
