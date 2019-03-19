package com.javapapers.webservices.rest.jersey.models;

import com.javapapers.webservices.rest.jersey.util.XMLSerializable;
import com.javapapers.webservices.rest.jersey.util.XMLTag;

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

	public boolean getTransferred() {
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
	public XMLTag XMLDump() {
		XMLTag tag = new XMLTag("message");
		tag.addLeafChild("content", getMessage());
		tag.addLeafChild("receiver", getReceiver());
		tag.addLeafChild("sender", getSender());
		tag.addLeafChild("timeSent", String.valueOf(getTimeSent()));
		tag.addLeafChild("transferred", String.valueOf(getTransferred()));
		return tag;
	}

	@Override
	public void XMLLoad(String dumps) {
		XMLTag tag = XMLTag.fromString(dumps);
		message = tag.queryByTagFirst("content").getContent();
		receiver = tag.queryByTagFirst("receiver").getContent();
		sender = tag.queryByTagFirst("sender").getContent();
		timeSent = Long.parseLong(tag.queryByTagFirst("timeSent").getContent());
		transferred = Boolean.parseBoolean(tag.queryByTagFirst("transferred").getContent());
	}

}
