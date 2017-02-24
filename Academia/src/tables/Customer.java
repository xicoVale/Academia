package tables;

import java.sql.ResultSet;
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
	
	public Customer(){
		setAttributes();
	}
	
	public Customer(DBConnect conn){
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
			//Converts n/a into NULL
			if (next.equalsIgnoreCase("n/a") || next.equals("")) {
				query += ", NULL";
			}
			else {
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
	
}
