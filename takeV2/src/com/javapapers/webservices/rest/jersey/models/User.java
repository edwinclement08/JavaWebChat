package com.javapapers.webservices.rest.jersey.models;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;



public class User implements Serializable {
    /**
	 * 
	 */
	
	private static final long TIMEOUT_PERIOD_FOR_TOKEN = 60*10; // 10 minutes
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String password;
    private String token;
    private LocalDateTime tokenCreationTime;
    

    public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString()	{
		return String.format("{Username: %s, Password: %s", name, password);
	}
	
	protected String generateString(int length) {
		Random  random = new SecureRandom();
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
	
	public String createToken()	{
		token = generateString(25);
		tokenCreationTime = LocalDateTime.now();
		return token;
	}
	
	public boolean verifyToken(String token)	{
		LocalDateTime now = LocalDateTime.now();
		
		long timeSinceLastLogin = Duration.between(now, tokenCreationTime).getSeconds();
		if (timeSinceLastLogin > TIMEOUT_PERIOD_FOR_TOKEN)	{
			// invalid login
			return false;
		} 
		return true;
	}
	
}