package com.sagaciousdevelopment.PVPPremium.stats.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.sagaciousdevelopment.PVPPremium.Core;

import net.md_5.bungee.api.ChatColor;

public class SLManager {
    private List<SLevel> levels = new ArrayList<SLevel>();
	public SLManager() {
		FileConfiguration conf = Core.getInstance().getConfig();
		for(String k : conf.getConfigurationSection("levels").getKeys(false)) {
			levels.add(new SLevel(k, ChatColor.translateAlternateColorCodes('&', conf.getString("levels." + k + ".name")), conf.getInt("levels." + k + ".xp_needed"), conf.getStringList("levels." + k + ".cmd")));
		}
		Collections.sort(levels, Collections.reverseOrder());
	}
	
	public SLevel getLevel(int xp) {
		for(SLevel s : levels) {
			if(s.getXPNeeded()>=xp) {
				return s;
			}
		}
		return null;
	}
	
	public boolean isNewLevel(SLevel s, int xp) {
		return getLevel(xp)!=s;
	}
}
