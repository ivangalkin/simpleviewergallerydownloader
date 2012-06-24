package com.google.code.simpleviewergallerydownloader.test;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import org.junit.Test;

import com.google.code.simpleviewergallerydownloader.DownloaderUtils;

public class TestDownloaderUtils {
	private static final String J_URL_PATH = "http://www.acme.com/path/filename.jpg";
	private static final String J_URL = "http://www.acme.com/filename.jpg";

	@Test
	public void testJoinURL() {
		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com", "",
				"filename.jpg"));

		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com", "/",
				"filename.jpg"));

		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com",
				"filename.jpg"));

		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com/",
				"filename.jpg"));

		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com",
				"/filename.jpg"));

		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com/",
				"/filename.jpg"));

		assertEquals(J_URL, DownloaderUtils.joinURL("http://www.acme.com/",
				"////filename.jpg"));

	}

	@Test
	public void testJoinURLPath() {
		assertEquals(J_URL_PATH, DownloaderUtils.joinURL("http://www.acme.com",
				"path", "filename.jpg"));

		assertEquals(J_URL_PATH, DownloaderUtils.joinURL("http://www.acme.com",
				"path/", "filename.jpg"));

		assertEquals(J_URL_PATH, DownloaderUtils.joinURL("http://www.acme.com",
				"/path/", "filename.jpg"));

		assertEquals(J_URL_PATH, DownloaderUtils.joinURL(
				"http://www.acme.com/", "path/", "filename.jpg"));

		assertEquals(J_URL_PATH, DownloaderUtils.joinURL("http://www.acme.com",
				"path/", "/filename.jpg"));

		assertEquals(J_URL_PATH, DownloaderUtils.joinURL(
				"http://www.acme.com/", "path", "/filename.jpg"));

		assertEquals(J_URL_PATH, DownloaderUtils.joinURL(
				"http://www.acme.com/", "/path/", "////filename.jpg"));

	}

	@Test
	public void testGetURL() throws MalformedURLException {
		assertEquals("http://www.acme.com/p%20ath/file%20name.jpg",
				DownloaderUtils.getURL(
						"http://www.acme.com/p ath/file name.jpg").toString());
	}
}
