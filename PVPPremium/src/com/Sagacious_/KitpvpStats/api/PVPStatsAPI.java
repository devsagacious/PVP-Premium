package com.Sagacious_.KitpvpStats.api;

import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLevel;

public class PVPStatsAPI {
	
	public static UserData getData(Player p) {
		return Core.getInstance().getDataHandler().getData(p);
	}
	
	public static SLevel getLevel(Player p) {
		return getData(p).getLevel();
	}
	
	public static String getLevelString(Player p) {
		return getData(p).getLevel().getName();
	}
	
	public static int getKills(Player p) {
		return getData(p).getKills();
	}
	
	public static int getDeaths(Player p) {
		return getData(p).getDeaths();
	}
	
	public static int getKillstreak(Player p) {
		return getData(p).getKillstreak();
	}
	
	public static int getTopKillstreak(Player p) {
		return getData(p).getTopKillstreak();
	}
	
	public static int getResets(Player p) {
		return getData(p).getResets();
	}

}
