package com.revature.Driver;

import java.sql.SQLException;

import com.revature.controller.DisplayMenu;

public class Driver 
{



	public static void main(String[] args) throws SQLException 
	{
		DisplayMenu dm = new DisplayMenu();
		printHeader();
		dm.inMenu();
	
	}
	
	private static void printHeader() {
		System.out.println("$=================================================$");
		System.out.println("|           Welcome To New RuneTerra One          |");
		System.out.println("|             Targon's Domain BankApp             |");
		System.out.println("$=================================================$");

	}
	
}
