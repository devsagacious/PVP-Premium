package com.sagaciousdevelopment.PVPPremium.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.sagaciousdevelopment.PVPPremium.Core;


public class StatManager {
	public List<Stats> stats = new ArrayList<Stats>();
	private File dataFolder = new File(Core.getInstance().getDataFolder(), "users");
	
	public StatManager() {
		if(!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		for(File f : dataFolder.listFiles()) {
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			stats.add(new Stats(UUID.fromString(f.getName().concat(".yml")), c.getString("name"), c.getInt("kills"), c.getInt("deaths"),
					c.getInt("killstreak"), c.getInt("top_killstreak"), c.getInt("resets"), c.getInt("hits"), c.getInt("misses"), c.getInt("criticals")));
		}
	}
	
	public Stats getData(Player p) {
		for(Stats d : stats) {
			if(d.getUniqueId().equals(p.getUniqueId())) {
				return d;
			}
		}
		return null;
	}
	
	public Stats getData(String name) {
		List<Stats> poss = new ArrayList<Stats>();
		for(Stats d : stats) {
			if(d.getName().equalsIgnoreCase(name)) {
				poss.add(d);
			}
		}
		if(poss.size()>1) {
			for(Stats d : poss) {
				if(d.getName().equals(name)) {
					return d;
				}
			}
		}
		return poss.get(0);
	}
	
	public List<Stats> getAllStats(){
		return stats;
	}
	
	public List<Stats> getKills(int kills){
		List<Stats> t = new ArrayList<Stats>();
		for(Stats u : stats) {
			if(u.getKills()==kills) {
				t.add(u);
			}
		}
		return t;
	}
	
	public List<Stats> getDeaths(int kills){
		List<Stats> t = new ArrayList<Stats>();
		for(Stats u : stats) {
			if(u.getDeaths()==kills) {
				t.add(u);
			}
		}
		return t;
	}
	public List<Stats> getKillstreak(int kills){
		List<Stats> t = new ArrayList<Stats>();
		for(Stats u : stats) {
			if(u.getTopKillstreak()==kills) {
				t.add(u);
			}
		}
		return t;
	}
	
	public List<Stats> getKDR(double kdr){
		List<Stats> t = new ArrayList<Stats>();
		for(Stats u : stats) {
			if(Double.valueOf(u.getKDR())==kdr) {
				t.add(u);
			}
		}
		return t;
	}
	
	public List<Stats> getCHR(double chr){
		List<Stats> t = new ArrayList<Stats>();
		for(Stats u : stats) {
			if(Double.valueOf(u.getCHR())==chr) {
				t.add(u);
			}
		}
		return t;
	}
	
	public List<Stats> getHMR(double hmr){
		List<Stats> t = new ArrayList<Stats>();
		for(Stats u : stats) {
			if(Double.valueOf(u.getHMR())==hmr) {
				t.add(u);
			}
		}
		return t;
	}

}
