package com.sagaciousdevelopment.PVPPremium.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sagaciousdevelopment.PVPPremium.Core;


public class KillstreakHandler {
    private HashMap<Integer, List<String>> killstreaks = new HashMap<Integer, List<String>>();
    public KillstreakHandler() {
    	FileConfiguration conf = Core.getInstance().getConfig();
		for(String k : conf.getConfigurationSection("killstreaks").getKeys(false)) {
			killstreaks.put(Integer.valueOf(k), conf.getStringList("killstreaks." + k + ".perform"));
		}
		
    }
	
	public void reward(Player p, int streak) {
		if(killstreaks.containsKey(streak)) {
			for(String f : killstreaks.get(streak)) {
				f = ChatColor.translateAlternateColorCodes('&', f);
				if(!f.startsWith("chat:")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), f.replace("%player%", p.getName()));
				}else {
					p.sendMessage(f);
				}
			}
		}
	}
}
