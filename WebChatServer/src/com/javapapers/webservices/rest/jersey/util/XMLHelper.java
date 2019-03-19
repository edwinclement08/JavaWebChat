package com.javapapers.webservices.rest.jersey.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

class XMLTag {
	String name;
	HashMap<String, String> parameters;

	boolean isLeaf = true;
	String content;
	ArrayList<XMLTag> children = null;

	XMLTag(String name) {
		this.name = name;
		parameters = new HashMap<String, String>();
		content = "";
		children = new ArrayList<XMLTag>();
	}

	XMLTag(String name, HashMap<String, String> parameters) {
		this.name = name;
		this.parameters = parameters;
		content = "";
		children = new ArrayList<XMLTag>();
	}

	public void setParameter(String name, String value) {
		parameters.put(name, value);
	}

	public void setContent(String content) {
		isLeaf = true;
		this.content = content;
	}

	public void addChild(XMLTag child) {
		isLeaf = false;
		this.children.add(child);
	}

	// TODO
	public void queryChildrenById(String name) {

	}

	public Object get(int i) {
		if (isLeaf)
			return content;
		else
			return children.get(i);
	}

	public String toString() {
		StringBuffer parameterString = new StringBuffer("");
		parameters.forEach((name, value) -> {
			parameterString.append(String.format(" %s='%s'", name, value));
		});

		StringBuffer totalDump = new StringBuffer("");
		totalDump.append(String.format("<%s%s>", name, parameterString));

		if (isLeaf) {
			totalDump.append(content);
		} else {
			for (int index = 0; index < children.size(); index++) {
				totalDump.append(children.get(index).toString());
			}
		}

		totalDump.append(String.format("</%s>", name));
		return totalDump.toString();
	};

}

class XMLParser {

	static class ParseReturn {
		public int index;
		public XMLTag tag;

		public ParseReturn(int index, XMLTag tag) {
			this.index = index;
			this.tag = tag;
		}
	}

	public static ParseReturn parse(String dumps, Integer index, String parentName, String parameters) {
//		System.out.println("parent name :" + parentName);
		XMLTag tag = new XMLTag(parentName);

		if (parameters.length() > 0) {

			String[] paramList = parameters.split(" ");

			for (String item : paramList) {
				String[] individual = item.split("=");

				tag.setParameter(individual[0], individual[1].replace('\'', ' ').trim());
			}
		}

		ArrayList<XMLTag> childArray = new ArrayList<XMLTag>();

		boolean insideTag = false;
		boolean startTag = false;

		char c = dumps.charAt(index);

		while (index < dumps.length()) {
			c = dumps.charAt(index);
			if (c == '<') {
				index++;
				char ct = dumps.charAt(index);
				if (ct == '/') { // end tag
					startTag = false;
					insideTag = true;
					while (ct != '>') {

						index += 1;
						ct = dumps.charAt(index);

					}

					childArray.forEach((XMLTag xtag) -> tag.addChild(xtag));
//					System.out.println("d: " + tag);
					return new ParseReturn(index, tag);
				} else { // start tag of subtag
					startTag = true;
					insideTag = true;

					StringBuffer childTagName = new StringBuffer("");

					c = dumps.charAt(index);
					while (c != '>' && c != ' ') {
						childTagName.append(c);
						index++;
						c = dumps.charAt(index);

					} // for tag name
//					System.out.println("new tag: " + childTagName);
					StringBuffer params = new StringBuffer("");
					if (c == ' ') {
						index++;
						c = dumps.charAt(index);
						while (c != '>') {
							params.append(c);
							index++;
							c = dumps.charAt(index);
						} // for tag params
					}

//					System.out.println("--------params: " + params);

					insideTag = false;

					ParseReturn d = parse(dumps, index, childTagName.toString(), params.toString());
					childArray.add(d.tag);
					index = d.index;

				}

			} else {
				// content of tag
				// TODO add support for escape chars
				if (!insideTag) {
					StringBuffer content = new StringBuffer();
					try {
						if (c == '>')
							index++;
						c = dumps.charAt(index);
						while (c != '<') {
							content.append(c);
							index++;
							c = dumps.charAt(index);
						}
						index--;

						tag.setContent(content.toString());

					} catch (Exception e) {
						// TODO: handle exception
//						System.out.println("dsds" + content);
						e.printStackTrace();
					}
				}

			}
			index++;
		}
		childArray.forEach((XMLTag xtag) -> tag.addChild(xtag));

		return new ParseReturn(index, tag);
	}

	public static XMLTag parse(String dumps) {
		ParseReturn d = XMLParser.parse(dumps, 0, "root", "");
		XMLTag child = d.tag.children.get(0);
		return child;

	}

}

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

		XMLTag result = XMLParser.parse(dump);

		// end of function

		// ending time
		long end = System.currentTimeMillis();
		System.out.println("dump and load and dump xml takes " + (end - start) + "ms");
		System.out.println(dump);
		System.out.println("--START--");
		System.out.println(result);

	}

}
