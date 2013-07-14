package com.google.code.simpleviewergallerydownloader;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class DownloaderUtils {

	private static final String SLASH = "/";

	private DownloaderUtils() {
	}

	public static String joinURL(String... parts) {
		List<String> partsList = new LinkedList<String>();
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] == null || parts[i].isEmpty()) {
				continue;
			}
			String part = StringUtils.strip(parts[i], SLASH);
			if (!part.isEmpty()) {
				partsList.add(part);
			}
		}
		return StringUtils.join(partsList, SLASH);
	}

	public static URL getURL(String input) throws MalformedURLException {

		try {
			String decodedURL = URLDecoder.decode(input, "UTF-8");
			URL url = new URL(decodedURL);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(),
					url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());

			return uri.toURL();

		} catch (URISyntaxException uriException) {
			throw new MalformedURLException(
					ExceptionUtils.getMessage(uriException));
		} catch (UnsupportedEncodingException encodingException) {
			throw new MalformedURLException(
					ExceptionUtils.getMessage(encodingException));
		}
	}

	public static URL getURL(String... parts) throws MalformedURLException {
		return getURL(joinURL(parts));
	}

	public static void addURLIfWellformed(Collection<URL> collection,
			String... parts) {
		try {
			URL url = getURL(parts);
			collection.add(url);
		} catch (MalformedURLException e) {
		}
	}

}
