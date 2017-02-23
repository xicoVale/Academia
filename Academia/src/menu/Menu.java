package menu;

import java.util.ArrayList;
import java.util.Scanner;

import dbconnect.DBConnect;
import tables.Customer;

public class Menu {

	private DBConnect conn;

	private Menu() {}
	
	public Menu(DBConnect conn) {
		this.setConn(conn);
	}

	public DBConnect getConn() {
		return conn;
	}

	public void setConn(DBConnect conn) {
		this.conn = conn;
	}

	public void SelectOption() {

		Scanner input = new Scanner(System.in);
		int option;
		{
			System.out.println("   Please select an option:");
			System.out.println("-------------------------------");
			System.out.println("[1] New client registration");
			System.out.println("[2] New order");
			System.out.println("[3] Export the list of clients to a file");
			System.out.println("[4] Export the orders of a client to a text file");
			System.out.println("[5] Import file to Database (binary file)");

			option = input.nextInt();

			switch (option) {

			case 1:
				RegistNewClient(input);
				break;

			case 2:
				break;

			case 3:
				break;

			case 4:
				break;

			case 5:
				break;
			}
		}
		input.close();
	}

	public void RegistNewClient(Scanner input) {

		Customer newCustomer = new Customer(conn);
		ArrayList<String> attributes = newCustomer.getAttributes();

		input.nextLine();
		System.out.print("Customer name: ");
		attributes.add(input.nextLine());
		System.out.print("Contact Last name: ");
		attributes.add(input.nextLine());
		System.out.print("Contact First name: ");
		attributes.add(input.nextLine());
		System.out.print("Phone number: ");
		attributes.add(input.nextLine());
		System.out.print("Address Line 1: ");
		attributes.add(input.nextLine());
		System.out.print("Address Line 2: ");
		attributes.add(input.nextLine());
		System.out.print("City: ");
		attributes.add(input.nextLine());
		System.out.print("State: ");
		attributes.add(input.nextLine());
		System.out.print("Postal Code: ");
		attributes.add(input.nextLine());
		System.out.print("Country: ");
		attributes.add(input.nextLine());
		attributes.add("n/a");
		attributes.add("n/a");

		newCustomer.register();

	}

}
