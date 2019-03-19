package com.javapapers.webservices.rest.jersey.test.modules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.javapapers.webservices.rest.jersey.util.XMLTag;

public class XMLTagTest {
	boolean GET_TIME = false;

	@Test
	public void testTagDump() {
		long start = System.currentTimeMillis();

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

		String expectedXMLString = "<html id='contentPage' class='indexPage'><head></head><body><div>Hello World</div><div>Trial2</div></body></html>";
		String actualXMLString = root.toString();

		long end = System.currentTimeMillis();
		if (GET_TIME)
			System.out.println("\nDump and load and dump xml takes " + (end - start) + "ms");

		assertEquals(expectedXMLString, actualXMLString);
	}

	@Test
	public void testTagLoad() {
		long start = System.currentTimeMillis();

		String XMLStringDump = "<html id='contentPage' class='indexPage'><head></head><body><div>Hello World</div><div>Trial2</div></body></html>";

		XMLTag expectedXMLTag = new XMLTag("html");
		XMLTag head = new XMLTag("head");
		XMLTag body = new XMLTag("body");
		XMLTag div = new XMLTag("div");
		XMLTag div2 = new XMLTag("div");

		expectedXMLTag.setParameter("id", "contentPage");
		expectedXMLTag.setParameter("class", "indexPage");

		div.setContent("Hello World");
		div2.setContent("Trial2");

		expectedXMLTag.addChild(head);
		expectedXMLTag.addChild(body);
		body.addChild(div);
		body.addChild(div2);

		XMLTag actualXMLTag = XMLTag.fromString(XMLStringDump);

		long end = System.currentTimeMillis();
		if (GET_TIME)
			System.out.println("\nDump and load and dump xml takes " + (end - start) + "ms");

		assertEquals(expectedXMLTag, actualXMLTag);
	}

}
