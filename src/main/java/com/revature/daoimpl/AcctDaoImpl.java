package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.AcctDao;
import com.revature.models.Acct;
import com.revature.util.ConnectionUtil;

public class AcctDaoImpl implements AcctDao {
	public static ConnectionUtil cu = ConnectionUtil.getInstance();

	public List<Acct> getAllAccounts() throws SQLException {
		List<Acct> accountsList = new ArrayList<Acct>();

		Connection conn = cu.getConnection();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");

		Acct a = null;
		while (rs.next()) {
			a = new Acct(rs.getInt(1), rs.getString(1), rs.getDouble(2), rs.getString(3));
			accountsList.add(a);
		}
		return accountsList;
	}

	public List<Acct> getAccountsByID(String acct_id) throws SQLException {
		List<Acct> accountList = new ArrayList<Acct>();
		Connection conn = cu.getConnection();

		String sql = "SELECT * FROM accounts WHERE acct_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, acct_id);
		ResultSet rs = ps.executeQuery();

		Acct a = null;
		while (rs.next()) {
			a = new Acct(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
			accountList.add(a);

		}
		System.out.println(accountList + "\n");
		return accountList;

	}


	public int createAccount(Acct account) throws SQLException {
		// if executeUpdate work returns 1 that we assign to this
		int accountsCreated = 0;
		Connection conn = cu.getConnection();
		String sql = "INSERT INTO accounts(acct_number, acct_id, balance, acct_type) VALUES (nextval (\'acctSeq\'), ?, ?, ?)";

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setString(1, account.getId());
		ps.setDouble(2, account.getBalance());
		ps.setString(3, account.getType());

		accountsCreated = ps.executeUpdate();
		ps = conn.prepareStatement("commit");
		ps.execute();

		return accountsCreated;

	}

	public void updateAccount(Acct account) throws SQLException {
		Connection conn = cu.getConnection();

		String sql = "UPDATE accounts SET balance = ? WHERE acct_number = ?";

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setDouble(1, account.getBalance());
		ps.setInt(2, account.getAccountNumber());

		ps.execute();
		ps = conn.prepareStatement("commit");
		ps.execute();

	}

	public int deleteAccount(Acct account) throws SQLException {
		int accountsDeleted = 0;
		Connection conn = cu.getConnection();

		String sql = "DELETE FROM accounts WHERE acct_id = ? AND acct_number = ?";
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setString(1, account.getId());
		ps.setInt(2, account.getAccountNumber());

		accountsDeleted = ps.executeUpdate();
		ps = conn.prepareStatement("commit");
		ps.execute();

		return accountsDeleted;

	}

	public double getBalance(int accountNumber) throws SQLException {
		double balance = 0;
		ResultSet rs = null;

		String sql = "SELECT * FROM accounts WHERE acct_number = ?";

		Connection conn = cu.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, accountNumber);
		rs = ps.executeQuery();

		while (rs.next()) {
			balance = rs.getDouble("balance");
		}
		return balance;

	}

	@Override
	public Acct selectAccount(int acct_number, String username) throws SQLException {
			Connection conn = cu.getConnection();

			String sql = "SELECT * FROM accounts WHERE acct_id = ? AND acct_number = ?";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, username);
			ps.setInt(2, acct_number);

			ResultSet rs = ps.executeQuery();

			Acct a = null;

			while (rs.next()) {
				a = new Acct(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
				 System.out.println(a);
			}

			return a;

		}

	@Override
	public boolean updateActive(boolean active) throws SQLException {
	
		return active;
	}

}
