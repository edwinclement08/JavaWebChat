package com.javapapers.webservices.rest.jersey.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Singleton;

@Singleton
public class UserDao implements Dao<User> {
	private boolean USE_SQL_DB = true;
	private boolean DEBUG = true;
	private ArrayList<User> users = new ArrayList<>();

	private DatabaseConnection db;
	private Connection dbConnection;

	public UserDao() {
		if (USE_SQL_DB) {
			db = DatabaseConnection.getDatabase();
			dbConnection = db.getConnection();

			if (dbConnection == null) {
				// dbConnection Fail
				USE_SQL_DB = false; // executes the below code
			} else {
				try {
					String myTableName = "CREATE TABLE IF NOT EXISTS UserDatabase ("
							+ "idNo INT(64) NOT NULL AUTO_INCREMENT," + "username VARCHAR(30) UNIQUE,"
							+ "password VARCHAR(30)," + "PRIMARY KEY(idNo))";
					Statement statement;
					statement = dbConnection.createStatement();
					statement.executeUpdate(myTableName);
					if (DEBUG)
						System.out.println("Table Created if not existed");
				} catch (SQLException e) {
					e.printStackTrace();
					if (DEBUG)
						System.out.println("Failed in Table Creation.");
				}
			}

			// create a few dummy values
			save(new User("John", "pass123"));
			save(new User("Susan", "suzy"));
		}

		if (!USE_SQL_DB) {
			users.add(new User("John", "pass123"));
			users.add(new User("Susan", "suzy"));
		}

	}

	@Override
	public Optional<User> get(long id) {
		if (USE_SQL_DB) {
			String getUserSQL = String.join("\n", "Select username,password from  UserDatabase where idNo=%d;");
			getUserSQL = String.format(getUserSQL, id);

			User user = null;
			try {
				Statement ps = dbConnection.createStatement();
				ResultSet rs = ps.executeQuery(getUserSQL);

				if (rs.next()) {
					String userName = rs.getString("username");
					String password = rs.getString("password");
					user = new User(userName, password);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				if (DEBUG)
					System.out.println("Failed in executing SQL(get user by id).");
			}
			return Optional.ofNullable(user);
		} else {
			return Optional.ofNullable(users.get((int) id));
		}
	}

	public Optional<User> getByUserName(String userName) {
		if (USE_SQL_DB) {
			String getUserSQL = String.join("\n", "Select username,password from  UserDatabase where username='%s';");
			getUserSQL = String.format(getUserSQL, userName);

			User user = null;
			try {
				Statement ps = dbConnection.createStatement();
				ResultSet rs = ps.executeQuery(getUserSQL);

				if (rs.next()) {
					String password = rs.getString("password");
					user = new User(userName, password);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				if (DEBUG)
					System.out.println("Failed in executing SQL(get user by name).");
			}
			return Optional.ofNullable(user);
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
		if (USE_SQL_DB) {
			String getUserSQL = String.join("\n", "SELECT username, password from UserDatabase");

			ArrayList<User> resultListOfUsers = new ArrayList<User>();
			try {
				Statement ps = dbConnection.createStatement();
				ResultSet rs = ps.executeQuery(getUserSQL);

				while (rs.next()) {
					String userName = rs.getString("username");
					String password = rs.getString("password");
					resultListOfUsers.add(new User(userName, password));

				}
			} catch (SQLException e) {
				e.printStackTrace();
				if (DEBUG)
					System.out.println("Failed in executing SQL(get all users).");
			}
			return resultListOfUsers;
		} else {
			return users;
		}
	}

	@Override
	public boolean save(User user) {
		if (USE_SQL_DB) {
			String UserAddSQL = "INSERT INTO UserDatabase (username, password) VALUES (?,?);";
			try {
				PreparedStatement ps = dbConnection.prepareStatement(UserAddSQL);
				ps.setString(1, user.getName());
				ps.setString(2, user.getPassword());
				int result = ps.executeUpdate();

				if (result == 1) {
					return true;
				} else {
					return false;
				}
			} catch (SQLIntegrityConstraintViolationException e) {
				if (DEBUG)
					System.out.println("User already exist in DB. ");
			} catch (SQLException e) {
				e.printStackTrace();
				if (DEBUG)
					System.out.println("Failed in adding mock Usersnb.");
			}
			return false;

		} else {
			users.add(user);
			return true;
		}
	}

	public boolean updateByUsername(String userName, String oldPassword,  String[] params) {
		if (USE_SQL_DB) {
			Optional<User> u = getByUserName(userName);

			if (!u.isPresent()) {
				if (DEBUG)
					System.out.println("User not present");
				return false;
			}
			
			String retreivedPassword = u.get().getPassword();
			if(!retreivedPassword.equals(oldPassword))	{
				return false;
			}

			String UserAddSQL = "UPDATE UserDatabase SET  password = ? where username = ?";
			try {
				PreparedStatement ps = dbConnection.prepareStatement(UserAddSQL);
				ps.setString(2, Objects.requireNonNull(params[0], "Name cannot be null"));
				ps.setString(1, Objects.requireNonNull(params[1], "password cannot be null"));
				int result = ps.executeUpdate();

				if (result == 1) {
					return true;
				} else {
					return false;
				}
			} catch (SQLIntegrityConstraintViolationException e) {
				if (DEBUG)
					System.out.println("User already exist in DB. ");
			} catch (SQLException e) {
				e.printStackTrace();
				if (DEBUG)
					System.out.println("Failed in adding mock Usersnb.");
			}

			return false;
		} else {
			Optional<User> u = getByUserName(userName);
			if (u.isPresent()) {
				// the user exists previously
				User user = u.get();
				try {
					user.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
					user.setPassword(Objects.requireNonNull(params[1], "password cannot be null"));
					return true;
				} catch (NullPointerException e) {
					return false;
				}
			} else {
				return false;
			}
		}

	}

	public boolean deleteByUsername(String userName, String password) {
		if (USE_SQL_DB) {
			Optional<User> u = getByUserName(userName);
			if (!u.isPresent()) {
				if (DEBUG)
					System.out.println("User not present");
				return false;
			}
			String retreivedPassword = u.get().getPassword();
			if(!retreivedPassword.equals(password))	{
				return false;
			}
			
			String UserAddSQL = "DELETE FROM UserDatabase WHERE username = ?;";
			try {
				PreparedStatement ps = dbConnection.prepareStatement(UserAddSQL);
				ps.setString(1, userName);
				int result = ps.executeUpdate();
				if (result == 1) {
					return true;
				} else {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				if (DEBUG)
					System.out.println("Failed in Deletion");
			}
			return false;
		} else {
			Optional<User> u = getByUserName(userName);
			if (u.isPresent()) {
				// the user exists previously
				User user = u.get();
				try {
					users.remove(user);

					return true;
				} catch (NullPointerException e) {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean update(User t, String[] params) {
		return updateByUsername(t.getName(), t.getPassword(),  params);
	}

	@Override
	public boolean delete(User t) {
		return deleteByUsername(t.getName(), t.getPassword());
	}
}