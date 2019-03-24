package com.javapapers.webservices.rest.jersey.models;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

import com.javapapers.webservices.rest.jersey.util.XMLSerializable;
import com.javapapers.webservices.rest.jersey.util.XMLTag;

public class User implements XMLSerializable {
	/**
	 * 
	 */

	private static final long TIMEOUT_PERIOD_FOR_TOKEN = 60 * 10; // 10 minutes
	public static final int TOKEN_LENGTH = 25;
	public static final boolean DEBUG = true;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	private String name;
	private String password;
	private String token;
	private long tokenCreationTime;

	public User(String name, String password, String token, long tokenCreationTime) {
		super();
		this.name = name;
		this.password = password;
		this.token = token;
		this.tokenCreationTime = tokenCreationTime;
	}

	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
		token = createToken();
		tokenCreationTime = Instant.now().getEpochSecond();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() { // TODO remove this thing later
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("{Username: %s, Password: %s, token:%s}", name, password, token);
	}

	public static String generateRandomString(int length) {
		Random random = new SecureRandom();
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(random.nextInt(characters.length()));
		}
		return new String(text);
	}

	public String createToken() {
		token = generateRandomString(25);
		tokenCreationTime = Instant.now().getEpochSecond();
		return token;
	}

	public String getToken() {
		return token;
	}

	public long gettokenCreationTime() {
		return tokenCreationTime;
	}

	public boolean verifyToken(String givenToken) {
		long now = Instant.now().getEpochSecond();

		long timeSinceLastLogin = now - tokenCreationTime;
		if (timeSinceLastLogin < TIMEOUT_PERIOD_FOR_TOKEN) {
			if (token.equals(givenToken))
				debugPrint("Token Validity Expired");
			return true;
		}
		return false;
	}

	@Override
	public XMLTag XMLDump() {
		XMLTag tag = new XMLTag("user");
		tag.addLeafChild("name", getName());
		tag.addLeafChild("password", getPassword());
		tag.addLeafChild("token", getToken());
		tag.addLeafChild("tokenCreationTime", String.valueOf(gettokenCreationTime()));
		return tag;
	}

	@Override
	public void XMLLoad(String dumps) {
		XMLTag tag = XMLTag.fromString(dumps);
		name = tag.queryByTagFirst("name").getContent();
		password = tag.queryByTagFirst("password").getContent();
		token = tag.queryByTagFirst("token").getContent();
		tokenCreationTime = Long.parseLong(tag.queryByTagFirst("tokenCreationTime").getContent());
	}

	@Override
	public boolean XMLDumpToFile(String fileName) {
		return XMLDump().dumpToFile(fileName);

	}

	@Override
	public boolean XMLLoadFromFile(String fileName) {
		XMLTag tag = XMLTag.loadFromFile(fileName);
		if (DEBUG)
			System.out.println("Tag Data: " + tag.toString());
		boolean status;
		if (tag.getName().equals("_error")) {
			status = false;
		} else {
			status = true;
		}
		XMLLoad(tag.toString());
		return status;

	}

}