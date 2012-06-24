package com.google.code.simpleviewergallerydownloader.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.code.simpleviewergallerydownloader.HttpDownloaderTransport;
import com.google.code.simpleviewergallerydownloader.IDownloaderTransport;
import com.google.code.simpleviewergallerydownloader.IDownloaderTransport.DownloaderTransportException;

public class TestHttpDownloaderTransport {

	@Test
	public void testDownloadOneFile() throws DownloaderTransportException,
			IOException {
		IDownloaderTransport httpTransport = new HttpDownloaderTransport(
				"test", "test");
		InputStream file = httpTransport.download(new URL(
				"http://browserspy.dk/password-ok.php"));

		assertNotNull(file);

		ByteArrayOutputStream content = new ByteArrayOutputStream();
		IOUtils.copy(file, content);

		assertTrue(content.size() > 0);
	}

	@Test
	public void testDownloadFileMultipleTimes()
			throws DownloaderTransportException, IOException {
		int T = 10;

		IDownloaderTransport httpTransport = new HttpDownloaderTransport(
				"test", "test");
		List<ByteArrayOutputStream> contentList = new ArrayList<ByteArrayOutputStream>(
				T);

		for (int i = 0; i < T; i++) {
			InputStream file = httpTransport.download(new URL(
					"http://browserspy.dk/password-ok.php"));

			assertNotNull(file);

			ByteArrayOutputStream content = new ByteArrayOutputStream();
			IOUtils.copy(file, content);
			assertTrue(content.size() > 0);
			contentList.add(content);
		}

		for (int i = 1; i < T; i++) {
			assertEquals(contentList.get(0).size(), contentList.get(i).size());
		}
	}

	@Test
	public void testDownloadFileMultipleTimesMultipleInstances()
			throws DownloaderTransportException, IOException {
		for (int i = 0; i < 10; i++) {
			testDownloadFileMultipleTimes();
		}
	}

}
