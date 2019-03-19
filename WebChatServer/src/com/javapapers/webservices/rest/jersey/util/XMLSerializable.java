package com.javapapers.webservices.rest.jersey.util;

public interface XMLSerializable {
	public XMLTag XMLDump();

	public void XMLLoad(String dump);
}
