package com.google.code.simpleviewergallerydownloader;

import java.io.InputStream;
import java.net.URL;

public interface IDownloaderTransport {
	public static class DownloaderTransportException extends Exception {
		private static final long serialVersionUID = -5586539769635793976L;

		public DownloaderTransportException(Throwable e) {
			super(e);
		}

		public DownloaderTransportException(String msg) {
			super(msg);
		}
	}

	public InputStream download(URL url) throws DownloaderTransportException;
}
