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

import dbconnect.DBConnect;

public class Customer extends Tables {
	private final int SIZE = 12;
	private ArrayList<String> attributes;
	private DBConnect conn;
	// The first part of the insert query
	private final String INSERT = "INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, "
			+ "city, state, postalCode, country, salesRepEmployeeNumber, creditLimit) VALUES(";

	public Customer() {
		setAttributes();
	}

	public Customer(DBConnect conn) {
		setAttributes();
		setConn(conn);
	}

	@Override
	public int getSize() {
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

	public DBConnect getConn() {
		return conn;
	}

	public void setConn(DBConnect conn) {
		this.conn = conn;
	}

	public void register() {
		// Will hold the values provided by user input
		String query = "'" + getNewCustomerNumber() + "'";

		for (String next : getAttributes()) {
			// Converts n/a into NULL
			if (next.equalsIgnoreCase("n/a") || next.equals("")) {
				query += ", NULL";
			} else {
				query += ", '" + next + "'";
			}
		}
		query += ")";

		conn.updateDb(INSERT + query);
		try {
			conn.getConnection().commit();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}

	/**
	 * Returns the customerNumber the next customer should have
	 * 
	 * @return
	 */
	private int getNewCustomerNumber() {
		String getNumber = "SELECT customerNumber FROM customers ORDER BY customerNumber DESC LIMIT 1;";
		ResultSet res = conn.query(getNumber);
		int customerNumber = Integer.MIN_VALUE;
		try {
			res.next();
			customerNumber = Integer.parseInt(res.getString("customerNumber")) + 1;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return customerNumber;
	}

	/**
	 * Exports the customers table to a file.
	 * 
	 * @param path
	 *            Where the export file should be
	 */
	public void exportCustomers(String path) {
		String query = "SELECT * FROM customers ORDER BY customerNumber ASC";

		try (FileOutputStream file = new FileOutputStream(path);) {
			ObjectOutputStream oos = new ObjectOutputStream(file);
			ResultSet res = conn.query(query);
			ResultSetMetaData rsmd = res.getMetaData();

			int cols = rsmd.getColumnCount();
			for (int col = 1; col <= cols; col++) {
				res.next();
				oos.writeObject(res.getString(col));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exports a customers order history
	 */
	public void exportCustomerDetails(String id) {
		String orderQuery = "SELECT * FROM orders "
				+ " JOIN orderDetails ON orders.orderNumber = orderDetails.orderNumber"
				+ " WHERE orders.customerNumber = " + id;
		try (BufferedWriter file = new BufferedWriter(new FileWriter(id + ".txt"))) {
			ResultSet res = conn.query(orderQuery);
			String header = writeHeader(res);
			String row = writeRow(res);
			
			file.write(header + row);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a formatted header for writting
	 *
	 * @return A string containing a formatted header
	 */
	private String writeHeader(ResultSet res) {
		StringBuilder header = new StringBuilder();
		try {
			ResultSetMetaData rsmd = res.getMetaData();
			int colCount = rsmd.getColumnCount();

			for (int col = 1; col <= colCount; col++) {
				String label = rsmd.getColumnLabel(col);
				
				if (label.equals("comments")) {
					header.append(String.format("| %s ", label));
				}
				else {
					int labelSize = label.length();
					int colSize = rsmd.getColumnDisplaySize(col);
					int spaces = (colSize - labelSize) / 2;
					if (spaces == 0) { spaces = 1; }
					
					header.append(String.format("|%" + spaces + "s" + "%s" + "%" + spaces + "s", " ", label, " "));
				}
			}
			header.append("\n");
			StringBuilder line = new StringBuilder();
			while (line.length() < header.length()) {
				line.append("-");
			}
			line.append("\n");
			header.append(line);
			line.append(header);

			return line.toString();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return null;
		}
	}

	/**
	 * Formats the rows for writting
	 * 
	 * @param res
	 * @return A string containing the formatted rows
	 */
	private String writeRow(ResultSet res) {
		try {
			ResultSetMetaData rsmd = res.getMetaData();
			StringBuilder row = new StringBuilder();
			int colCount = rsmd.getColumnCount();

			while (res.next()) {
				for (int col = 1; col <= colCount; col++) {
					String colValue = res.getString(col);
					// comments column must be exempt from regular formatting or it goes all to hell
					if (col == 6) {
						row.append(String.format("| %s", colValue));
					}
					else {
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
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return null;
		}
	}
}
