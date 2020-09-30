package com.Sagacious_.KitpvpStats.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.bukkit.scheduler.BukkitRunnable;

import com.Sagacious_.KitpvpStats.Core;

public class SQLConnection {
	
	private Connection conn;
	
	public SQLConnection(String database, String port, String username, String password, String host) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Core.getInstance().getLogger().info("MySQL Connection has been established");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				refreshConnection();
			}
		}.runTaskTimer(Core.getInstance(), 600L, 600L);
	}
	
	public void setupTables() {
		executeStatement("CREATE TABLE IF NOT EXISTS PVPData(uuid VARCHAR(36) PRIMARY KEY, name TEXT, kills INT, deaths INT, killstreak INT, top_killstreak INT, resets INT,"
				+ "xp FLOAT, hits INT, misses INT, criticals INT, bountiesKilled INT, bountiesSurvived INT, uniqueKills TEXT);");
		executeStatement("CREATE TABLE IF NOT EXISTS Bounty(id VARCHAR(17) PRIMARY KEY, uuidOwner VARCHAR(36), uuidTarget VARCHAR(36), amount FLOAT, timeLeft INT);");
	}
	
	private void refreshConnection() {
		PreparedStatement st = null;
		ResultSet valid = null;
		try
		{
		st = conn.prepareStatement("SELECT 1 FROM Dual");
		valid = st.executeQuery();
		if (valid.next())
		return;
		} catch (SQLException e2)
		{
		System.out.println("Connection is idle or terminated. Reconnecting...");
		} finally
		{
			
		}
		}
	
	public void executeStatement(String query) {
		PreparedStatement stat = prepareStatement(query);
		try {
			stat.executeUpdate();
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement prepareStatement(String query) {
		refreshConnection();
		try {
			PreparedStatement stat = (PreparedStatement) conn.prepareStatement(query);
			return stat;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
