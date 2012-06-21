package com.google.code.simpleviewergallerydownloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SimpleDownloaderTransport implements IDownloaderTransport {

	@Override
	public InputStream download(URL url) throws DownloaderTransportException {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new DownloaderTransportException(e);
		}
	}

}
