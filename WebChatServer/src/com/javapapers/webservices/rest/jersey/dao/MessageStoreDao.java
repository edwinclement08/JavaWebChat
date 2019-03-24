package com.javapapers.webservices.rest.jersey.dao;

import java.util.ArrayList;

import com.javapapers.webservices.rest.jersey.models.Message;
import com.javapapers.webservices.rest.jersey.models.User;

public abstract class MessageStoreDao {
	public abstract boolean sendMessage(Message m);

	public abstract ArrayList<Message> getNewMessages(User user, String friend);

	public abstract ArrayList<Message> getAllMessages(User user, String friend);

	public abstract ArrayList<Message> peekMessage(User user, String friend, int count);

	public abstract ArrayList<String> getChatFriends(User user);

}
