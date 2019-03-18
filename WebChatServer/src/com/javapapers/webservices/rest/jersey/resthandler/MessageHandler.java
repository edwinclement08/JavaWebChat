package com.javapapers.webservices.rest.jersey.resthandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.javapapers.webservices.rest.jersey.dao.MessageStoreDB;
import com.javapapers.webservices.rest.jersey.dao.MessageStoreDao;
import com.javapapers.webservices.rest.jersey.dao.UserStoreDB;
import com.javapapers.webservices.rest.jersey.models.Message;
import com.javapapers.webservices.rest.jersey.models.User;

@Path("/message")
@Singleton
public class MessageHandler {
	static Logger logger = LogManager.getLogger(MessageHandler.class.getName());
	private boolean USE_SQL_DB = true;
	public boolean DEBUG = true;

	MessageStoreDao messageStoreDao;
	UserStoreDB userDao;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	public MessageHandler() {

		userDao = UserStoreDB.getInstance();
		if (USE_SQL_DB) {
			messageStoreDao = MessageStoreDB.getInstance();

		}
		if (!USE_SQL_DB || messageStoreDao == null) {
			// Use other TYPE of store TODO

		}
		debugPrint("Initialization Complete");

	}

	@POST
	@Path("sendMessage")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendMessage(@FormParam("username") String userName, @FormParam("token") String token,
			@FormParam("receiver") String receiver, @FormParam("content") String content) {
		Message message = new Message().setMessage(content).setReceiver(receiver).setSender(userName)
				.setTimeSent(Instant.now().getEpochSecond());
		Optional<User> u = userDao.getByUserName(userName);
		if (!u.isPresent()) {
			debugPrint("No User found with name :" + userName);
			return "{'status':'false', 'message':'No Such User'}".replace("'", "\"");
		} else {
			User user = u.get();
			debugPrint("Got that User:" + user + " || tokenReceived: " + token);

			if (user.verifyToken(token)) {
				debugPrint("User Verified");
				boolean status = messageStoreDao.sendMessage(message);
				if (status) {
					return "{'status':'true', 'message':'Message Sent'}".replace("'", "\"");
				} else {
					debugPrint("message send failure " + message.toString());
					return "{'status':'false', 'message':'Message send failure'}".replace("'", "\"");
				}
			} else {
				debugPrint("user token invalid");
			}

		}

		return "{'status':'false', 'message':'General Failure'}".replace("'", "\"");
	}

	@POST
	@Path("getAllMessages")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getAllMessage(@FormParam("username") String userName,
			@FormParam("token") String token) {
		return getMessages(userName, token, true);

	}

	@POST
	@Path("getNewMessages")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getNewMessage(@FormParam("username") String userName,
			@FormParam("token") String token) {
		return getMessages(userName, token, false);
	}

	/**
	 * @param userName    name of receiver
	 * @param token       token given by login
	 * @param allMessages true - all messages, false - un-received ones only
	 * @return
	 */
	private HashMap<String, Object> getMessages(String userName, String token, boolean allMessages) {
		HashMap<String, Object> returnData = new HashMap<String, Object>();

		Optional<User> u = userDao.getByUserName(userName);
		if (!u.isPresent()) {
			debugPrint("No User found with name :" + userName);

			returnData.put("Status", "false");
			returnData.put("message", "No Such User");
			return returnData;
		} else {
			debugPrint("Got that User");
			User user = u.get();
			debugPrint(user);
			debugPrint(token);

			if (user.verifyToken(token)) {
				debugPrint("User Verified");

				ArrayList<Message> messages;

				if (allMessages)
					messages = messageStoreDao.getAllMessages(user);
				else
					messages = messageStoreDao.getNewMessages(user);

				returnData.put("Status", "true");
				returnData.put("message", "Data retrieved");
				returnData.put("count", messages.size());
				returnData.put("contents", messages);
				return returnData;
			} else {
				debugPrint("User token invalid");
				returnData.put("Status", "true");
				returnData.put("message", "User token invalid");
				return returnData;
			}
		}

	}
}
