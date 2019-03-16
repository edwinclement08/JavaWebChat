package com.javapapers.webservices.rest.jersey.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
	private static DatabaseConnection _instance;

    private PreparedStatement preStatement;
    private Statement statement;
    private ResultSet result;
    private Connection con;
	
    
    private String jdbcDriver = "com.mysql.jdbc.Driver";
    private String dbAddress = "jdbc:mysql://localhost:3306/chatdb?createDatabaseIfNotExist=true";
    private String userName = "edwin";
    private String password = "passwordalpha9";
    
	private DatabaseConnection() {
		try {
			Class.forName(jdbcDriver);
	        con = DriverManager.getConnection(dbAddress, userName, password);

			
			String myTableName = "CREATE TABLE IF NOT EXISTS UserDatabase (" 
			            + "idNo INT(64) NOT NULL AUTO_INCREMENT,"  
			            + "username VARCHAR(30),"
			            + "password VARCHAR(30),"
			            + "PRIMARY KEY(idNo))";  
			   
	        statement = con.createStatement();
	        
	        
	        statement.executeUpdate(myTableName);
	        System.out.println("Table Created");
	 

			con.close();
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e);
		}
	}
	
	public void createNecessaryTables()	{
		
	}
	public static DatabaseConnection getDatabaseConnection() {
		if (_instance == null) {
			_instance = new DatabaseConnection();
		}

		return _instance;
	}
}
