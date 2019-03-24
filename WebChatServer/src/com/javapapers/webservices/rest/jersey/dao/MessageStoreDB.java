package com.javapapers.webservices.rest.jersey.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import com.javapapers.webservices.rest.jersey.models.Message;
import com.javapapers.webservices.rest.jersey.models.User;
import com.javapapers.webservices.rest.jersey.util.DatabaseConnection;

// TODO Ask about the problem with singleton and no static methods allowed
// in interfaces
public class MessageStoreDB extends MessageStoreDao {
	private static MessageStoreDB _instance = null;
	private boolean DEBUG = true;

	private DatabaseConnection db;
	private Connection dbConnection;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	private MessageStoreDB() {
		db = DatabaseConnection.getDatabase();
		dbConnection = db.getConnection();

		initializeMessageTable();
	}

	private void initializeMessageTable() {
		try {
			String myTableName = String.join("\n", "CREATE TABLE IF NOT EXISTS MessageDatabase (",
					"id INT(64) NOT NULL AUTO_INCREMENT,", "sender VARCHAR(30),", "receiver VARCHAR(30),",
					"message VARCHAR(200),", "timeSent int,", "transferred bool, ", "PRIMARY KEY(id));");
			;
			Statement statement;
			statement = dbConnection.createStatement();
			statement.executeUpdate(myTableName);
			debugPrint("Table Created if not existed");
		} catch (SQLException e) {
			e.printStackTrace();
			debugPrint("Failed in Table Creation.");
		}
	}

	public static MessageStoreDB getInstance() {
		if (_instance == null)
			_instance = new MessageStoreDB();
		return _instance;
	}

