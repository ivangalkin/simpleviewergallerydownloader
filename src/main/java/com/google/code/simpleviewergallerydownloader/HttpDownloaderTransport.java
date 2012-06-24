package com.google.code.simpleviewergallerydownloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class HttpDownloaderTransport implements IDownloaderTransport {

	private DefaultHttpClient httpClient;
	private HttpContext localContext;

	public HttpDownloaderTransport(String username, String password) {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));

		ClientConnectionManager cm = new BasicClientConnectionManager(
				schemeRegistry);
		httpClient = new DefaultHttpClient(cm);
		httpClient
				.getParams()
				.setParameter(CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20120403211507 Firefox/12.0");
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(username, password));

		localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE,
				new BasicCookieStore());
	}

	@Override
	public InputStream download(URL url) throws DownloaderTransportException {
		try {
			HttpGet getRequest = new HttpGet(url.toURI());

			HttpResponse response = httpClient
					.execute(getRequest, localContext);
			return response.getEntity().getContent();
		} catch (ClientProtocolException cpException) {
			throw new DownloaderTransportException(cpException);
		} catch (IOException ioException) {
			throw new DownloaderTransportException(ioException);
		} catch (IllegalStateException isException) {
			throw new DownloaderTransportException(isException);
		} catch (URISyntaxException urisException) {
			throw new DownloaderTransportException(urisException);
		}
	}
}
