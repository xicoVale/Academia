package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
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
