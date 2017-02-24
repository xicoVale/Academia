package main;

import dbconnect.DBConnect;
import menu.Menu;

public class Main {

	public static void main(String[] args) {
		DBConnect conn;
		Menu menu;
		try {
			conn = DBConnect.newDBC();
			menu = new Menu(conn);
			
			menu.SelectOption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
