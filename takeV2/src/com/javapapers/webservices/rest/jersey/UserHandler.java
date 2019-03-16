package com.javapapers.webservices.rest.jersey;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.javapapers.webservices.rest.jersey.models.User;
import com.javapapers.webservices.rest.jersey.models.UserDao;

@Path("/user")
@Singleton
public class UserHandler {
	public UserDao userDao;

	public UserHandler() {
		System.out.println("UserHandler init");
		userDao = new UserDao();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> sayPlainTextHello() {
		return new ArrayList<User>(userDao.getAll());
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAll(@PathParam("username") String userName) {

		User user = new User("-", "-");
		try {
			user = userDao.getByUserName(userName).get();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		ArrayList<User> result = new ArrayList<User>();
		result.add(user);
		return result;

	}
	
	@POST
	@Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String registerUser(@FormParam("username") String userName,
    		@FormParam("password") String password)	{
		User newUser = new User(userName, password);
		String token = newUser.createToken();
		userDao.save(newUser);
		String jsonString = "{'username': '%s', 'token':'%s', 'status': '%b'}".replace("'", "\"");
		return String.format(jsonString, userName, token, true);
		
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public String loginUser(@FormParam("username") String userName,
			@FormParam("password") String password) {
		String responseString = "{'status': '%b', 'token':'%s', 'message':'%s' }".replace("'", "\"");
		User user = new User("-", "-");
		try {
			user = userDao.getByUserName(userName).get();
			String token = user.createToken();
			return String.format(responseString, true, token, "Successful Login");
		} catch (NoSuchElementException e) {
			return String.format(responseString, false, "No Such User");
		}
	}
	
}