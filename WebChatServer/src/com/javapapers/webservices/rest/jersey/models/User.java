package com.javapapers.webservices.rest.jersey.models;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

public class User implements Serializable {
	/**
	 * 
	 */

	private static final long TIMEOUT_PERIOD_FOR_TOKEN = 60 * 10; // 10 minutes
	private static final long serialVersionUID = 1L;

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

	protected String generateRandomString(int length) {
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
				return true;
		}
		return false;
	}

}