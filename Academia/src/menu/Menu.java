package menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import dbconnect.DBConnect;
import tables.Customer;
import tables.OrderDetails;
import tables.Orders;

public class Menu {

	private DBConnect conn;

	public Menu() {
	}

	public Menu(DBConnect conn) {
		this.setConn(conn);
	}

	public DBConnect getConn() {
		return conn;
	}

	public void setConn(DBConnect conn) {
		this.conn = conn;
	}
	/**
	 *	Menu start point
	 */
	public void selectOption() {
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

			System.out.print("> ");
			option = input.nextInt();
			input.nextLine();

			switch (option) {
			case 1:
				registerNewClient(input);
				break;

			case 2:
				registerNewOrder(input);
				break;

			case 3:
				exportClients(input);
				break;

			case 4:
				exportCustomerDetails(input);
				break;

			case 5:
				importFile(input);
				break;
			}
		}
		input.close();
	}

	/**
	 *  [1] Menu entry for adding a user to the database
	 *  
	 *  @param input - Scanner where the user input will be read from
	 **/
	public void registerNewClient(Scanner input) {
		Customer newCustomer = new Customer(conn);
		ArrayList<String> attributes = newCustomer.getAttributes();

		System.out.print("Customer name: ");
		attributes.add(checkNull(input, "Customer name: "));
		System.out.print("Contact Last name: ");
		attributes.add(checkNull(input, "Contact Last name: "));
		System.out.print("Contact First name: ");
		attributes.add(checkNull(input, "Contact First name: "));
		System.out.print("Phone number: ");
		attributes.add(validatePhone(input, "Phone number: "));
		System.out.print("Address Line 1: ");
		attributes.add(checkNull(input, "Address Line 1: "));
		System.out.print("Address Line 2: ");
		attributes.add(input.nextLine());
		System.out.print("City: ");
		attributes.add(checkNull(input, "City: "));
		System.out.print("State: ");
		attributes.add(input.nextLine());

		System.out.print("Postal Code: ");
		attributes.add(validatePostalCode(input, "Postal Code: "));

		System.out.print("Country: ");
		attributes.add(checkNull(input, "Country: "));
		attributes.add("n/a");
		attributes.add("n/a");

		newCustomer.register();
		System.out.println("Registration with success.");
	}

	/** 
	 * [1.1] Verifies if a user input is empty
	 * 
	 * @param in - Scanner where the user input is to be read from
	 * @param promptText - String containing the text to be prompted to the user
	 * 
	 * @return A String with the user provided text
	 **/
	private String checkNull(Scanner in, String promptText) {
		String reset = in.nextLine();
		if (reset.equalsIgnoreCase("n/a") || reset.equals("")) {
			System.out.println("You must fill this field.");
			System.out.print(promptText);
			return checkNull(in, promptText);
		} else
			return reset;
	}

	/**  
	 * [1.2] Reads a phone number from user input and verifies if it is in the correct format
	 * 
	 * @param in - Scanner where the phone number will be read from
	 * @param promptText - String containing the text the user will be prompted
	 * 
	 * @return A String containing the phone number
	 **/
	private String validatePhone(Scanner in, String promptText) {
		String reset = in.nextLine();
		// A phone number should be in the portuguese mobile or land line phone format
		if (!reset.matches("(^9[1236]{1}[0-9]{7})") && !reset.matches("(^2[0-9]{8})")) {
			System.out.println("Invalid number.");
			System.out.print(promptText);
			return validatePhone(in, promptText);
		} else
			return reset;
	}

	/** 
	 * [1.3] Verifies that a postal code is in the proper format or is empty
	 * 
	 * @param in - Scanner object where the postal code will be read from
	 * @param promptText - String containing the text to be prompted to the user 
	 * 
	 * @return A String containing the properly formatted postal code
	 **/
	private String validatePostalCode(Scanner in, String promptText) {
		String reset = in.nextLine();
		// The input should be in the 0000-000 format
		// or empty
		if (!reset.matches("(^[0-9]{4}-[0-9]{3})") && !reset.equals("") && !reset.equalsIgnoreCase("n/a")) {
			System.out.println("Invalid Postal Code.");
			System.out.print(promptText);
			return validatePostalCode(in, promptText);
		} else
			return reset;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * [2] Adds a new order to the database read from user input
	 * 
	 * @param input - Scanner object where the order details should be read from
	 **/
	public void registerNewOrder(Scanner input) {

		Orders newOrder = new Orders(conn);
		ArrayList<String> attributes = newOrder.getAttributes();

		OrderDetails newOrderDetails = new OrderDetails(conn);
		ArrayList<String> attributesDetails = newOrderDetails.getAttributes();

		
		System.out.print("Order date: ");
		attributes.add(validateDateFormat(input, "Order date (AAAA-MM-DD): "));
		
		System.out.print("Required date: ");
		attributes.add(validateDateFormat(input, "Required date (AAAA-MM-DD): "));
		
		System.out.print("Shipped date: ");
		attributes.add(validateShippedDateFormat(input, "Shipped date (AAAA-MM-DD): "));
		
		System.out.print("Status: ");
		attributes.add(checkNull(input, "Status: "));
		
		System.out.print("Comments: ");
		attributes.add(input.nextLine());
		
		System.out.print("Customer Number: ");
		String customerNum = checkNull(input, "Customer Number: ");
		while (!conn.checkCustomerId(customerNum)) {
			System.out.println("Customer Number not found.");
			System.out.print("Customer Number: ");
			customerNum = checkNull(input, "Customer Number: ");
		}
		attributes.add(customerNum);

		System.out.print("Product code: ");
		String productCode = checkNull(input, "Product code: ");
		while (!newOrderDetails.checkProductCode(productCode)) {
			System.out.println("Product code not found.");
			System.out.print("Product code: ");
			productCode = checkNull(input, "Product code: ");
		}
		attributesDetails.add(productCode);

		System.out.print("Quantity: ");
		attributesDetails.add(checkNull(input, "Quantity: "));
		
		System.out.print("Unitary price: ");
		attributesDetails.add(checkNull(input, "Unitary price: "));
		
		System.out.print("Order line number: ");
		attributesDetails.add(checkNull(input, "Order line number: "));

		newOrder.register();
		attributesDetails.add(0, "" + newOrder.getOrderNumber());
		newOrderDetails.register();
		System.out.println("Registration with success.");
	}

	/** 
	 * [2.1] Verifies if a date provided by the user is in the proper format
	 * 
	 * @param in - Scanner where the date should be read from
	 * @param promptText - String containing the text to be prompted to the user
	 *  
	 *  @return A String containing a propper formated date
	**/
	private String validateDateFormat(Scanner in, String promptText) {
		String reset = in.nextLine();
		try {
			LocalDate.parse(reset);
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date");
			System.out.print(promptText);
			return validateDateFormat(in, promptText);
		}
		return reset;
	}

	/** 
	 * [2.2] Verifies if the shipped date provided is in a valid format or is empty 
	 *  
	 *  @param in - Scanner object where the date should be read from
	 *  @param promptText - String containing the user prompt text
	 *  
	 *  @return A String containing the date in the proper format
	**/
	private String validateShippedDateFormat(Scanner in, String promptText) {
		String reset = in.nextLine();
		
		if (!reset.equalsIgnoreCase("n/a") && !reset.equals("")) {
			return validateDateFormat(in, promptText);
		} else
			return reset;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * [3] Menu entry for customer table exporting
	 * 
	 * @param input - A Scanner object from which the name of the export file should be read
	 **/
	private void exportClients(Scanner input){
		Customer customer = new Customer(conn);
		System.out.print("Export file path: ");
		String path = input.nextLine();
		customer.exportCustomers(path);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * [4] Menu entry for customer details exporting
	 * 
	 * @param input - A Scanner object from which the customerNumber of the customer whose details are to be exported
	 **/
	private void exportCustomerDetails(Scanner input) {		
		System.out.print("Customer id: ");
		String in = input.nextLine();
		
		// Checks if the id provided exists in the databases
		// Will keep prompting the user until a correct id is given
		while(!conn.checkCustomerId(in)) {
			System.out.println("Invalid id.");
			System.out.print("Customer id: ");
			in = input.nextLine();
		}
		// Export the details<
		Customer customer = new Customer(conn);
		customer.exportCustomerDetails(in);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 
	 * [5] Menu entry for importing the contents of a binary file to the database
	 * 
	 * @param input - The Scanner object where the file's location should be read from
	**/
	private void importFile(Scanner input) {
		// Get the file path from the user
		System.out.print("File path: ");
		String path = input.nextLine();
		
		try {
			// Import the contents of the file
			conn.importFile(path);
		} catch (Exception e) {
			// Handle the exceptions from badly formatted files
			System.out.println(e.getMessage());
			importFile(input);
		}
	}
}
	


	




