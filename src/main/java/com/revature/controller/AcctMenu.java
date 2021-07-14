package com.revature.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.daoimpl.AcctDaoImpl;
import com.revature.exceptions.MyCustomException;
import com.revature.models.Acct;
import com.revature.util.ConnectionUtil;

public class AcctMenu extends Input {
		double lastAmount;
		double balance;
		String id;
		private static Logger log = LoggerFactory.getLogger(AcctMenu.class);

		public AcctMenu() {
			super();
		}

		public AcctMenu(double lastAmount, double balance) {
			this.lastAmount = lastAmount;
			this.balance = balance;
		}

		public double deposit(double amount) {
			if (amount != 0) {
				balance += amount;
				lastAmount = amount;
			}
			return balance;
		}

		public double withdraw(double amount) {
			if (amount != 0) {
				balance -= amount;
				lastAmount = amount;
			}
			return balance;
		}

		public void accountMenuOptions(String username) throws SQLException, MyCustomException {
			
			ConnectionUtil cu = new ConnectionUtil();
			Connection connect = cu.getConnection();
			
			Statement stmt = connect.createStatement();
			
			ResultSet resultchecking = null;
			ResultSet resultsavings = null;
			
			AcctDaoImpl adi = new AcctDaoImpl();
			boolean accountActive = Input.checkActiveAccount(username);
			if(accountActive) {
				if (adi.getAccountsByID(username).isEmpty()) {
				System.out.println("Create an account with us \n ");
				createNewBankAccount();
				}
			}
			else 
			{
				System.out.println("Your account is not activated. Please wait until its activated.");
				System.exit(0);
			}

			System.out.println("Enter an account number to access it: ");
			Scanner key = new Scanner(System.in);
			int accountNumber = key.nextInt();
			Acct a = adi.selectAccount(accountNumber, username);
			id = a.getId();
			

			balance = adi.getBalance(accountNumber);

			int choice;

			double depositAmount = 0;
			double withdrawAmount = 0;

			System.out.println("Welcome to your Targon's Domain bank\n");
			System.out.println("input a number for each choice");
			System.out.println("0,) To transfer funds");
			System.out.println("1.) To view your account's balance");
			System.out.println("2.) To make a deposit to an account");
			System.out.println("3.) To make a withdraw from an account");
			System.out.println("4.) To create a new account");
			System.out.println("5.) To view all your accounts");
			System.out.println("6.) To select an account");
			System.out.println("7.) To delete an account");
			System.out.println("8.) To exit the app");

			do {

				System.out.println("your selection: ");
				choice = key.nextInt();

				switch (choice) {
				case 0:
					try(Connection conn = cu.getConnection())
					{
						System.out.println("Please pick a transfer option: \n"
										 + "1.) Checking to Savings \n"
										 + "2.) Savings to Checking");
						int transfer = key.nextInt();
						switch(transfer)
						{
						case 1:
							resultchecking = stmt.executeQuery("SELECT balance FROM accounts WHERE acct_number = "
									+ a.getAccountNumber() + "AND acct_type = 'checking';");
							resultsavings = stmt.executeQuery("SELECT balance FROM accounts WHERE acct_number = "
									+ a.getAccountNumber() + "AND acct_type = 'savings';");
							
							if(resultchecking.next() || resultsavings.next())
							{
								Double checkingbalance = resultchecking.getDouble(1);
								Double savingsbalance = resultsavings.getDouble(2);
								
								System.out.println("How much would you like to tranfer? You currently have $" 
								+ checkingbalance + " in your checking account.");
								double amount = key.nextDouble();
								while(amount > checkingbalance)
								{
									System.out.println("You can't take out more than what you currently have. Please try again.");
									amount = key.nextDouble();
								}
								
								stmt.executeUpdate("UPDATE accounts SET balance = " + (checkingbalance - amount) 
										+ " WHERE acct_number = " + a.getAccountNumber() + " AND acct_type = 'checking';");
								stmt.executeUpdate("UPDATE accounts SET balance = " + (savingsbalance + amount) 
										+ " WHERE acct_number = " + a.getAccountNumber() + " AND acct_type = 'savings';");
								ResultSet getBalance = stmt.executeQuery("SELECT balance FROM accounts WHERE "
										+ "acct_number = " + a.getAccountNumber() + " AND acct_type = 'checking';");
								while(getBalance.next())
								{
									Double currentBalance = getBalance.getDouble(1);
									System.out.println("You now have $" + currentBalance + " in your Checking account");
								}
									
							}
							break;
						case 2:
							resultchecking = stmt.executeQuery("SELECT balance FROM accounts WHERE acct_number = "
									+ a.getAccountNumber() + "AND acct_type = 'checking';");
							resultsavings = stmt.executeQuery("SELECT balance FROM accounts WHERE acct_number = "
									+ a.getAccountNumber() + "AND acct_type = 'savings';");
							
							if(resultchecking.next() || resultsavings.next())
							{
								Double checkingbalance = resultchecking.getDouble(1);
								Double savingsbalance = resultsavings.getDouble(2);
								
								System.out.println("How much would you like to tranfer? You currently have $" 
								+ savingsbalance + " in your checking account.");
								double amount = key.nextDouble();
								while(amount > savingsbalance)
								{
									System.out.println("You can't take out more than what you currently have. Please try again.");
									amount = key.nextDouble();
								}
								
								stmt.executeUpdate("UPDATE accounts SET balance = " + (savingsbalance - amount) 
										+ " WHERE acct_number = " + a.getAccountNumber() + " AND acct_type = 'savings';");
								stmt.executeUpdate("UPDATE accounts SET balance = " + (checkingbalance + amount) 
										+ " WHERE acct_number = " + a.getAccountNumber() + " AND acct_type = 'checking';");
								ResultSet getBalance = stmt.executeQuery("SELECT balance FROM accounts WHERE "
										+ "acct_number = " + a.getAccountNumber() + " AND acct_type = 'savings';");
								while(getBalance.next())
								{
									Double currentBalance = getBalance.getDouble(1);
									System.out.println("You now have $" + currentBalance + " in your Checking account");
								}
									
							}
							break;
						default:
							System.out.println("Invalid input, please try again.");
							break;

						}
						
					}
					catch(SQLException e)
					{
						e.printStackTrace();
						log.warn("Transaction failed.");
					}
				case 1:
					System.out.println("Your account's balance is: " + balance);
					break;
				case 2:
					System.out.println("Account number: " + a.getAccountNumber() + " balance: " + a.getBalance() + " type: "
							+ a.getType());
					System.out.println("Enter an amount to deposit: ");
					while (!(key.hasNextDouble())) {
						System.out.println("You must enter a numeric value, try again.");
						key.next();
					}
					depositAmount = key.nextDouble();
					if (depositAmount > 0) {

						a.setBalance(deposit(depositAmount));
						adi.updateAccount(a);

						System.out.println(
								"Your account number: " + a.getAccountNumber() + " new balance is: " + a.getBalance());
					} else {
						System.out.println("You attemtepted to deposit an invalid amount. Try again.");
					}
					break;
				case 3:
					System.out.println("Account number: " + a.getAccountNumber() + " balance: " + a.getBalance() + " type: "
							+ a.getType());
					System.out.println("Enter an amount to withdraw: ");

					while (!(key.hasNextDouble())) {
						System.out.println("Amount must be a valid number, try again.");
						key.next();
					}
					withdrawAmount = key.nextDouble();

					if (withdrawAmount < 0) {
						System.out.println("You entered a negative amount. Try again.");
					} else if (withdrawAmount > balance) {
						System.out.println("You attempted to withdraw more than your balance");

					} else {

						a.setBalance(withdraw(withdrawAmount));
						adi.updateAccount(a);

						System.out.println(
								"Your account number: " + a.getAccountNumber() + " new balance is: " + a.getBalance());
					}
					break;
				case 4:
					createNewBankAccount();
					break;
				case 5:
					System.out.println("These are your available accounts: \n");
					adi.getAccountsByID(username);
					break;
				case 6:
					System.out.println("enter an account number to select that account: ");

					int acct_number = key.nextInt();

					Acct account = adi.selectAccount(acct_number, username);

					adi.updateAccount(account);
					break;
				case 7:
					System.out.println("select an account to delete: ");
					int number = key.nextInt();
					Acct accountToDelete = adi.selectAccount(number, username);
					if (accountToDelete.getBalance() != 0) {
						System.out.println("Account must have a balance of 0 to be deleted.");
					} else {
						adi.deleteAccount(accountToDelete);
					}

					break;
				case 8:
					System.out.println("Thank you for visiting and using our app. See you soon!");
					System.exit(0);
					break;
				default:
					System.out.println("Please enter a valid number ranging from 1-7");
				}
			} while (choice != 8);
			Acct account = new Acct();
			account.setId(username);
			account.setBalance(balance);

			adi.updateAccount(account);
		}// closes method body
	
}

