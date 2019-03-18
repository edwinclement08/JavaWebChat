package com.javapapers.webservices.rest.jersey.dao;

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

import com.javapapers.webservices.rest.jersey.models.User;
import com.javapapers.webservices.rest.jersey.util.DatabaseConnection;

public class UserDao implements Dao<User> {
	private static UserDao _instance = null;
	private boolean USE_SQL_DB = true;
	private boolean DEBUG = true;
	private ArrayList<User> users = new ArrayList<>();

	private DatabaseConnection db;
	private Connection dbConnection;

	private void debugPrint(Object message) {
		String className = this.getClass().getSimpleName();
		String methodName = new Throwable().getStackTrace()[1].getMethodName();
		if (DEBUG)
			System.out.println(String.format("|%s|%s| :: %s", className, methodName, message.toString()));
	}

	public static UserDao getInstance() {
		if (_instance == null) {
			_instance = new UserDao();
		}
		return _instance;
	}

	private UserDao() {
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
							+ "token VARCHAR(30)," + "tokenCreationTime INT, " + "password VARCHAR(30),"
							+ "PRIMARY KEY(idNo))";
					Statement statement;
					statement = dbConnection.createStatement();
					statement.executeUpdate(myTableName);
					debugPrint("Table Created if not existed");
				} catch (SQLException e) {
					e.printStackTrace();
					debugPrint("Failed in Table Creation.");
				}
			}

			// create a few dummy values
			User u1 = new User("John", "pass123");
			User u2 = new User("Susan", "suzy");
