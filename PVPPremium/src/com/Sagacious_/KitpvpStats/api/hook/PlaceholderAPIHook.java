package com.Sagacious_.KitpvpStats.api.hook;

import org.bukkit.OfflinePlayer;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLManager;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;

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
    public String onRequest(OfflinePlayer p, String id){
		LeaderboardHandler lh = Core.getInstance().getLeaderboardHandler();
		DataHandler dh = Core.getInstance().getDataHandler();
		SLManager sl = Core.getInstance().getSLManager();
		if(p!=null) {
		UserData ps = dh.getData(p.getUniqueId());
		if(id.equals("level")) {
		    return "" + ps.getLevel().getName();
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
			if(lh.orderedKills.size()>f) {
				return id.endsWith("amount")?""+lh.killTop.get(f):lh.orderedKills.get(f).getName();
			}
			return "None";
		}
		if(id.contains("deaths_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedDeaths.size()>f) {
				return id.endsWith("amount")?""+lh.deathTop.get(f):lh.orderedDeaths.get(f).getName();
			}
			return "None";
		}
		if(id.contains("killstreak_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedKillstreak.size()>f) {
				return id.endsWith("amount")?""+lh.killstreakTop.get(f):lh.orderedKillstreak.get(f).getName();
			}
			return "None";
		}
		if(id.contains("chr_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedCHR.size()>f) {
				return id.endsWith("amount")?""+lh.chrTop.get(f):lh.orderedCHR.get(f).getName();
			}
			return "None";
		}
		if(id.contains("hmr_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedHMR.size()>f) {
				return id.endsWith("amount")?""+lh.hmrTop.get(f):lh.orderedHMR.get(f).getName();
			}
			return "None";
		}
		if(id.startsWith("level_")&&!id.contains("progress")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedLevel.size()>f) {
				return id.endsWith("amount")?""+lh.levelTop.get(f).getName():lh.orderedLevel.get(f).getName();
			}
			return "None";
		}
		if(id.contains("kdr_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedKDR.size()>f) {
				return id.endsWith("amount")?""+lh.kdrTop.get(f):lh.orderedKDR.get(f).getName();
			}
			return "None";
		}
		if(id.equals("curr_xp")) {
			return "" + ps.getXP();
		}
		if(id.equals("nextlevel_xp")) {
			return "" + sl.getNextLevelXP(ps.getLevel());
		}
		if(id.equals("level_progress")) {
			return sl.getLevelProgress(ps);
		}
        if(id.equals("level_progress_percent")) {
			return sl.getLevelProgressPercent(ps);
		}
        if(id.equals("combatlog")) {
        	return ""+Core.getInstance().getCombatlogHandler().getCombatLog(p.getUniqueId());
        }
        if(id.equals("uniquekills")) {
        	return ""+ps.getUniqueKills().keySet().size();
        }
        if(id.contains("uniquekilltop_")) {
			int f = Integer.parseInt(id.split("_")[1])-1;
			if(lh.orderedUnqiuekills.size()>f) {
				return id.endsWith("amount")?""+(int)Math.round(lh.unqiuekillTop.get(f)):lh.orderedUnqiuekills.get(f).getName();
			}
			return "None";
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
