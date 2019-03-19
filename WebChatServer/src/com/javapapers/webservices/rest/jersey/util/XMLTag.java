package com.javapapers.webservices.rest.jersey.util;

import java.util.ArrayList;
import java.util.HashMap;

public class XMLTag {
	String name;
	HashMap<String, String> parameters;

	boolean isLeaf = true;
	String content;
	ArrayList<XMLTag> children = null;

	public XMLTag(String name) {
		this.name = name;
		parameters = new HashMap<String, String>();
		content = "";
		children = new ArrayList<XMLTag>();
	}

	public XMLTag(String name, HashMap<String, String> parameters) {
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

	public static XMLTag fromString(String dumps) {
		return XMLParser.getInstance().parse(dumps);
	}

}

class XMLParser {
	class ParseReturn {
		public int index;
		public XMLTag tag;

		public ParseReturn(int index, XMLTag tag) {
			this.index = index;
			this.tag = tag;
		}
	}

	public boolean DEBUG = false;
	private static XMLParser _instance = null;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	private XMLParser() {
	}

	public static XMLParser getInstance() {
		if (_instance == null) {
			_instance = new XMLParser();
		}
		return _instance;
	}

	ParseReturn parse(String dumps, Integer index, String parentName, String parameters) {
		debugPrint("parent name :" + parentName);
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

		char c = dumps.charAt(index);

		while (index < dumps.length()) {
			c = dumps.charAt(index);
			if (c == '<') {
				index++;
				char ct = dumps.charAt(index);
				if (ct == '/') { // end tag

					insideTag = true;
					while (ct != '>') {

						index += 1;
						ct = dumps.charAt(index);

					}

					childArray.forEach((XMLTag xtag) -> tag.addChild(xtag));
					debugPrint("d: " + tag);
					return new ParseReturn(index, tag);
				} else { // start tag of subtag

					insideTag = true;

					StringBuffer childTagName = new StringBuffer("");

					c = dumps.charAt(index);
					while (c != '>' && c != ' ') {
						childTagName.append(c);
						index++;
						c = dumps.charAt(index);

					} // for tag name
					debugPrint("new tag: " + childTagName);
					StringBuffer params = new StringBuffer("");
					if (c == ' ') {
						index++;
						c = dumps.charAt(index);
						while (c != '>') {
							params.append(c);
							index++;
							c = dumps.charAt(index);
						} // for tag parameters
					}

					debugPrint("--------params: " + params);

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

				}

			}
			index++;
		}
		childArray.forEach((XMLTag xtag) -> tag.addChild(xtag));

		return new ParseReturn(index, tag);
	}

	public XMLTag parse(String dumps) {
		ParseReturn d = XMLParser.getInstance().parse(dumps, 0, "root", "");
		XMLTag child = d.tag.children.get(0);
		return child;

	}

}