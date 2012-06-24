package com.google.code.simpleviewergallerydownloader.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.code.simpleviewergallerydownloader.HttpDownloaderTransport;
import com.google.code.simpleviewergallerydownloader.IDownloaderTransport;
import com.google.code.simpleviewergallerydownloader.IDownloaderTransport.DownloaderTransportException;

public class TestHttpDownloaderTransport {

	@Test
	public void testDownload() throws DownloaderTransportException, IOException {
		IDownloaderTransport httpTransport = new HttpDownloaderTransport(
				"test", "test");
		InputStream file = httpTransport.download(new URL(
				"http://browserspy.dk/password-ok.php"));

		assertNotNull(file);

		ByteArrayOutputStream content = new ByteArrayOutputStream();
		IOUtils.copy(file, content);

		assertTrue(content.size() > 0);
	}

}
