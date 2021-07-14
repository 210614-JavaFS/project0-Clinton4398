package com.revature.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.revature.daoimpl.AcctDaoImpl;
import com.revature.daoimpl.UserDaoImpl;
import com.revature.exceptions.MyCustomException;
import com.revature.models.Acct;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

//creates bank account for users
public class Input extends UserDaoImpl {

	public Input() {
		super();
	}
	static UserDaoImpl udi = new UserDaoImpl();

	static Scanner key = new Scanner(System.in);

	static User user = new User();

	public static void createUserAccount() throws SQLException {
		String username;
		String password;
		String firstName;
		String lastName;

		System.out.println("Enter a userame you want to use: ");
		
		username = key.nextLine();
		
		while (udi.doesUsernameMatch(username)) {
			System.out.println("username already in use, try again: ");
			username = key.nextLine();
		}
		user.setUsername(username);
		
		System.out.println("Enter your secret password: ");
		password = key.nextLine();
		user.setPassword(password);
		
		System.out.println("Enter your first name: ");
		firstName = key.nextLine();
		user.setFirstname(firstName);
		System.out.println("Enter your last name: ");
		lastName = key.nextLine();
		user.setFirstname(lastName);

		createUser(user);
	}

	public static void userLogin() throws SQLException, MyCustomException {

		System.out.println("Enter your username: ");
		String username = key.nextLine();
		
		while (!udi.doesUsernameMatch(username)) {
			System.out.println("Username not found, try again: ");
			username = key.nextLine();
		}
		
		System.out.println("Enter your password: ");
		String password = key.nextLine();
		
		while (!udi.doesPasswordMatch(password, username)) {
			System.out.println("Password does not match, try again: ");
			password = key.nextLine();
		}
		
		AcctMenu acctMenu = new AcctMenu();
		acctMenu.accountMenuOptions(username);

	}
	public static boolean checkActiveAccount(String username) throws SQLException{
		ResultSet rs = null;
		ConnectionUtil cul = new ConnectionUtil();
		Connection connt = cul.getConnection();
		Statement stmt = connt.createStatement();
		
		rs = stmt.executeQuery("SELECT * FROM users WHERE username = " + udi.getUsersByUsername(username));
		
		User activeUser = new User();
		
		boolean activated = false;
		if(rs.next()) {
			username = activeUser.getUsername();
			activated = rs.getBoolean(5);
			
		}
		
		return activated;
	}


	//creates a new account after the user logs in or registers
	public void createNewBankAccount() throws SQLException {
		String username;
		double balance;
		String type;

		System.out.println("Enter your username: ");
		username = key.nextLine();

		System.out.println("enter the type of account \"checking\" or \"savings\": ");
		type = key.nextLine();

		System.out.println("Select an amount to deposit: ");
		balance = key.nextDouble();

		Acct account = new Acct();

		account.setId(username);
		account.setBalance(balance);
		account.setType(type);

		AcctDaoImpl adi = new AcctDaoImpl();
		adi.createAccount(account);

		AcctMenu acctMenu = new AcctMenu();
		try {
			acctMenu.accountMenuOptions(username);
		} catch (MyCustomException e) {
					e.printStackTrace();
		}

	}
}
