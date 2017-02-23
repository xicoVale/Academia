package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
	private final String URL = "jdbc:mysql://localhost:3306/data";
	private Connection conn;
	
	public DBConnect(){
		connect();
	}
	
	private void connect(){
		try {
			conn = DriverManager.getConnection(URL, "root", "password");
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
}
