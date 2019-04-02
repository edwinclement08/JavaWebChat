package com.javapapers.webservices.rest.jersey.resthandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import com.javapapers.webservices.rest.jersey.dao.MessageStoreDB;
import com.javapapers.webservices.rest.jersey.dao.MessageStoreDao;
import com.javapapers.webservices.rest.jersey.dao.UserStoreDB;
import com.javapapers.webservices.rest.jersey.models.Message;
import com.javapapers.webservices.rest.jersey.models.User;
import com.javapapers.webservices.rest.jersey.resthandler.jsonholder.MessageTempHolder;
import com.javapapers.webservices.rest.jersey.resthandler.jsonholder.UserMessageRequestHolder;

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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendMessage(MessageTempHolder m) {
		Message message = new Message().setMessage(m.getContent()).setReceiver(m.getReceiver())
				.setSender(m.getUsername()).setTimeSent(Instant.now().getEpochSecond());
		Optional<User> u = userDao.getByUserName(m.getUsername());
		if (!u.isPresent()) {
			debugPrint("No User found with name :" + m.getUsername());
			return "{'status':false, 'message':'No Such User'}".replace("'", "\"");
		} else {
			User user = u.get();
			debugPrint("Got that User:" + user + " || tokenReceived: " + m.getToken());

			if (user.verifyToken(m.getToken())) {
				debugPrint("User Verified");
				boolean status = messageStoreDao.sendMessage(message);
				if (status) {
					return "{'status':true, 'message':'Message Sent'}".replace("'", "\"");
				} else {
					debugPrint("message send failure " + message.toString());
					return "{'status':false, 'message':'Message send failure'}".replace("'", "\"");
				}
			} else {
				debugPrint("user token invalid");
				return "{'status':false, 'message':'user token invalid'}".replace("'", "\"");

			}
		}
	}

	@POST
	@Path("/peekMessages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> peekMessages(UserMessageRequestHolder auth) {
		String userName = auth.getUsername();
		String token = auth.getToken();

		HashMap<String, Object> returnData = new HashMap<String, Object>();

		Optional<User> u = userDao.getByUserName(userName);
		if (!u.isPresent()) {
			debugPrint("No User found with name :" + userName);

			returnData.put("status", false);
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

				messages = messageStoreDao.peekMessage(user, auth.getFriend(), 3);

				returnData.put("status", true);
				returnData.put("message", "Data retrieved");
				returnData.put("count", messages.size());
				returnData.put("contents", messages);
				return returnData;
			} else {
				debugPrint("User token invalid");
				returnData.put("status", false);
				returnData.put("message", "User token invalid");
				return returnData;
			}
		}

	}

	@POST
	@Path("getAllMessages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getAllMessage(UserMessageRequestHolder auth) {
		return getMessages(auth.getUsername(), auth.getToken(), auth.getFriend(), true);

	}

	@POST
	@Path("getNewMessages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getNewMessage(UserMessageRequestHolder auth) {
		return getMessages(auth.getUsername(), auth.getToken(), auth.getFriend(), false);
	}

	/**
	 * @param userName    name of receiver
	 * @param token       token given by login
	 * @param allMessages true - all messages, false - un-received ones only
	 * @return
	 */
	private HashMap<String, Object> getMessages(String userName, String token, String friend, boolean allMessages) {
		HashMap<String, Object> returnData = new HashMap<String, Object>();

		Optional<User> u = userDao.getByUserName(userName);
		if (!u.isPresent()) {
			debugPrint("No User found with name :" + userName);

			returnData.put("status", false);
			returnData.put("message", "No Such User");
			return returnData;
		} else {
			debugPrint("Got that User");
			User user = u.get();

			if (user.verifyToken(token)) {
				debugPrint("User Verified");

				ArrayList<Message> messages;

				if (allMessages)
					messages = messageStoreDao.getAllMessages(user, friend);
				else
					messages = messageStoreDao.getNewMessages(user, friend);

				returnData.put("status", true);
				returnData.put("message", "Data retrieved");
				returnData.put("count", messages.size());
				returnData.put("contents", messages);
				return returnData;
			} else {
				debugPrint("User token invalid");
				debugPrint(user);
				debugPrint(token);
				debugPrint(user.getToken().equals(token));

				returnData.put("status", false);
				returnData.put("message", "User token invalid");
				return returnData;
			}
		}
	}

	@POST
	@Path("getUserFriends")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Object> getUserFriends(UserMessageRequestHolder auth) {
		HashMap<String, Object> returnData = new HashMap<String, Object>();
		UserValidationResult uv = verifyUser(auth.getUsername(), auth.getToken());
		if (uv.status) {
			returnData.put("friends", messageStoreDao.getChatFriends(uv.user));
			returnData.put("status", true);
			returnData.put("message", "Friend List Successfully Retrieved");
		} else {
			returnData.put("status", false);
			returnData.put("friends", new ArrayList<Strings>());
			returnData.put("message", uv.message);
		}
		return returnData;
	}

	class UserValidationResult {
		public String message = "";
		public boolean status = false;
		public User user = null;
	}

	private UserValidationResult verifyUser(String userName, String token) {
		UserValidationResult uv = new UserValidationResult();

		Optional<User> u = userDao.getByUserName(userName);
		if (!u.isPresent()) {
			uv.message = "No User found with name :" + userName;
			uv.status = false;
			return uv;
		} else {
			uv.message = "Got that User :" + userName;
			User user = u.get();
			if (user.verifyToken(token)) {
				uv.status = true;
				uv.message = "User Verified:" + userName;
				uv.user = user;
				return uv;
			} else {
				uv.message = "User token invalid:" + userName;
				uv.status = false;
				return uv;
			}
		}
	}

}
