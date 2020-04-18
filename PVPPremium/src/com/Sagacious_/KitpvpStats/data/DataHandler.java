package com.Sagacious_.KitpvpStats.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class DataHandler {

	public HashMap<UUID, UserData> stats = new HashMap<UUID, UserData>();
	
	public DataHandler() {
		File dir = new File(Core.getInstance().getDataFolder(), "data");
		if(!dir.exists()) {
			dir.mkdir();
		}
		for(File f : dir.listFiles()) {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			UserData s = new UserData(UUID.fromString(f.getName().replace(".yml","")), conf.getString("name"), conf.getInt("kills"), conf.getInt("deaths"),
					conf.getInt("killstreak"), conf.getInt("top_killstreak"), conf.getInt("resets"), conf.getDouble("xp"), conf.getInt("hits"), conf.getInt("misses"), conf.getInt("criticals"), conf.getInt("bountiesKilled"), conf.getInt("bountiesSurvived"));
		    stats.put(s.getUniqueId(), s);
		}
	}
	
	public UserData getData(Player p) {
		if(!stats.containsKey(p.getUniqueId())) {
			return null;
		}
		return stats.get(p.getUniqueId());
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
		return poss.entrySet().iterator().next().getValue();
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
			if(u.getKills()==deaths) {
				t.put(u, deaths);
			}
		}
		return t;
	}
	
	public HashMap<UserData, Integer> getKillstreak(int killstreak){
		HashMap<UserData, Integer> t = new HashMap<UserData, Integer>();
		for(UserData u : getAllUserData()) {
			if(u.getKillstreak()==killstreak) {
				t.put(u, killstreak);
			}
		}
		return t;
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
}
