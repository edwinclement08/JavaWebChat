package com.javapapers.webservices.rest.jersey.dao;

import java.util.ArrayList;

import com.javapapers.webservices.rest.jersey.models.Message;
import com.javapapers.webservices.rest.jersey.models.User;

public abstract class MessageStoreDao {
	public abstract boolean sendMessage(Message m);

	public abstract ArrayList<Message> getNewMessages(User user);

	public abstract ArrayList<Message> getAllMessages(User user);

	public abstract ArrayList<Message> peekMessage(User user, int count);
}