//			debugPrint(u1);
//			debugPrint(u2);
			save(u1);
			save(u2);
		}

		if (!USE_SQL_DB) {
			users.add(new User("John", "pass123"));
			users.add(new User("Susan", "suzy"));
		}

		debugPrint("Initialization Complete");

	}

	@Override
	public Optional<User> get(long id) {
		if (USE_SQL_DB) {
			String getUserSQL = String.join("\n",
					"Select username,password,token,tokenCreationTime from  UserDatabase where idNo=%d;");
			getUserSQL = String.format(getUserSQL, id);

			User user = null;
			try {
				Statement ps = dbConnection.createStatement();
				ResultSet rs = ps.executeQuery(getUserSQL);

				if (rs.next()) {
					String userName = rs.getString("username");
					String password = rs.getString("password");
					String token = rs.getString("token");
					long tokenCreationTime = rs.getLong("tokenCreationTime");

					user = new User(userName, password, token, tokenCreationTime);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				debugPrint("Failed in executing SQL(get user by id).");
			}
			return Optional.ofNullable(user);
		} else {
			return Optional.ofNullable(users.get((int) id));
		}
	}

	public Optional<User> getByUserName(String userName) {
		if (USE_SQL_DB) {
			String getUserSQL = String.join("\n",
					"Select username,password,token,tokenCreationTime from  UserDatabase where username='%s';");
			getUserSQL = String.format(getUserSQL, userName);

			User user = null;
			try {
				Statement ps = dbConnection.createStatement();
				ResultSet rs = ps.executeQuery(getUserSQL);

				if (rs.next()) {

					String password = rs.getString("password");
					String token = rs.getString("token");
					long tokenCreationTime = rs.getLong("tokenCreationTime");

					user = new User(userName, password, token, tokenCreationTime);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				debugPrint("Failed in executing SQL(get user by name).");
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

	public Optional<User> getByUserOnject(User user) {
		return getByUserName(user.getName());
	}

	@Override
	public List<User> getAll() {
		if (USE_SQL_DB) {
			String getUserSQL = String.join("\n",
					"SELECT username, password,token,tokenCreationTime from UserDatabase");

			ArrayList<User> resultListOfUsers = new ArrayList<User>();
			try {
				Statement ps = dbConnection.createStatement();
				ResultSet rs = ps.executeQuery(getUserSQL);

				while (rs.next()) {
					String userName = rs.getString("username");
					String password = rs.getString("password");
					String token = rs.getString("token");
					long tokenCreationTime = rs.getLong("tokenCreationTime");

					User user = new User(userName, password, token, tokenCreationTime);
					resultListOfUsers.add(user);

				}
			} catch (SQLException e) {
				e.printStackTrace();
				debugPrint("Failed in executing SQL(get all users).");
			}
			return resultListOfUsers;
		} else {
			return users;
		}
	}

	@Override
	public boolean save(User user) {
		if (USE_SQL_DB) {

			// check if user already exists
			if (getByUserOnject(user).isPresent()) {
				// yes there is, use update
				debugPrint("update user: " + user);

				String UserAddSQL = "UPDATE UserDatabase SET  password = ?, token = ?, tokenCreationTime = ? where username = ?";
				try {
					PreparedStatement ps = dbConnection.prepareStatement(UserAddSQL);
					try {

						ps.setString(1, user.getPassword());
						ps.setString(2, user.getToken());
						ps.setLong(3, user.gettokenCreationTime());
						ps.setString(4, user.getName());
						int result = ps.executeUpdate();
						if (result == 1) {
							debugPrint("updated successfully");
							return true;
						} else {
							debugPrint("sql messed up");
							return false;
						}
					} catch (NullPointerException e) {
						debugPrint("save method is not given a complete User Object.");
						return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					debugPrint("Some Error in updating user record");
					return false;
				}
			} else {
				// create a record
				String UserAddSQL = "INSERT INTO UserDatabase (username, password,token,tokenCreationTime) VALUES (?,?,?,?);";
				try {
					PreparedStatement ps = dbConnection.prepareStatement(UserAddSQL);
					ps.setString(1, user.getName());
					ps.setString(2, user.getPassword());
					ps.setString(3, user.getToken());
					ps.setLong(4, user.gettokenCreationTime());

					int result = ps.executeUpdate();

					if (result == 1) {
						return true;
					} else {
						return false;
					}
				} catch (SQLIntegrityConstraintViolationException e) {
					debugPrint("User already exist in DB. ");
				} catch (SQLException e) {
					e.printStackTrace();
					debugPrint("Failed in adding mock Usersnb.");
				}
				return false;
			}
		} else {
			users.add(user);
			return true;
		}
	}

	public boolean updateByUsername(String userName, String oldPassword, String[] params) {
		if (USE_SQL_DB) {
			Optional<User> u = getByUserName(userName);

			if (!u.isPresent()) {
				debugPrint("User not present");
				return false;
			}

			String retreivedPassword = u.get().getPassword();
			if (!retreivedPassword.equals(oldPassword)) {
				return false;
			}

			String UserAddSQL = "UPDATE UserDatabase SET  password = ? where username = ?";
			try {
				PreparedStatement ps = dbConnection.prepareStatement(UserAddSQL);
				try {
					ps.setString(2, Objects.requireNonNull(params[0], "Name cannot be null"));
					ps.setString(1, Objects.requireNonNull(params[1], "password cannot be null"));
				} catch (NullPointerException e) {
					return false;
				}
				int result = ps.executeUpdate();

				if (result == 1) {
					return true;
				} else {
					return false;
				}
			} catch (SQLIntegrityConstraintViolationException e) {
				debugPrint("User already exist in DB. ");
			} catch (SQLException e) {
				e.printStackTrace();
				debugPrint("Failed in adding mock Usersnb.");
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
				debugPrint("User not present");
				return false;
			}
			String retreivedPassword = u.get().getPassword();
			if (!retreivedPassword.equals(password)) {
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
				debugPrint("Failed in Deletion");
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
		return updateByUsername(t.getName(), t.getPassword(), params);
	}

	@Override
	public boolean delete(User t) {
		return deleteByUsername(t.getName(), t.getPassword());
	}
}