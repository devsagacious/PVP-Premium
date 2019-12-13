package com.sagaciousdevelopment.PVPPremium.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.sagaciousdevelopment.PVPPremium.Core;

public class SQLConnection {

	private Connection conn;
	private String db;
	
	public SQLConnection(String host, String port, String database, String username, String password) {
		long start = System.currentTimeMillis();
		db=host+"/"+database;
		try {
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
	    System.out.println("Established SQL connection in " + (System.currentTimeMillis()-start) + "ms");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to establish a connection to the SQL instance");
		}
		if(isConnected()) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refreshConnection();
					System.out.println("SQL Connection for '" + db + "' has been automatically refreshed");
				}
			}, 14400L, 14400L);
		}
	}
	
	public boolean isConnected() {
		try {
			return conn!=null&&!conn.isClosed();
		} catch (SQLException e) {}
		return false;
	}
	
	public void closeConnection() {
		if(isConnected()) {
			try {
				conn.close();
			} catch (SQLException e) {
			    e.printStackTrace();
			}
			System.out.println("SQL Connection to '" + db + "' has been terminated");
		}
	}
	
	private void refreshConnection() {
		try {
			ResultSet set  = conn.prepareStatement("SELECT 1 FROM Dual").executeQuery();
			set.next();set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement prepareStatement(String query) {
		try {
			return conn.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
