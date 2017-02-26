package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnect.DBConnect;

public class OrderDetails extends Tables {
	private final int SIZE = 5;
	private ArrayList<String> attributes;
	private DBConnect conn;
	// Insert string
	private final String INSERT = "INSERT INTO orderDetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES (";
	
	public OrderDetails(){
		setAttributes();
	}
	public OrderDetails(DBConnect conn) {
		setConnection(conn);
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
	public void setConnection(DBConnect conn) {
		this.conn = conn;
	}

	@Override
	public DBConnect getConnection() {
		return conn;
	}
	/**
	 * Checks weather a productCode already exists in the database
	 * 
	 * @param productCode - A {@link String} containing the product code to be checked
	 * @return - <code>true</code> if the code is available to be used
	 * @return - <code>false</code> if the code isn't available to be used
	 */
	public boolean checkProductCode(String productCode) {
		String query = "SELECT productCode FROM products WHERE productCode = '" + productCode + "'";
		ResultSet res = conn.query(query);
		try {
			return (!res.next());
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
			return false;
		} 
	}
	/**
	 * Adds a new entry into the orderDetails table of the database.
	 */
	@Override
	public void register() {
		String query = "";
		
		for (String next: attributes) {
			if (attributes.indexOf(next) == 0) {
				query += "'" + next + "'";
			}
			else {
				query += ", '" + next + "'";
			}
		}
		query += ")";
		try {
			conn.updateDb(INSERT + query);
			conn.getConnection().commit();
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
		}
	}
	
}
