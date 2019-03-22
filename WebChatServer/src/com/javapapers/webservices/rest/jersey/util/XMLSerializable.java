package com.javapapers.webservices.rest.jersey.util;

public interface XMLSerializable {
	public XMLTag XMLDump();

	public boolean XMLDumpToFile(String fileName);

	public void XMLLoad(String dump);

	public boolean XMLLoadFromFile(String fileName);

}
