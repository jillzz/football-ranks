package crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	public Connection connection = null;
	
	
	/**
	 * Constructor
	 * Build a connection with the database
	 */
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/football";
			connection = DriverManager.getConnection(url, "user", "password");
			System.out.println("Connection to the database built");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			
		}
	}
	
	
	/**
	 * Execute querry and fetch result
	 * 
	 * @param query
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet fetchExecute(String query) throws SQLException {
		Statement sta = connection.createStatement();
		return sta.executeQuery(query);
	}

	
	/**
	 * Execute query
	 * 
	 * @param query
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean execute (String query) throws SQLException {
		Statement sta = connection.createStatement();
		return sta.execute(query);
	}
	
	
	/**
	 * Close connection
	 */
	@Override
	protected void finalize() throws Throwable {
		if (connection != null && !connection.isClosed())
			connection.close();
	}
}
