package crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	public Connection connection = null;
	
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
	
	public ResultSet fetchExecute(String query) throws SQLException {
		Statement sta = connection.createStatement();
		return sta.executeQuery(query);
	}

	public boolean execute (String query) throws SQLException {
		Statement sta = connection.createStatement();
		return sta.execute(query);
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (connection != null && !connection.isClosed())
			connection.close();
	}
}
