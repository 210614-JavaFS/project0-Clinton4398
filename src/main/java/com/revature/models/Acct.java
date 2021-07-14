package com.revature.models;

public class Acct {
	static int accountNumber;
	private String id;
	private double balance;
	private String type;
	protected boolean active;

	public Acct() {
		super();
	}

	public Acct(int accountNumber, String id, double balance, String type) {
		super();
		Acct.accountNumber = accountNumber;
		this.id = id;
		this.balance = balance;
		this.type = type;
	}
	public static int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		Acct.accountNumber = accountNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountNumber;
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Acct other = (Acct) obj;
		if (accountNumber != Acct.accountNumber)
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", id=" + id + ", balance=" + balance + ", type=" + type
				+ "]";
	}

	public void deposit(double amount) {
		// TODO Auto-generated method stub
		
	}

	
}
