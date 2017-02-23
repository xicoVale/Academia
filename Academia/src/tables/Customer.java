package tables;

import java.util.ArrayList;
import java.util.Iterator;

import dbconnect.DBConnect;

public class Customer extends Tables {
	private final int SIZE = 12;
	private ArrayList<String> attributes;
	private DBConnect conn;
	
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
		Iterator <String> iterator = attributes.iterator();
		String query = "INSERT INTO customer (customerName, contactLastName, contactFistName, phone, adressLine1, adressLine2, "
				+ "city, state, postalCode, country, salesRepEmployeeNumber, creditLimit) VALUES(";
		
		while (iterator.hasNext()) {
			if (iterator.next().equalsIgnoreCase("n/a")) {
				query += ", NULL";
			}
			else {
				query += ", " + iterator.next().toString();
			}
		}
		query += ");";
		
		conn.queryDb(query);
	}
}
