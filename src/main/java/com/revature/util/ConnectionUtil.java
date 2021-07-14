package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
	private static ConnectionUtil cu = new ConnectionUtil();
	
	public ConnectionUtil() {
		super();
	}
	
	public static synchronized ConnectionUtil getInstance() {
		if(cu==null) {
			cu = new ConnectionUtil();		
		}
		return cu;	
	}
	
	public Connection getConnection() {
		Connection conn = null;
		Properties prop= new Properties();
		
		try {
			prop.load(new FileReader("dbconn.properties"));
			conn= DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"), prop.getProperty("password"));
			
		} catch (SQLException e) {
			System.out.println("failed to create connection");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
