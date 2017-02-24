package menu;

import java.io.FileNotFoundException;
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

	private Menu() {
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
				RegistNewOrder(input);
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

	/** [1] Método que regista novo cliente e atualiza a Database **/
	public void RegistNewClient(Scanner input) {

		Customer newCustomer = new Customer(conn);
		ArrayList<String> attributes = newCustomer.getAttributes();

		input.nextLine();
		System.out.print("Customer name: ");
		attributes.add(checkNull(input, "Customer name: "));
		System.out.print("Contact Last name: ");
		attributes.add(checkNull(input, "Contact Last name: "));
		System.out.print("Contact First name: ");
		attributes.add(checkNull(input, "Contact First name: "));
		System.out.print("Phone number: ");
		attributes.add(checkPhone(input, "Phone number: "));
		System.out.print("Address Line 1: ");
		attributes.add(checkNull(input, "Address Line 1: "));
		System.out.print("Address Line 2: ");
		attributes.add(input.nextLine());
		System.out.print("City: ");
		attributes.add(checkNull(input, "City: "));
		System.out.print("State: ");
		attributes.add(input.nextLine());

		System.out.print("Postal Code: ");
		attributes.add(checkPostalCode(input, "Postal Code: "));

		System.out.print("Country: ");
		attributes.add(checkNull(input, "Country: "));
		attributes.add("n/a");
		attributes.add("n/a");

		newCustomer.register();
		System.out.println("Registration with success.");
	}

	/** [1.1] Método que confirma o preenchimento dos campos obrigatórios **/

	private String checkNull(Scanner in, String field) {
		String reset = in.nextLine();
		if (reset.equalsIgnoreCase("n/a") || reset.equals("")) {
			System.out.println("You must fill this field.");
			System.out.print(field);
			return checkNull(in, field);
		} else
			return reset;
	}

	/** [1.2] Método que confirma a validade do número de telefone **/
	private String checkPhone(Scanner in, String field) {
		String reset = in.nextLine();
		if (!reset.matches("(^9[1236]{1}[0-9]{7})") && !reset.matches("(^2[0-9]{8})")) {
			System.out.println("Invalid number.");
			System.out.print(field);
			return checkPhone(in, field);
		} else
			return reset;
	}

	/** [1.3] Método que confirma a validade do código postal **/
	private String checkPostalCode(Scanner in, String field) {
		String reset = in.nextLine();
		if (!reset.matches("(^[0-9]{4}-[0-9]{3})") && !reset.equals("") && !reset.equalsIgnoreCase("n/a")) {
			System.out.println("Invalid Postal Code.");
			System.out.print(field);
			return checkPostalCode(in, field);
		} else
			return reset;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * [2] Método que regista nova encomenda de um cliente já existente na
	 * Database e a atualiza
	 **/

	public void RegistNewOrder(Scanner input) {

		Orders newOrder = new Orders();
		ArrayList<String> attributes = newOrder.getAttributes();

		OrderDetails newOrderDetails = new OrderDetails();
		ArrayList<String> attributesDetails = newOrderDetails.getAttributes();

		input.nextLine();
		System.out.print("Order date: ");
		attributes.add(checkDateFormat(input, "Order date: "));
		System.out.print("Required date: ");
		attributes.add(checkDateFormat(input, "Required date: "));
		System.out.print("Shipped date: ");
		attributes.add(checkShipDateFormat(input, "Shipped date: "));
		System.out.print("Status: ");
		attributes.add(checkNull(input, "Status: "));
		System.out.print("Comments: ");
		attributes.add(input.nextLine());
		System.out.print("Customer Number: ");
		attributes.add(checkNull(input, "Customer Number: "));

		System.out.print("Product code: ");
		String productCode = checkNull(input, "Product code: ");
		while (!newOrderDetails.checkProductCode(productCode)) {
			System.out.println("Product code not found.");
			System.out.println("Product code: ");
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
		attributesDetails.add(0, newOrder.getOrderNumber());
		newOrderDetails.register();
		System.out.println("Registration with success.");
	}

	/** [2.1] Método que valida o formato da orderDate e requiredDate **/

	private String checkDateFormat(Scanner in, String field) {

		String reset = in.nextLine();
		try {
			LocalDate date = LocalDate.parse(reset);
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date");
			return checkDateFormat(in, field);
		}
		return reset;
	}

	/** [2.2] Método que valida o formato da shippedDate, que poder ser NULL **/

	private String checkShipDateFormat(Scanner in, String field) {

		String reset = in.nextLine();
		if (!reset.equalsIgnoreCase("n/a") || reset.equals("")) {
			return checkDateFormat(in, field);
		} else
			return reset;
	}
	
	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * [3] Método que exporta a lista de clientes registados para um ficheiro
	 * binário
	 **/

	public void ExportListOfClients(Customer customerName) throws FileNotFoundException {

		// Blob blob = rs.getBlob(customerName);
		// InputStream in = blob.getBinaryStream();
		// OutputStream out = new FileOutputStream("listClients.bin");
		// byte[] buff = blob.getBytes(1,(int)blob.getLength());
		// out.write(buff);
		// out.close();

		// FileOutputStream fos = new FileOutputStream();
		// fos.write();
		// ObjectOutputStream oos = new ObjectOutputStream(fos);
		// Blob blob = result.getBlob("photo");
		// InputStream inputStream = blob.getBinaryStream();
		// OutputStream outputStream = new FileOutputStream(filePath);

	}
}
