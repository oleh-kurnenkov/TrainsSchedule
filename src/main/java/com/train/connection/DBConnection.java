package com.train.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	
    private static final String DB_URL = "jdbc:mysql://localhost:3306/train_schema";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";
    
    private static DBConnection instance;
    private Connection connection;
    private	Statement statement;   
    
	private DBConnection() {
		try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(DB_URL,LOGIN,PASSWORD);
        statement = connection.createStatement();
		}
		catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	public PreparedStatement getPreparedStatement(String query) throws SQLException{
		return connection.prepareStatement(query);
	}
	
	public Statement getStatement(){
		return statement;
	}
	
	public static DBConnection  getInstance(){
        if (instance == null)
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        return instance;
	}
}
