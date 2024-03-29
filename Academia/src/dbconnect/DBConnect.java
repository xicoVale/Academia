package dbconnect;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import exceptions.InsuficientAttributesException;
import exceptions.InvalidCustomerIdException;
import tables.Customer;

public class DBConnect implements AutoCloseable{
	private final String URL = "jdbc:mysql://localhost:3306/data";
	private Connection conn;
	private static int single;
	
	private DBConnect(){
		connect();
		single++;
	}
	
	@Override
	public void finalize() {
		single = 0;
	}
	/**
	 * The DBConnect singleton method.
	 * 
	 * @return - A DBConnect object
	 * @throws Exception - An Exception is thrown if a connection already exists
	 */
	public static DBConnect newDBC() throws Exception{
		if (single == 0){
			DBConnect conn = new DBConnect();
			return conn;
		}
		else{
			throw new Exception("Database connection already exists");
		}
		
	}
	/**
	 * Establishes a connection to the database
	 */
	private void connect(){
		try {
			conn = DriverManager.getConnection(URL, "root", "password");
			// AutoCommit is set to false because of order updates
			// Since two tables are involved the database must only change if both
			// Updates are correct
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			sqlExceptionHandler(e);
		}
	}
	public Connection getConnection(){
		return conn;
	}
	/**
	 * Updates database table with the provided query.
	 * 
	 * @param query - String with the update query
	 * @throws SQLException 
	 */
	public void updateDb(String query) throws SQLException{
		Statement st = conn.createStatement();
		st.executeUpdate(query);	 
	}
	/**
	 * Queries the data base. Returns a ResultSet containing the result
	 * of the query or null if a SQLException occurs. 
	 * 
	 * @param query - String containing the query
	 * @return - ResultSet containing the results of the query  
	 */
	public ResultSet query(String query) {
		ResultSet res = null;
		try {
			Statement st = conn.createStatement();
			res = st.executeQuery(query);
		} catch (SQLException e) {
			sqlExceptionHandler(e);
		} finally {
			return res;
		}
	}
	/**
	 * 
	 * Checks if a customerNumber is already in use in the database
	 * 
	 * @param id
	 * @return true if the number isn't being used
	 * @return false if the contents of id aren't a 1 to 3 digit number or id does not match any customerNumber in the database
	 * @throws InvalidCustomerIdException - Thrown when the contents of id does not match any customerNumber in the database
	 */
	public boolean checkCustomerId(String id) {
		if(!id.matches("(^[0-9]{1,3})")) {
			return false;
		}
		else {
			String query = "SELECT customerNumber FROM customers WHERE customerNumber = " + id;
			ResultSet res = query(query);
			try {
				// CustomerNumber is being used in the database
				if (res.next()) {
					return false;
				}
				// CustomerNumber isn't being used in the database
				else {
					return true;
				}
			} catch (SQLException e) {
				sqlExceptionHandler(e);
				return false;
			} 	
		}
	}
	/**
	 * Imports the contents of a binary file into the database.
	 * 
	 * @param path - String containing the path of the file to be imported
	 * @throws InvalidCustomerIdException 
	 * @throws InsuficientAttributesException 
	 */
	public void importFile(String path) throws InsuficientAttributesException, InvalidCustomerIdException, IOException{
		ArrayList<String> attributes = new ArrayList<String>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
			for(int i = 0; i < Customer.size(); i++) {
				String line = (String) ois.readObject();
				attributes.add(line);
			}
			System.out.println(attributes.toString());
			processImport(attributes);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			processImport(attributes);
		}
	}
	/**
	 * Process the input read from a binary file and calls the methods that will add
	 * the information to the database
	 * 
	 * @param attributes - ArrayList containing the information read
	 * @throws InvalidCustomerIdException - This exception will be thrown when the user inputs an invalid customerNumber
	 */
	private void processImport(ArrayList<String> attributes) throws InvalidCustomerIdException {
		while (attributes.size() < Customer.size() + 1) {
			attributes.add("");
		}
		
		if(checkCustomerId(attributes.get(0))) {
			Customer customer = new Customer(this);
			customer.setAttributes(attributes);
			customer.registerWithId("'" + attributes.remove(0) + "'");
		}
	}
	/**
	 * Handles SQLExeptions by printing error messages and codes
	 * 
	 * @param e - SQLException to be handled
	 */
	public void sqlExceptionHandler(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("VendorError: " + e.getErrorCode());
	}
	/*
	 * Closes the connection and decrements the singleton counter
	 * (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		conn.close();
		single--;
	}
}