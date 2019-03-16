package com.javapapers.webservices.rest.jersey.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Singleton;
import com.javapapers.webservices.rest.jersey.models.User;

@Singleton
public class UserDao implements Dao<User> {
	private boolean USE_SQL_DB = false;
	private ArrayList<User> users = new ArrayList<>();
	
	private DatabaseConnection db;


	public UserDao() {
		if (USE_SQL_DB) { // TODO
			db = DatabaseConnection.getDatabaseConnection();


		} else {
			users.add(new User("John", "pass123"));
			users.add(new User("Susan", "suzy"));
		}

	}

	@Override
	public Optional<User> get(long id) {
		if (USE_SQL_DB) {// TODO
			return Optional.ofNullable(users.get((int) id));
		} else {
			return Optional.ofNullable(users.get((int) id));
		}
	}

	public Optional<User> getByUserName(String userName) {
		if (USE_SQL_DB) {// TODO
			return Optional.ofNullable(null);
		} else {

			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getName().equals(userName)) {
					return Optional.ofNullable(users.get(i));
				}
			}
			return Optional.ofNullable(null);
		}
	}

	@Override
	public List<User> getAll() {
		if (USE_SQL_DB) {// TODO
			return users;
		} else {
			return users;
		}
	}

	@Override
	public void save(User user) {
		if (USE_SQL_DB) {// TODO
			users.add(user);
		} else {
			users.add(user);
		}
	}

	@Override
	public boolean update(User user, String[] params) {
		if (users.indexOf(user) != -1) {
			// the user exists previously
			user.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
			user.setPassword(Objects.requireNonNull(params[1], "password cannot be null"));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void delete(User user) {
		users.remove(user);
	}
}