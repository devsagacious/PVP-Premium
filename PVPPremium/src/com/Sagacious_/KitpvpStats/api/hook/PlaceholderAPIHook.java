package com.Sagacious_.KitpvpStats.api.hook;

import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class PlaceholderAPIHook extends me.clip.placeholderapi.expansion.PlaceholderExpansion{

	@Override
	public String getAuthor() {
		return "Sagacious_";
	}

	@Override
	public String getIdentifier() {
		return "pvpstats";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
    public String onPlaceholderRequest(Player p, String id){
		if(p!=null) {
		UserData ps = Core.getInstance().dh.getData(p);
		if(id.equals("level")) {
		    return "" + Core.getInstance().sl.getLevel(ps.getXP()).getName();
		}
		if(id.equals("kills")) {
			return "" + ps.getKills();
		}
		if(id.equals("deaths")) {
			return "" + ps.getDeaths();
		}
		if(id.equals("kdr")) {
			return "" + ps.getKDR();
		}
		if(id.equals("chr")) {
			return "" + ps.getCHR();
		}
		if(id.equals("hmr")) {
			return "" + ps.getHMR();
		}
		if(id.equals("killstreak")) {
			return "" + ps.getKillstreak();
		}
		if(id.equals("topkillstreak")) {
			return "" + ps.getTopKillstreak();
		}
		if(id.contains("kills_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.orderedKills.size()>f) {
				return id.endsWith("amount")?""+Core.getInstance().lh.orderedKills.get(f).getKills():Core.getInstance().lh.orderedKills.get(f).getName();
			}
			return "None";
		}
		if(id.contains("deaths_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.orderedDeaths.size()>f) {
				return id.endsWith("amount")?""+Core.getInstance().lh.orderedDeaths.get(f).getDeaths():Core.getInstance().lh.orderedDeaths.get(f).getName();
			}
			return "None";
		}
		if(id.contains("killstreak_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.orderedKillstreak.size()>f) {
				return id.endsWith("amount")?""+Core.getInstance().lh.orderedKillstreak.get(f).getKillstreak():Core.getInstance().lh.orderedKillstreak.get(f).getName();
			}
			return "None";
		}
		if(id.contains("chr_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.orderedCHR.size()>f) {
				return id.endsWith("amount")?""+Core.getInstance().lh.orderedCHR.get(f).getCHR():Core.getInstance().lh.orderedCHR.get(f).getName();
			}
			return "None";
		}
		if(id.contains("hmr_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.orderedHMR.size()>f) {
				return id.endsWith("amount")?""+Core.getInstance().lh.orderedHMR.get(f).getHMR():Core.getInstance().lh.orderedHMR.get(f).getName();
			}
			return "None";
		}
		if(id.contains("kdr_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(Core.getInstance().lh.orderedKDR.size()>f) {
				return Core.getInstance().lh.orderedKDR.get(f).getName();
			}
			return "None";
		}
		if(id.equals("curr_xp")) {
			return "" + ps.getXP();
		}
		if(id.equals("nextlevel_xp")) {
			return "" + Core.getInstance().sl.getNextLevelXP(ps.getLevel());
		}
		if(id.equals("level_progress")) {
			return Core.getInstance().sl.getLevelProgress(ps);
		}
        if(id.equals("level_progress_percent")) {
			return Core.getInstance().sl.getLevelProgressPercent(ps);
		}
		}
		return null;
		
	}
	
	 @Override
	    public boolean persist(){
	        return true;
	    }

	    @Override
	    public boolean canRegister(){
	        return true;
	    }


}
