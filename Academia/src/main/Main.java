package main;

import dbconnect.DBConnect;
import menu.Menu;

public class Main {

	public static void main(String[] args) {
		DBConnect conn = new DBConnect();
		Menu menu = new Menu(conn);
		
		menu.SelectOption();
	}

}
