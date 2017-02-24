package main;

import dbconnect.DBConnect;
import menu.Menu;

public class Main {

	public static void main(String[] args) {
		Menu menu;
		try (DBConnect conn = DBConnect.newDBC()){
			menu = new Menu(conn);
			
			menu.SelectOption();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
