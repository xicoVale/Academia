package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnect.DBConnect;

public class Orders extends Tables {
	private final int SIZE = 6;
	private ArrayList<String> attributes;
	private DBConnect conn;
	// An orderNumber to be passed to orderDetails so they have the same orderNumber
	// as the database dictates
	private int orderNumber = 0;
	// Data insertion query
	private final String INSERT = "INSERT INTO orders (orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (";
	
	public Orders(){
		setAttributes();
	}
	
	public Orders(DBConnect conn) {
		setConnection(conn);
		setAttributes();
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public ArrayList<String> getAttributes() {
		return attributes;
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
		return this.conn;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * Adds a new order to the database
	 */
	public void register() {
		this.orderNumber = getNewOrderNumber();
		String orderNumber = new String( "" + this.orderNumber);
		String query = "'" + orderNumber + "'";
		
		for(String next: attributes) {
			if (next.equals("") || next.equalsIgnoreCase("n/a")) {
				query += ", NULL";
			}
			else {
				query += ", '" + next + "'";
			}
		}
		query += ")";
		
		try {
			conn.updateDb(INSERT + query);
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
		}
	}
	/**
	 * Returns the next available orderNumber.
	 * 
	 * @return - The next available order number or 0 if an {@link SQLException} occurs
	 */
	private int getNewOrderNumber() {
		String getNewOrderNumber = "SELECT orderNumber FROM orders ORDER BY orderNumber DESC LIMIT 1";
		int newNumber = 0;
		try {
			ResultSet res = conn.query(getNewOrderNumber);
			if (res.next()) {
				newNumber = Integer.parseInt(res.getString("orderNumber")) + 1;
			}
		} catch (SQLException e) {
			conn.sqlExceptionHandler(e);
		} finally {
			return newNumber;
		}
	}

	
	
}
