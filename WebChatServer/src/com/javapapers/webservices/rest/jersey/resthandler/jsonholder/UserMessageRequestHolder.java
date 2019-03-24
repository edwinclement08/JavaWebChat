package com.javapapers.webservices.rest.jersey.resthandler.jsonholder;

public class UserMessageRequestHolder {
	public String username;
	public String token;
	public String friend;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}
}
