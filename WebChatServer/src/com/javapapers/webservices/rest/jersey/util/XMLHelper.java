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

		totalDump.append(String.format("</%s>", name, parameterString));
		return totalDump.toString();
	};

}

class XMLParser {
	public static void p(char s) {
		System.out.println(s);
	}

	public static void p(String s) {
		System.out.println(s);
	}

	public static void printStack(Stack<XMLTag> childTagStack) {
		System.out.print("| STACK :: ");
		Object[] data = childTagStack.toArray();
		for (int i = 0; i < data.length; i++) {
			System.out.print(String.format(" ,%s", ((XMLTag) data[i]).toString()));
		}
		System.out.print(" END");
	}

	public static void printFrames(Stack<Stack<XMLTag>> stackframe) {
		System.out.print("FRAME :: ");
		Object[] data = stackframe.toArray();
		for (int i = 0; i < data.length; i++) {
			printStack((Stack<XMLTag>) data[i]);
		}
		System.out.println(" FEND");

	}

	public static XMLTag parse(String dump) {
		XMLTag root = new XMLTag("temp");
//		root.addChild(new XMLTag("temp")); // TODO remove this later

		Stack<XMLTag> parentTagStack = new Stack<XMLTag>();
		Stack<XMLTag> childTagStack = new Stack<XMLTag>();
		Stack<Stack<XMLTag>> stackframe = new Stack<Stack<XMLTag>>();

		int index = 0;
		boolean insideTag = false;
		boolean isStartTag = false;
		StringBuffer currentTagName = new StringBuffer();
		XMLTag currentTag = new XMLTag("temp2");

		StringBuffer currentTagContent = new StringBuffer();

		while (index < dump.length()) {
			char c = dump.charAt(index);

			switch (c) {
			case '<':
				insideTag = true;

				index += 1;
				c = dump.charAt(index);
				currentTagName = new StringBuffer();

				if (c == '/') {
					isStartTag = false;
					// terminating tag,
					if (currentTagContent.length() > 0) {
						System.out.println("The Content was " + currentTagContent);
						currentTag.setContent(currentTagContent.toString());
					} else {

						while (!childTagStack.isEmpty()) {
							currentTag.addChild(childTagStack.pop());
						}

					}

				} else {
					isStartTag = true;
					// push current frame to stack

//					printFrames(stackframe);
					stackframe.push(childTagStack);
					// set new empty stack for current node
					childTagStack = new Stack<XMLTag>();

				}
				break;
			case '>':
				if (insideTag) {
					insideTag = false;
					if (isStartTag) {
						// push tag into the stack
						parentTagStack.push(currentTag);
						currentTag = new XMLTag(currentTagName.toString());
					} else {
						// pop the top of stack

						childTagStack = stackframe.pop(); // get the parents child stack
						childTagStack.push(currentTag); // add self to parent child list
						currentTag = parentTagStack.pop(); // set parent to be the current tag

						currentTagContent = new StringBuffer();

						System.out.println(currentTagName);
					}

				} else {
					System.out.println("Some Issue is there :" + currentTagName);
				}
				break;
			default:
				if (insideTag) {
					if (c == ' ') {
						// parameters start now
						while (index < dump.length()) {
							char ct = dump.charAt(index);
							if (ct == '>')
								break;
							System.out.print(ct);

							index++;
						}
						System.out.println();

						index--;
					}
					currentTagName.append(c);
				} else {
					// the content
					currentTagContent.append(c);

				}

			}

			index += 1;
		}

		return currentTag; // (XMLTag) root.get(0);
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

		XMLTag root = new XMLTag("html");
		XMLTag head = new XMLTag("head");
		XMLTag body = new XMLTag("body");
		XMLTag div = new XMLTag("div");
		XMLTag div2 = new XMLTag("div");

		body.setParameter("id", "contentPage");
		body.setParameter("class", "indexPage");

		div.setContent("Hello World");
		div2.setContent("Trial2");

		root.addChild(head);
		root.addChild(body);
		body.addChild(div);
		body.addChild(div2);
		System.out.println("--START--");
		System.out.println(root);

		String dump = root.toString();

		System.out.println(XMLParser.parse(dump));

	}

}
