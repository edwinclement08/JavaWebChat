package com.javapapers.webservices.rest.jersey.resthandler;

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

import com.javapapers.webservices.rest.jersey.dao.UserDao;
import com.javapapers.webservices.rest.jersey.models.User;

@Path("/user")
@Singleton
public class UserHandler {
	public UserDao userDao;
	public static final boolean DEBUG = true;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	public UserHandler() {
		System.out.println("UserHandler init");
		userDao = UserDao.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> sayPlainTextHello() {
		return new ArrayList<User>(userDao.getAll());
	}

	@GET
	@Path("/username/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getByName(@PathParam("username") String userName) {

		User user = new User("-", "-");
		ArrayList<User> result = new ArrayList<User>();

		try {
			user = userDao.getByUserName(userName).get();
		} catch (NoSuchElementException e) {
			// e.printStackTrace();
			if (DEBUG)
				System.out.println("No Such user present");
			return result;
		}
		result.add(user);
		return result;

	}

	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getById(@PathParam("id") long id) {

		User user = new User("-", "-");
		ArrayList<User> result = new ArrayList<User>();

		try {
			user = userDao.get(id).get();
		} catch (NoSuchElementException e) {
			// e.printStackTrace();
			if (DEBUG)
				System.out.println("No Such user present");
			return result;
		}
		result.add(user);
		return result;
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String registerUser(@FormParam("username") String userName, @FormParam("password") String password) {
		User newUser = new User(userName, password);
		String token = newUser.createToken();
		boolean status = userDao.save(newUser);
		String jsonString = "{'username': '%s', 'token':'%s', 'status': '%b'}".replace("'", "\"");
		return String.format(jsonString, userName, token, status);

	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public String loginUser(@FormParam("username") String userName, @FormParam("password") String password) {
		String responseString = "{'status': '%b', 'token':'%s', 'message':'%s' }".replace("'", "\"");
		User user = new User("-", "-");
		try {
			user = userDao.getByUserName(userName).get();
			String token = user.createToken();
			debugPrint(user);
			userDao.save(user);
			debugPrint("saved");
			return String.format(responseString, true, token, "Successful Login");
		} catch (NoSuchElementException e) {
			return String.format(responseString, false, "", "No Such User");
		}
	}

	@POST
	@Path("/delete/")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUser(@FormParam("username") String userName, @FormParam("password") String password) {
		String responseString = "{'status': '%b', 'message':'%s' }".replace("'", "\"");

		boolean status = userDao.deleteByUsername(userName, password);
		if (status)
			return String.format(responseString, status, "Successful Deletion");
		else
			return String.format(responseString, false, "No Such User");

	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateUser(@FormParam("username") String userName, @FormParam("oldpassword") String oldPassword,
			@FormParam("newpassword") String newPassword) {
		boolean status = userDao.updateByUsername(userName, oldPassword, new String[] { userName, newPassword });
		String jsonString = "{'username': '%s', 'status': '%b'}".replace("'", "\"");
		return String.format(jsonString, userName, status);

	}
}