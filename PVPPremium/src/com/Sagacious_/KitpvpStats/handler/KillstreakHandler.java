package com.Sagacious_.KitpvpStats.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class KillstreakHandler {
	
	public HashMap<Integer, HashMap<List<String>, List<String>>> killstreaks = new HashMap<Integer, HashMap<List<String>,List<String>>>();
	
	public KillstreakHandler() {
		FileConfiguration conf = Core.getInstance().getConfig();
		for(String k : conf.getConfigurationSection("killstreaks").getKeys(false)) {
			HashMap<List<String>, List<String>> s = new HashMap<List<String>, List<String>>();
			s.put(conf.getStringList("killstreaks." + k + ".perform"), conf.getStringList("killstreaks." + k + ".end"));
			killstreaks.put(Integer.valueOf(k), s);
		}
	}
	
	private int getLastSurpassedKillStreak(int streak) {
		Set<Integer> s = killstreaks.keySet();
		int selected = -1;
		for(Integer f : s) {
			if(f<=streak&&f>selected) {
				selected=f;
			}
		}
		return selected;
	}
	
	public void reward(Player p, int streak) {
		if(killstreaks.containsKey(streak)) {
			for(List<String> f : killstreaks.get(streak).keySet()) {
				for(String s : f) {
					if(s.equals("")) {return;}
					if(s.startsWith("chat:")){
						   s = s.substring(5);
						   p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%player%", p.getName())));
						}else if (s.startsWith("bc:")){
							s = s.substring(3);
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%player%", p.getName())));
						}else {
						   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName())));
						}
				}
			}
		}
	}
	
	public void endkillstreak(Player killer, Player p, int streak) {
		if(killer.equals(p)) {return;}
		if(getLastSurpassedKillStreak(streak)>0) {
			for(Entry<Integer, HashMap<List<String>, List<String>>> f : killstreaks.entrySet()) {
				if(f.getKey().equals(getLastSurpassedKillStreak(streak))) {
				for(Entry<List<String>, List<String>> q : f.getValue().entrySet()) {
					for(String s : q.getValue()) {
						if(s.equals("")) {return;}
						if(s.startsWith("chat:")){
							   s = s.substring(5);
							   killer.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%killer%", killer.getName())));
							}else if (s.startsWith("bc:")){
								s = s.substring(3);
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%killer%", killer.getName())));
							}else {
							   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replace("%player%", p.getName()).replace("%killer%", killer.getName())));
							
					}
				}
					}
				}
			}
		}
	}

}

