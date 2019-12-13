package com.sagaciousdevelopment.PVPPremium.api;

import org.bukkit.entity.Player;

import com.sagaciousdevelopment.PVPPremium.Core;
import com.sagaciousdevelopment.PVPPremium.stats.Stats;

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
		Stats ps = Core.getInstance().sm.getData(p);
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