	@Override
	public boolean sendMessage(Message m) {
		// check if user already exists

		// create a record
		String messageSendSQL = String.join("\n", "INSERT INTO MessageDatabase ",
				"(sender, receiver, message, timeSent, transferred) VALUES (?,?,?,?,?);");

		try {
			PreparedStatement ps = dbConnection.prepareStatement(messageSendSQL);
			ps.setString(1, m.getSender());
			ps.setString(2, m.getReceiver());
			ps.setString(3, m.getMessage());
			ps.setLong(4, m.getTimeSent());
			ps.setBoolean(5, false);

			int result = ps.executeUpdate();

			if (result == 1) {
				debugPrint("Message added to Database");
				return true;
			} else {
				return false;
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			debugPrint("Message already exist in DB.(resend is failure)");
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			debugPrint("Failed in adding mock Message.");
			return false;
		}
	}

	@Override
	public ArrayList<Message> peekMessage(User user, String friend, int count) {
		String peekMessagesSQL = String.join("\n",
				"SELECT * FROM (SELECT id, sender, receiver, message, timeSent, transferred",
				" from MessageDatabase where (receiver='%s' and sender='%s')  or (receiver='%s' and sender='%s') ",
				" ORDER BY  id DESC LIMIT  %d ) sdf order by id;");
		peekMessagesSQL = String.format(peekMessagesSQL, user.getName(), friend, friend, user.getName());

		debugPrint(peekMessagesSQL);

		ArrayList<Message> resultListOfMessages = new ArrayList<Message>();
		try {
			Statement ps = dbConnection.createStatement();
			ResultSet rs = ps.executeQuery(peekMessagesSQL);

			while (rs.next()) {
				long id = rs.getLong("id");

				String sender = rs.getString("sender");
				String receiver = rs.getString("receiver");
				String message = rs.getString("message");
				long timeSent = rs.getLong("timeSent");
				boolean transferred = rs.getBoolean("transferred");

				Message receivedMessage = new Message();
				receivedMessage.setId(id).setSender(sender).setReceiver(receiver).setMessage(message)
						.setTimeSent(timeSent).setTransferred(transferred);
				resultListOfMessages.add(receivedMessage);
				debugPrint(receivedMessage);
			}
			debugPrint("peeked all Messages for user:" + user.getName());

		} catch (SQLException e) {
			e.printStackTrace();
			debugPrint("Failed in executing SQL(peek messages).");
		}
		return resultListOfMessages;
	}

	@Override
	public ArrayList<Message> getAllMessages(User user, String friend) {
		return getMessages(user, friend, true);
	}

	@Override
	public ArrayList<Message> getNewMessages(User user, String friend) {
		return getMessages(user, friend, false);
	}

	private ArrayList<Message> getMessages(User user, String friend, boolean allMessages) {
		String getAllMessagesSQL = String.join("\n", "SELECT id, sender, receiver, message, timeSent, transferred",
				" from MessageDatabase where ((receiver='%s' and sender='%s')  or (receiver='%s' and sender='%s')) ");
		getAllMessagesSQL = String.format(getAllMessagesSQL, user.getName(), friend, friend, user.getName());
		if (!allMessages)
			getAllMessagesSQL += "and transferred=0";
		debugPrint(getAllMessagesSQL);

		ArrayList<Message> resultListOfMessages = new ArrayList<Message>();
		ArrayList<Long> listOfAllRecvMessageId = new ArrayList<Long>();
		try {
			Statement ps = dbConnection.createStatement();
			ResultSet rs = ps.executeQuery(getAllMessagesSQL);

			while (rs.next()) {
				long id = rs.getLong("id");
				listOfAllRecvMessageId.add(id); // for updating the db that all these messages have been sent.

				String sender = rs.getString("sender");
				String receiver = rs.getString("receiver");
				String message = rs.getString("message");
				long timeSent = rs.getLong("timeSent");
				boolean transferred = rs.getBoolean("transferred");

				Message receivedMessage = new Message();
				receivedMessage.setId(id).setSender(sender).setReceiver(receiver).setMessage(message)
						.setTimeSent(timeSent).setTransferred(transferred);
				resultListOfMessages.add(receivedMessage);
				debugPrint(receivedMessage);
			}
			debugPrint("Retrieved all Messages for user:" + user.getName());

			updateTransferredStatus(user.getName(), listOfAllRecvMessageId);
			debugPrint("Updated transfer status of all Messages for user:" + user.getName());

		} catch (SQLException e) {
			e.printStackTrace();
			debugPrint("Failed in executing SQL(get all messages).");
		}
		return resultListOfMessages;

	}

	private boolean updateTransferredStatus(String receiver, ArrayList<Long> ids) {
		String updateTransferredStatusSQL = "UPDATE MessageDatabase SET  transferred = ? where id = ? and receiver=?";

		int successCount = 0;
		try {
			PreparedStatement ps = dbConnection.prepareStatement(updateTransferredStatusSQL);

			for (int count = 0; count < ids.size(); count++) {
				ps.setBoolean(1, true);
				ps.setLong(2, ids.get(count));
				ps.setString(3, receiver);

				int result = ps.executeUpdate();
				successCount += result == 1 ? 1 : 0;
			}

			if (successCount == ids.size()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			debugPrint("Some Error in updating message transferred status");
			return false;
		}
	}

	@Override
	public ArrayList<String> getChatFriends(User user) {
		String getFriendsSQL = String.join("\n", "SELECT sender from MessageDatabase where (receiver='%s') UNION ",
				"SELECT receiver from MessageDatabase where (sender='%s');");

		getFriendsSQL = String.format(getFriendsSQL, user.getName(), user.getName());

		debugPrint("SENDERSQL:" + getFriendsSQL);

		ArrayList<String> resultOfFriends = new ArrayList<String>();

		try {
			Statement ps = dbConnection.createStatement();
			ResultSet rs = ps.executeQuery(getFriendsSQL);
			while (rs.next()) {
				String friend = rs.getString("sender");

				resultOfFriends.add(friend);
				debugPrint(friend);
			}
			debugPrint("Retrieved all Friends for user:" + user.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			debugPrint("Failed in executing SQL(get all friends).");
		}
		return resultOfFriends;
	}

}
