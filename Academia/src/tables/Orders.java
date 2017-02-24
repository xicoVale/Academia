package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconnect.DBConnect;

public class Orders extends Tables {
	private final int SIZE = 6;
	private ArrayList<String> attributes;
	private DBConnect conn;
	private int orderNumber = 0;
	private final String INSERT = "INSERT INTO orders (orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (";
	
	public Orders(){
		setAttributes();
	}
	
	public Orders(DBConnect conn) {
		setConn(conn);
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
	public void setConn(DBConnect conn) {
		this.conn = conn;		
	}

	@Override
	public DBConnect getConn() {
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
		
		conn.updateDb(INSERT + query);
	}
	/**
	 * Returns the next order number
	 * 
	 * @return
	 */
	private int getNewOrderNumber() {
		String getNumber = "SELECT orderNumber FROM orders ORDER BY orderNumber DESC LIMIT 1";
		int newNumber = 0;
		try {
			ResultSet res = conn.query(getNumber);
			if (res.next()) {
				newNumber = Integer.parseInt(res.getString("orderNumber")) + 1;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} finally {
			return newNumber;
		}
	}

	
	
}
