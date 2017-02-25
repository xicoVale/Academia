package tables;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.omg.CORBA.COMM_FAILURE;

import dbconnect.DBConnect;

public class Customer extends Tables {
	private final static int SIZE = 12;
	private ArrayList<String> attributes;
	private DBConnect conn;
	// The first part of the insert query
	private final String INSERT = "INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, "
			+ "addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit) VALUES(";

	public Customer() {
		setAttributes();
	}

	public Customer(DBConnect conn) {
		setAttributes();
		setConnection(conn);
	}

	@Override
	public int getSize() {
		return SIZE;
	}
	public static int size() {
		return SIZE;
	}

	@Override
	public ArrayList<String> getAttributes() {
		return this.attributes;
	}

	@Override
	public void setAttributes() {
		this.attributes = new ArrayList<String>(SIZE);
	}
	public void setAttributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}

	public DBConnect getConnection() {
		return conn;
	}

	public void setConnection(DBConnect conn) {
		this.conn = conn;
	}
	/**
	 * Adds a new customer to the customers table.
	 */
	public void register() {
		// Will hold the values provided by user input
		String query = "'" + getNewCustomerNumber() + "'";
		registerWithId(query);
	}
	/**
	 * Adds a new customer to the customers table using a customerNumber provided
	 * @param query - A {@link String} containing the customerNumber to be used
	 */
	public void registerWithId(String query) {
		// Iterate through the attributes to be added to the table
		for (String next : getAttributes()) {
			// Converts n/a into NULL
			if (next.equalsIgnoreCase("n/a") || next.equals("")) {
				query += ", NULL";
			} else {
				query += ", '" + next + "'";
			}
		}
		query += ")";
		// Update the database
		conn.updateDb(INSERT + query);
		try {
			conn.getConnection().commit();
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
		}
	}

	/**
	 * Returns the customerNumber the next customer should have
	 * 
	 * @return - The next available customer number
	 */
	private int getNewCustomerNumber() {
		// This query returns the highest customer number in use
		// The next customerNumber should be the result of this query plus one
		String getNewCustomerNumber = "SELECT customerNumber FROM customers ORDER BY customerNumber DESC LIMIT 1;";
		ResultSet res = conn.query(getNewCustomerNumber);
		int customerNumber = Integer.MIN_VALUE;
		try {
			res.next();
			// Convert the result to int
			customerNumber = Integer.parseInt(res.getString("customerNumber")) + 1;
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
		}
		return customerNumber;
	}

	/**
	 * Exports the customers table to a file.
	 * 
	 * @param path - A {@link String} containing the path to the export file
	 */
	public void exportCustomers(String path) {
		// A query that returns the entirety of the customers table
		String query = "SELECT * FROM customers ORDER BY customerNumber ASC";
		
		// Open a FileOutputStream to write the file
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
			ResultSet res = conn.query(query);
			
			// Get the number of columns from the query result
			ResultSetMetaData rsmd = res.getMetaData();
			int cols = rsmd.getColumnCount();
			
			// Iterate over the query results and write them to the file
			for (int col = 1; col <= cols; col++) {
				res.next();
				oos.writeObject(res.getString(col));
			}
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exports a customers order history to a file named after his customerNumber
	 */
	public void exportCustomerDetails(String id) {
		String orderQuery = "SELECT * FROM orders "
				+ " JOIN orderDetails ON orders.orderNumber = orderDetails.orderNumber"
				+ " WHERE orders.customerNumber = " + id;
		try (BufferedWriter file = new BufferedWriter(new FileWriter(id + ".txt"))) {
			ResultSet res = conn.query(orderQuery);
			String header = writeHeader(res);
			String row = formatTable(res);
			
			file.write(header + row);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a formatted header for writing
	 *
	 * @return - A {@link String} containing a formatted header
	 */
	private String writeHeader(ResultSet res) {
		StringBuilder header = new StringBuilder();
		try {
			// Get the number of columns
			ResultSetMetaData rsmd = res.getMetaData();
			int colCount = rsmd.getColumnCount();

			// Iterate over the columns to get their names
			for (int col = 1; col <= colCount; col++) {
				// Fetches the column name to be written
				String label = rsmd.getColumnLabel(col);
				
				// Skips formatting of the comments table
				// It is to big to be formatted the other way
				if (label.equals("comments")) {
					header.append(String.format("| %s ", label));
				}
				// Formatting the rest of the column names
				else {
					// Calculate how many spaces to add so the printed column name has the propper size
					int labelSize = label.length();
					int colSize = rsmd.getColumnDisplaySize(col);
					int spaces = (colSize - labelSize) / 2;
					// There should be at least one space in either side
					if (spaces == 0) { spaces = 1; }
					// Formatting the column name
					header.append(String.format("|%" + spaces + "s" + "%s" + "%" + spaces + "s", " ", label, " "));
				}
			}
			header.append("\n");
			// Creates the lines that will be written above and below the column names
			StringBuilder line = new StringBuilder();
			while (line.length() < header.length()) {
				line.append("-");
			}
			line.append("\n");
			header.append(line);
			line.append(header);

			return line.toString();
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
			return null;
		}
	}

	/**
	 * Formats the body of the table for writing
	 * 
	 * @param res - A {@link ResultSet} containing the contents of the table to be formatted
	 * @return - A {@link String} containing the formatted table
	 */
	private String formatTable(ResultSet res) {
		try {
			ResultSetMetaData rsmd = res.getMetaData();
			StringBuilder row = new StringBuilder();
			int colCount = rsmd.getColumnCount();

			while (res.next()) {
				for (int col = 1; col <= colCount; col++) {
					String colValue = res.getString(col);
					// comments column must be exempt from regular formatting 
					// otherwise it becomes too big and breaks the formatting
					if (col == 6) {
						row.append(String.format("| %s", colValue));
					}
					else {
						// Calculate how many spaces should be should be added to either side of the table contens 
						int valueSize = colValue.length();
						int size = rsmd.getColumnDisplaySize(col);
						int spaces = (size - valueSize) / 2;
						// There should always be some space between the | and the actual value
						if(spaces == 0) {spaces = 1;}
						row.append(String.format("|%" + spaces + "s" + "%s" + "%" + spaces + "s", " ", colValue, " "));
					}
				}
				row.append("|\n");
			}
			return row.toString();
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
			return null;
		}
	}
}
