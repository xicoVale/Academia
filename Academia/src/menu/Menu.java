package menu;

import java.util.ArrayList;
import java.util.Scanner;

import tables.Customer;

public class Menu {

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

		Customer newCustomer = new Customer();
		ArrayList<String> attributes = newCustomer.getAttributes();
		
		System.out.println("Customer name: ");
		attributes.add(input.nextLine());
		System.out.println("Contact Last name: ");
		attributes.add(input.nextLine());
		System.out.println("Contact First name: ");
		attributes.add(input.nextLine());
		System.out.println("Phone number: ");
		attributes.add(input.nextLine());
		System.out.println("Address Line 1: ");
		attributes.add(input.nextLine());
		System.out.println("Address Line 2: ");
		attributes.add(input.nextLine());
		System.out.println("City: ");
		attributes.add(input.nextLine());
		System.out.println("State: ");
		attributes.add(input.nextLine());
		System.out.println("Postal Code: ");
		attributes.add(input.nextLine());
		System.out.println("Country: ");
		attributes.add(input.nextLine());
		attributes.add("n/a");
		attributes.add("n/a");
		
	}

}
