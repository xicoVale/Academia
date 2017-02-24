package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnect.DBConnect;

public class OrderDetails extends Tables {
	private final int SIZE = 5;
	private ArrayList<String> attributes;
	private DBConnect conn;
	private final String INSERT = "INSERT INTO orderDetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES (";
	
	public OrderDetails(){
		setAttributes();
	}
	public OrderDetails(DBConnect conn) {
		setConn(conn);
		setAttributes();
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

	@Override
	public void setConn(DBConnect conn) {
		this.conn = conn;
	}

	@Override
	public DBConnect getConn() {
		return conn;
	}
	/**
	 * Checks weather a productCode exists in the database
	 * 
	 * @param productCode
	 * @return true if the code exists
	 * @return false if the code doesn't exist
	 */
	public boolean checkProductCode(String productCode) {
		String query = "SELECT productCode FROM products WHERE productCode = " + productCode;
		ResultSet res = conn.query(query);
		try {
			if (!res.next()) {
				return false;
			}
			else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} finally {
			return false;
		}
	}
	@Override
	public void register() {
		String query = "";
		
		for (String next: attributes) {
			if (attributes.indexOf(next) == 0) {
				query += "'" + next + "'";
			}
			query += ", '" + next + "'";
		}
		query += ")";
		conn.query(INSERT + query);
		try {
			conn.getConnection().commit();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
}
