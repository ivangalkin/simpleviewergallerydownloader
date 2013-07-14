package com.google.code.simpleviewergallerydownloader.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		assertEquals(68, gallery.getImages().size());

		assertEquals("", gallery.getImagePath());
		assertEquals("00.jpg", gallery.getImages().get(0).getImageURL());
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
		assertEquals(23, gallery.getImages().size());

		assertEquals("", gallery.getImagePath());
		assertEquals("01Faultier.jpg", gallery.getImages().get(0).getImageURL());
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
		assertEquals(61, gallery.getImages().size());

		assertEquals("images/large/", gallery.getImagePath());
		assertEquals("_7423694043.jpg", gallery.getImages().get(0).getImageURL());
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
		assertEquals(7, gallery.getImages().size());

		assertEquals("slides/", gallery.getImagePath());
		assertEquals("722.jpg", gallery.getImages().get(0).getImageURL());
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
		assertEquals(53, gallery.getImages().size());

		assertEquals("slides/", gallery.getImagePath());
		assertEquals("Reinh_Vorne.jpg", gallery.getImages().get(0).getImageURL());
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
		assertEquals(39, gallery.getImages().size());

		assertEquals("", gallery.getImagePath());
		assertEquals("Nastassja Nass  _01.jpg", gallery.getImages().get(0)
				.getImageURL());
	}

	/**
	 * http://www.photo-art.asia/emily/gallery.xml
	 */

	@Test
	public void testXMLParse6() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery6.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(29, gallery.getImages().size());

		assertEquals("", gallery.getImagePath());
		assertEquals("images/20090703 SG Emily _MG_3331.jpg", gallery
				.getImages().get(0).getImageURL());
	}
	
	/**
	 * http://www.haus-schnede.de/hochzeitsgalerie/gallery.xml
	 */
	@Test
	public void testXMLParse7() throws DownloaderParseException {
		InputStream is = TestDownloaderXMLParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/google/code/simpleviewergallerydownloader/test/gallery7.xml");
		assertNotNull(is);
		SimpleviewerGallery gallery = new DownloaderXMLParser()
				.getSimpleviewerGallery(is);
		assertEquals(33, gallery.getImages().size());

		assertEquals("images/", gallery.getImagePath());
		assertEquals("images/luftballonaktion3.jpg", gallery
				.getImages().get(0).getImageURL());
	}
		

}
