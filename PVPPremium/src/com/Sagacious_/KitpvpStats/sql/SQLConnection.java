package com.Sagacious_.KitpvpStats.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import com.Sagacious_.KitpvpStats.Core;
import com.mysql.jdbc.PreparedStatement;

public class SQLConnection {
	
	private Connection conn;
	
	public SQLConnection(String database, String port, String username, String password, String host) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql//" + host + "/" + database + "?user=" + username + "&password=" + password);
			Core.getInstance().getLogger().info("MySQL Connection has been established");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				refreshConnection();
			}
		}.runTaskTimer(Core.getInstance(), 144000L, 144000L);
	}
	
	public void setupTables() {
		executeStatement("CREATE TABLE IF NOT EXISTS PVPData(uuid VARCHAR(36) PRIMARY KEY, name TEXT, kills INT, deaths INT, killstreak INT, top_killstreak INT, resets INT,"
				+ "xp FLOAT, hits INT, misses INT, criticals INT, bountiesKilled INT, bountiesSurvived INT);");
		executeStatement("CREATE TABLE IF NOT EXISTS Bounty(id TEXT PRIMARY KEY, uuidOwner VARCHAR(36), uuidTarget VARCHAR(36) PRIMARY KEY, amount FLOAT, timeLeft INT");
	}
	
	private void refreshConnection() {
		PreparedStatement stat = prepareStatement("SELECT 1 FROM Dual;");
		try {
			stat.executeUpdate();
			stat.close();
			Core.getInstance().getLogger().info("MySQL Connection has been refreshed");
		} catch (SQLException e) {
			e.printStackTrace();
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
		try {
			PreparedStatement stat = (PreparedStatement) conn.prepareStatement(query);
			return stat;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
