package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public static DBConnect newDBC() throws Exception{
		if (single == 0){
			DBConnect conn = new DBConnect();
			return conn;
		}
		else{
			throw new Exception("Database connection already exists");
		}
		
	}
	
	private void connect(){
		try {
			conn = DriverManager.getConnection(URL, "root", "password");
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	public Connection getConnection(){
		return conn;
	}
	public void updateDb(String query){
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);	
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	public ResultSet query(String query) {
		ResultSet res = null;
		try {
			Statement st = conn.createStatement();
			res = st.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} finally {
			return res;
		}
	}
	/**
	 * 
	 * Checks if a customerNumber is already being used
	 * 
	 * @param id
	 * @return true if the number isn't being used
	 * @return false if the number is being used or if id isn't a number
	 */
	public boolean checkId(String id) {
		if(!id.matches("(^[0-9]{3})")) {
			return false;
		}
		else {
			String query = "SELECT customerNumber FROM customers WHERE customerNumber = " + id;
			ResultSet res = query(query);
			try {
				return (res.next());
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
				return false;
			} 	
		}
	}

	@Override
	public void close() throws Exception {
		conn.close();
		single--;
	}
}
