package com.Sagacious_.KitpvpStats.api.hook;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class WorldguardHook {
	private boolean enabled = false;
	private List<String> regions;
	
	public WorldguardHook() {
		if(Bukkit.getPluginManager().isPluginEnabled("WorldEdit") && Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			enabled = Core.getInstance().getConfig().getBoolean("worldguard-enabled");
			regions = Core.getInstance().getConfig().getStringList("worldguard-regions");
		}
	}
	
	public boolean countstats(Player p) {
		if(!enabled) {return true;}
		if(enabled) {
			for(com.sk89q.worldguard.protection.regions.ProtectedRegion r : com.sk89q.worldguard.bukkit.WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())) {
                for(String d : regions) {
                	if(r.getId().equalsIgnoreCase(d)) {
                		return true;
                	}
                }
             }
		}
		return false;
	}

}
