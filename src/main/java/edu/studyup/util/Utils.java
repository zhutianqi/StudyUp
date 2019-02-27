package edu.studyup.util;

public class Utils {
	public static String getBaseURL(String url) {
		if (url.startsWith("http://")) url = url.substring(7);
		else if (url.startsWith("https://")) url = url.substring(8);
		if (url.contains("/")) url = url.substring(0, url.indexOf("/"));
		if (url.contains(":")) url = url.substring(0, url.indexOf(":"));
		return url;
	}
}
