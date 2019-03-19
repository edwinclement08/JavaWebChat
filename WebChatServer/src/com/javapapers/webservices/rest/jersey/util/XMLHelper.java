package com.javapapers.webservices.rest.jersey.util;

public class XMLHelper {
	private XMLHelper _instance = null;

	private XMLHelper() {
	}

	public XMLHelper getInstance() {
		if (_instance == null)
			_instance = new XMLHelper();
		return _instance;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		// start of function

		XMLTag root = new XMLTag("html");
		XMLTag head = new XMLTag("head");
		XMLTag body = new XMLTag("body");
		XMLTag div = new XMLTag("div");
		XMLTag div2 = new XMLTag("div");

		root.setParameter("id", "contentPage");
		root.setParameter("class", "indexPage");

		div.setContent("Hello World");
		div2.setContent("Trial2");

		root.addChild(head);
		root.addChild(body);
		body.addChild(div);
		body.addChild(div2);

		String dump = root.toString();

		XMLTag result = XMLTag.fromString(dump);

		// end of function

		// ending time
		long end = System.currentTimeMillis();
		System.out.println(dump);
		System.out.println("xml->string->xml->string");
		System.out.println(result);
		System.out.println("\nDump and load and dump xml takes " + (end - start) + "ms");

	}

}
