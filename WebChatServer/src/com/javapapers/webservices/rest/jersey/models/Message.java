package com.javapapers.webservices.rest.jersey.models;

import com.javapapers.webservices.rest.jersey.util.XMLSerializable;

public class Message implements XMLSerializable {
	long id;

	String sender;
	String receiver;
	String message;
	long timeSent;
	boolean transferred;

	public Message setSender(String sender) {
		this.sender = sender;
		return this;
	}

	public Message setReceiver(String receiver) {
		this.receiver = receiver;
		return this;
	}

	public Message setMessage(String message) {
		this.message = message;
		return this;
	}

	public Message setTimeSent(long timeSent) {
		this.timeSent = timeSent;
		return this;
	}

	public Message setTransferred(boolean transferred) {
		this.transferred = transferred;
		return this;
	}

	public Message setId(long id) {
		this.id = id;
		return this;
	}

	public boolean isTransferred() {
		return transferred;
	}

	public long getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getMessage() {
		return message;
	}

	public long getTimeSent() {
		return timeSent;
	}

	public String toString() {
		String fs = "{'message': '%s','receiver': '%s','sender': '%s','timeSent': '%s','transferred': '%b'}";
		fs = fs.replace("'", "\"");
		return String.format(fs, message, receiver, sender, timeSent, transferred);
	}

	@Override
	public String XMLDump() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void XMLLoad() {
		// TODO Auto-generated method stub

	}

}
