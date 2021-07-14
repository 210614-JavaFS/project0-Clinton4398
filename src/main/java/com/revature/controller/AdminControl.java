package com.revature.controller;

import java.util.Date;

import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.daoimpl.UserDaoImpl;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class AdminControl { //check the activate account part maybe need to comment it out
	
	public static ConnectionUtil cu =  ConnectionUtil.getInstance();
	
	private static Logger log = LoggerFactory.getLogger(AdminControl.class);


	public static void adminSession(Scanner key) throws SQLException {
		// generate date and current time
		ResultSet rs = null;
		ConnectionUtil cul = new ConnectionUtil();
		Connection connt = cul.getConnection();
		Statement stmt = connt.createStatement();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		
		UserDaoImpl udi = new UserDaoImpl();
		
		System.out.println("Welcome Admin " + formatter.format(date));

		String adminUsername, adminPassword;
		
		System.out.println("Enter your username: ");
		adminUsername = key.nextLine();

		Properties pro = new Properties();
		int adminChoice;
		try 
		{
			pro.load(new FileReader("adminlogin.properties"));
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		if (adminUsername.equals(pro.getProperty("adminUsername")))
		{
			System.out.println("Enter your password: ");

			adminPassword = key.nextLine();

			if (adminPassword.equals(pro.getProperty("adminPassword"))) 
			{
				System.out.println("welcome Admin: ");
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

		System.out.println("1.) to view users");
		System.out.println("2.) Approve/Deny users");
		System.out.println("3.) to create a new user");
		System.out.println("4.) to delete users");
		System.out.println("5.) to update users");
		System.out.println("0.)  to Log Out");

		do 
		{
			System.out.println("enter a choice: ");
			adminChoice = key.nextInt();

			switch (adminChoice) 
			{

				case 1:
					try {
						for (int i = 0; i < udi.getUsers().size(); i++) {
							System.out.println(udi.getUsers().get(i));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				case 2:
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

				case 3:
					Scanner input = new Scanner(System.in);
					String user_username, user_password, firstName, lastName;

					System.out.println("enter a username: ");
					user_username = input.nextLine();

					System.out.println("Enter your secret password: ");
					user_password = input.nextLine();

					System.out.println("Enter your first name: ");
					firstName = input.nextLine();

					System.out.println("Enter your last name: ");
					lastName = input.nextLine();

					User user = new User();
					user.setUsername(user_username);
					user.setPassword(user_password);
					user.setFirstname(firstName);
					user.setFirstname(lastName);

					udi.createUser(user);

					System.out.println(udi.getUsers());

					break;

				case 4:
					System.out.println("Enter the username for the account you wish to delete: ");
					String usernameToDelete = key.nextLine();
					User u = udi.getUsersByUsername(usernameToDelete);
					udi.deleteUsers(u);
					System.out.println("User deleted");
					break;

				case 5:
					System.out.println("enter username for account you wish to update: ");
					String usernameAcctUpdate = key.nextLine();
					System.out.println(
							"1.) to update username \n 2.) to update password \n 3.) to update first name \n 4.) to update last name:");
					int updateChoice = key.nextInt();

					if (updateChoice == 1) {
						System.out.println("enter a new username: ");
						String newUsername = key.nextLine();
						u = udi.getUsersByUsername(usernameAcctUpdate);
						udi.updateUsername(newUsername, usernameAcctUpdate);
						System.out.println(u.toString());
						
					} else if (updateChoice == 2) {
						System.out.println(udi.getUsers().toString() + "\n");
						System.out.println("enter the username of the user: ");
						String uname = key.nextLine();
						u = udi.getUsersByUsername(uname);
						System.out.println("enter a new password: ");
						String newPassword = key.nextLine();
						udi.updatePassword(newPassword, uname);
						System.out.println(u.toString());
						
					} else if (updateChoice == 3) {
						System.out.println(udi.getUsers().toString() + "\n");
						System.out.println("enter the username of an user to update: ");
						String uname = key.nextLine();
						u = udi.getUsersByUsername(uname);
						System.out.println("enter a new first name for the user: ");
						String newFirstName = key.nextLine();
						udi.updateFirstName(newFirstName, uname);
						System.out.println(u.toString());

					} else if (updateChoice == 4) {
						System.out.println(udi.getUsers().toString() + "\n");
						System.out.println("enter the username of an user to update: ");
						String uname = key.nextLine();
						u = udi.getUsersByUsername(uname);
						System.out.println("enter a new last name for the user: ");
						String newLastName = key.nextLine();
						udi.updateFirstName(newLastName, uname);

					}
					break;

				case 0:
					System.out.println("Thank you for working on behalf of us.");
					break;
				}
			} 
		while (adminChoice != 0);

		}
}
