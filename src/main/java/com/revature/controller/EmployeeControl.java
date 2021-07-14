package com.revature.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.daoimpl.UserDaoImpl;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class EmployeeControl {
	
	public static ConnectionUtil cu =  ConnectionUtil.getInstance();
	private static Logger log = LoggerFactory.getLogger(EmployeeControl.class);

	public static void employeeSession(Scanner key) throws SQLException {
		// generate date and current time
		ResultSet rs = null;
		ConnectionUtil cul = new ConnectionUtil();
		Connection connt = cul.getConnection();
		Statement stmt = connt.createStatement();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		
		UserDaoImpl udi = new UserDaoImpl();
		
		System.out.println("Welcome Employee " + formatter.format(date));

		String empeeUsername, empeePassword;
		
		System.out.println("Enter your username: ");
		empeeUsername = key.nextLine();

		Properties pro = new Properties();
		String adminChoice;
		try 
		{
			pro.load(new FileReader("empeelogin.properties"));
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		if (empeeUsername.equals(pro.getProperty("empeeUsername")))
		{
			System.out.println("Enter your password: ");

			empeePassword = key.nextLine();

			if (empeePassword.equals(pro.getProperty("empeePassword"))) 
			{
				System.out.println("welcome Employee: ");
			} 
			else 
			{
				System.out.println("Wrong passowrd, try again.");
			}
			} 
		else 
		{
			System.out.println("Incorrect username, try again");
		}

		System.out.println("1.) View users");
		System.out.println("2.) Approve/Deny users");
		System.out.println("0.) Log Out");

		do 
		{
			System.out.println("enter a choice: ");
			adminChoice = key.nextLine();

			switch (adminChoice) 
			{

				case "1":
					try {
						for (int i = 0; i < udi.getUsers().size(); i++) {
							System.out.println(udi.getUsers().get(i));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				case "2":
					System.out.println("What is the username of the account you'd like to activate/deactivate.");
					String username = key.nextLine();
					
					rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
					if(rs.next()) 
					{
						System.out.println("Thank you. \n");
					}
					else 
					{
					System.out.println("Sorry that was username doesn't match anything in our system. Please try again.");
					username = key.nextLine();
					}
					
					System.out.println("Would you like to: \n"
							+ "1) Activate the account \n"
							+ "2) Deactivate the account.");
					String choice = key.nextLine();
					switch(choice) {
					case "1":
						try(Connection conn = cul.getConnection())
						{
							stmt.executeUpdate("UPDATE users SET active = TRUE WHERE username = '" + username +"';");
						}
						catch(SQLException e) 
						{
							e.printStackTrace();
							log.warn("Activation action failed.");
						}
						break;
					case "2":
						try(Connection conn = cul.getConnection())
						{
							stmt.executeUpdate("UPDATE users SET active = FALSE WHERE username = '" + username +"';");
						}
						catch(SQLException e) 
						{
							e.printStackTrace();
							log.warn("Deactivation action failed.");
						}
						break;
					default:
						System.out.println("Invalid input, please try again.");
						break;
					}
					break;
					
				case "0":
					System.out.println("Thank you for working on behalf of us.");
					System.exit(0);
					break;
				}
			} 
		while (adminChoice != "0");

		}
}

