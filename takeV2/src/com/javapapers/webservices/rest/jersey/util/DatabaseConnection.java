package com.javapapers.webservices.rest.jersey.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static DatabaseConnection _instance;
	private Connection con;

	private String jdbcDriver = "com.mysql.jdbc.Driver";
	private String dbAddress = "jdbc:mysql://localhost:3306/chatdb2?createDatabaseIfNotExist=true";
	private String userName = "root";
	private String password = "passwordalpha9";

	private DatabaseConnection() {
		try {
			Class.forName(jdbcDriver);
			con = DriverManager.getConnection(dbAddress, userName, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("jdbc driver not found, should not happen");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DB Connection Failure");
		}
	}

	public Connection getConnection() {
		return con;
	}
	
	public static DatabaseConnection getDatabase() {
		if (_instance == null) {
			_instance = new DatabaseConnection();
		}
		return _instance;
	}
}
