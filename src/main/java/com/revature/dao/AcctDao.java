package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.models.Acct;


public interface AcctDao {

	// public void insertAccount(Account account, User user) throws SQLException;

	public List<Acct> getAllAccounts() throws SQLException;

	public List<Acct> getAccountsByID(String acct_id) throws SQLException;

	public Acct selectAccount(int acct_number, String username) throws SQLException;
	
	public int createAccount(Acct account) throws SQLException;

	public void updateAccount(Acct account) throws SQLException;

	public int deleteAccount(Acct account) throws SQLException;
	
	public double getBalance(int accountNumber) throws SQLException;
	
	public boolean updateActive(boolean active) throws SQLException;

}
