package com.google.code.simpleviewergallerydownloader.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.math.BigInteger;

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

		assertEquals("", gallery.getImagePath());
		assertEquals("00.jpg", gallery.getImage().get(0).getCaption());
		assertEquals("00.jpg", gallery.getImage().get(0).getFilename());
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

		assertEquals("", gallery.getImagePath());
		assertEquals("01Faultier.jpg", gallery.getImage().get(0).getCaption());
		assertEquals("01Faultier.jpg", gallery.getImage().get(0).getFilename());
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

		assertEquals("images/large/", gallery.getImagePath());
		assertEquals("", gallery.getImage().get(0).getCaption());
		assertEquals("_7423694043.jpg", gallery.getImage().get(0).getFilename());
	}

	/**
	 * http://www.gittischneider.at/bilder/bilder.xml
	 */
	@Test
	public void testXMLParse3() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery3.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(7, gallery.getImage().size());

		assertEquals("slides/", gallery.getImagePath());
		assertEquals("", gallery.getImage().get(0).getCaption());
		assertEquals("722.jpg", gallery.getImage().get(0).getFilename());
	}

	/**
	 * http://new.fotobild-thueringen.de/Reinhardsbrunn/gallery.xml
	 */
	@Test
	public void testXMLParse4() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery4.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(53, gallery.getImage().size());

		assertEquals("slides/", gallery.getImagePath());
		assertEquals(
				"<font size=\"10px\"><br /></font><font color=\"#ffffff\" face=\"Tahoma, Arial, Helvetica, sans-serif\" size=\"10px\">30.08.09</font><font size=\"10px\"><br /><br /></font><font color=\"#ffffff\" face=\"Tahoma, Arial, Helvetica, sans-serif\" size=\"12px\">Image 1 Of 53</font>",
				gallery.getImage().get(0).getCaption());
		assertEquals("Reinh_Vorne.jpg", gallery.getImage().get(0).getFilename());
	}

	/**
	 * http://www.nastassjanass.com/gallery/gallery.xml
	 */
	@Test
	public void testXMLParse5() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery5.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(39, gallery.getImage().size());
		assertEquals("PORTRAIT GALLERY", gallery.getTitle());
		assertEquals(BigInteger.valueOf(480), gallery.getMaxImageHeight());

		assertEquals("", gallery.getImagePath());
		assertEquals("Nastassja Nass  _01.jpg", gallery.getImage().get(0)
				.getCaption());
		assertEquals("Nastassja Nass  _01.jpg", gallery.getImage().get(0)
				.getFilename());
	}

}
