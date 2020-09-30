package com.Sagacious_.KitpvpStats.data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.rank.SLevel;
import com.Sagacious_.KitpvpStats.sql.SQLConnection;

public class DataHandler {

	public HashMap<UUID, UserData> stats = new HashMap<UUID, UserData>();
	
	private SQLConnection sql = Core.getInstance().getSQLConnection();
	
	public DataHandler() {
		if(sql==null) {
		File dir = new File(Core.getInstance().getDataFolder(), "data");
		if(!dir.exists()) {
			dir.mkdir();
		}
		for(File f : dir.listFiles()) {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			
			List<String> unique = conf.getStringList("uniqueKills");
			HashMap<UUID, Integer> fs = new HashMap<UUID, Integer>();
			
			for(String sd : unique) {
				fs.put(UUID.fromString(sd.split(":")[0]), Integer.parseInt(sd.split(":")[1]));
			}
			
			UserData s = new UserData(UUID.fromString(f.getName().replace(".yml","")), conf.getString("name"), conf.getInt("kills"), conf.getInt("deaths"),
					conf.getInt("killstreak"), conf.getInt("top_killstreak"), conf.getInt("resets"), conf.getDouble("xp"), conf.getInt("hits"), conf.getInt("misses"), conf.getInt("criticals"), conf.getInt("bountiesKilled"), conf.getInt("bountiesSurvived"), fs);
		    stats.put(s.getUniqueId(), s);
		}
		}else {
			try {
				ResultSet set = sql.prepareStatement("SELECT * FROM PVPData").executeQuery();
				while(set.next()) {
					List<String> unique = new ArrayList<String>(Arrays.asList(set.getString("uniqueKills").split("--")));
					HashMap<UUID, Integer> fs = new HashMap<UUID, Integer>();
					
					for(String sd : unique) {
						if(!sd.split(":")[0].equals("")&&!sd.split(":")[0].equals("{}")) {
						fs.put(UUID.fromString(sd.split(":")[0]), Integer.parseInt(sd.split(":")[1]));
					}
					}
					
					UserData d = new UserData(UUID.fromString(set.getString("uuid")), set.getString("name"), set.getInt("kills"), set.getInt("deaths"), set.getInt("killstreak"), 
						     set.getInt("top_killstreak"), set.getInt("resets"), set.getDouble("xp"), set.getInt("hits"), set.getInt("misses"), 
						     set.getInt("criticals"), set.getInt("bountiesKilled"), set.getInt("bountiesSurvived"), fs);
					stats.put(d.getUniqueId(), d);
				}
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public UserData getData(Player p) {
		if(!stats.containsKey(p.getUniqueId())) {
			UserData d = new UserData(p.getUniqueId(), p.getName(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
			stats.put(p.getUniqueId(), d);
			return d;
		}
		return stats.get(p.getUniqueId());
	}
	
	public UserData getData(UUID u) {
		if(!stats.containsKey(u)) {
			return null;
		}
		return stats.get(u);
	}
	
	public UserData getData(String name) {
		HashMap<String, UserData> poss = new HashMap<String, UserData>();
		for(UserData d : getAllUserData()) {
			if(d.getName().equalsIgnoreCase(name)) {
				poss.put(d.getName(), d);
			}
		}
		   if(poss.containsKey(name)) {
			   return poss.get(name);
		   }
		   UserData[] d = new UserData[poss.values().size()];
		   if(poss.values().size()<1) {return null;}
		return poss.values().toArray(d)[0];
	}
	
	public List<UserData> getAllUserData(){
		List<UserData> s = new ArrayList<UserData>();
		for(Entry<UUID, UserData> z : stats.entrySet()) {
			s.add(z.getValue());
		}
		return s;
	}
	
	public HashMap<UserData, Integer> getKills(int kills){
		HashMap<UserData, Integer> t = new HashMap<UserData, Integer>();
		for(UserData u : getAllUserData()) {
			if(u.getKills()==kills) {
				t.put(u, kills);
			}
		}
		return t;
	}
	
	public HashMap<UserData, Integer> getDeaths(int deaths){
		HashMap<UserData, Integer> t = new HashMap<UserData, Integer>();
		for(UserData u : getAllUserData()) {
			if(u.getDeaths()==deaths) {
				t.put(u, deaths);
			}
		}
		return t;
	}
	
	public HashMap<UserData, Integer> getKillstreak(int killstreak){
		HashMap<UserData, Integer> t = new HashMap<UserData, Integer>();
		for(UserData u : getAllUserData()) {
			if(u.getTopKillstreak()==killstreak) {
				t.put(u, killstreak);
			}
		}
		return t;
	}
	
	public List<UserData> getLevel(SLevel level){
		List<UserData> d = new ArrayList<UserData>();
		for(UserData u : getAllUserData()) {
			if(u.getLevel().equals(level)) {d.add(u);}
		}
		return d;
	}
	
	public HashMap<UserData, Double> getKDR(double KDR){
		HashMap<UserData, Double> t = new HashMap<UserData, Double>();
		for(UserData u : getAllUserData()) {
			if(Double.valueOf(u.getKDR())==KDR) {
				t.put(u, KDR);
			}
		}
		return t;
	}
	
	public HashMap<UserData, Double> getCHR(double CHR){
		HashMap<UserData, Double> t = new HashMap<UserData, Double>();
		for(UserData u : getAllUserData()) {
			if(Double.valueOf(u.getCHR())==CHR) {
				t.put(u, CHR);
			}
		}
		return t;
	}
	
	public HashMap<UserData, Double> getHMR(double HMR){
		HashMap<UserData, Double> t = new HashMap<UserData, Double>();
		for(UserData u : getAllUserData()) {
			if(Double.valueOf(u.getHMR())==HMR) {
				t.put(u, HMR);
			}
		}
		return t;
	}
	
	public HashMap<UserData, Double> getUnqiueKills(double HMR){
		HashMap<UserData, Double> t = new HashMap<UserData, Double>();
		for(UserData u : getAllUserData()) {
			if(Double.valueOf(u.getUniqueKills().entrySet().size())==HMR) {
				t.put(u, HMR);
			}
		}
		return t;
	}
}
