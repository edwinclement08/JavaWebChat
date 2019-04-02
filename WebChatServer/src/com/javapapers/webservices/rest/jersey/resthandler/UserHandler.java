package com.javapapers.webservices.rest.jersey.resthandler;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.javapapers.webservices.rest.jersey.dao.UserStoreDB;
import com.javapapers.webservices.rest.jersey.models.User;
import com.javapapers.webservices.rest.jersey.resthandler.jsonholder.UserChangePasswordHolder;
import com.javapapers.webservices.rest.jersey.resthandler.jsonholder.UserHolder;

@Path("/user")
@Singleton
public class UserHandler {
	public UserStoreDB userDao;
	public static final boolean DEBUG = true;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	public UserHandler() {
		System.out.println("UserHandler init");
		userDao = UserStoreDB.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> allNames() {
		return new ArrayList<String>(
				userDao.getAll().stream().map(user -> user.getName()).collect(Collectors.toList()));
	}

	@GET
	@Path("/details/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> allUserDetails() {
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
	@Consumes(MediaType.APPLICATION_JSON)
	public String registerUser(UserHolder regUser) {
		User newUser = new User(regUser.getUsername(), regUser.getPassword());
		String token = newUser.createToken();
		boolean status = userDao.add(newUser);
		String jsonString = "{'username': '%s', 'token':'%s', 'status': '%b'}".replace("'", "\"");
		return String.format(jsonString, regUser.getUsername(), token, status);
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String loginUser(UserHolder newUser) {
		String responseString = "{'status': '%b', 'user':'%s' ,'token':'%s', 'message':'%s' }".replace("'", "\"");
		User user = new User("", "");
		try {
			user = userDao.getByUserName(newUser.getUsername()).get();
			if (user.getPassword().equals(newUser.getPassword())) {
				debugPrint(user);
				String token = user.createToken();
				userDao.save(user);
				debugPrint("saved");
				return String.format(responseString, true, newUser.getUsername(), token, "Successful Login");
			} else {
				return String.format(responseString, false, "", "", "Wrong Password");
			}
		} catch (NoSuchElementException e) {
			return String.format(responseString, false, "", "", "No Such User");
		}
	}

	@POST
	@Path("/delete/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteUser(UserHolder newUser) {
		String responseString = "{'status': '%b', 'message':'%s' }".replace("'", "\"");

		boolean status = userDao.deleteByUsername(newUser.getUsername(), newUser.getPassword());
		if (status)
			return String.format(responseString, status, "Successful Deletion");
		else
			return String.format(responseString, false, "No Such User");
	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateUser(UserChangePasswordHolder userDetails) {
		boolean status = userDao.updateByUsername(userDetails.getUsername(), userDetails.getOldpassword(),
				new String[] { userDetails.getUsername(), userDetails.getNewpassword() });
		String jsonString = "{'username': '%s', 'status': '%b'}".replace("'", "\"");
		return String.format(jsonString, userDetails.getUsername(), status);
	}
}