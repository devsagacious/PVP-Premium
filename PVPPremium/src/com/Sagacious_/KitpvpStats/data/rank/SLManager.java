package com.Sagacious_.KitpvpStats.data.rank;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;


public class SLManager {
    private List<SLevel> levels = new ArrayList<SLevel>();
    
    private String level_progress_identifier = "|";
	private int level_progress_blocks = 10;
	private String level_progress_color = "§c";
	private String level_progress_noncolor = "§7";
    
	public SLManager() {
		FileConfiguration conf = Core.getInstance().getConfig();
		for(String k : conf.getConfigurationSection("levels").getKeys(false)) {
			levels.add(new SLevel(k, ChatColor.translateAlternateColorCodes('&', conf.getString("levels." + k + ".name")), conf.getInt("levels." + k + ".xp_needed"), conf.getStringList("levels." + k + ".cmd")));
		}
		level_progress_identifier = conf.getString("level-progress-identifier");
		level_progress_blocks = conf.getInt("level-progress-blocks");
		level_progress_color = ChatColor.translateAlternateColorCodes('&', conf.getString("level-progress-color"));
		level_progress_noncolor = ChatColor.translateAlternateColorCodes('&', conf.getString("level-progress-noncolor"));
	}
	
	public SLevel getLevel(double d) {
		SLevel s = levels.get(0);
		for(int i = 0; i<levels.size(); i++) {
			if(levels.get(i).getXPNeeded()<=d) {
				s = levels.get(i);
			}
		}
		return s;
	}
	
	public double getNextLevelXP(SLevel l) {
		boolean isnext = false;
		for(int i = 0; i < levels.size(); i++) {
			if(isnext) {
				isnext=false;
				return levels.get(i).getXPNeeded();
			}else {
				if(levels.get(i).equals(l)) {
					isnext=true;
				}
			}
		}
		return l.getXPNeeded();
	}
	
	public double getRealNextLevelXP(SLevel l,double xp) {
		return (getNextLevelXP(l)-xp<0?0:getNextLevelXP(l)-xp);
	}
	
	public boolean isNewLevel(SLevel s, double xp) {
		return getLevel(xp)!=s;
	}
	
	private DecimalFormat df = new DecimalFormat("###.#", new DecimalFormatSymbols(Locale.ENGLISH));
	public String getLevelProgressPercent(UserData s) {
		if(getNextLevelXP(s.getLevel())<0.1) {return "100.0";}
		double percent = (double)s.getXP()/(double)getNextLevelXP(s.getLevel())*(double)100;
		if(percent>100) {percent=100.0;}
		double f = Double.valueOf(String.valueOf(percent).replace(",", "."));
		return df.format(f);
	}
	
	public String getLevelProgress(UserData s) {
		double percent = Double.valueOf(getLevelProgressPercent(s));
		String returned = "";
		for(int i = 0; i < level_progress_blocks; i++) {
			boolean color = false;
			if((double)(i+1)/(double)level_progress_blocks*(double)100<=percent) {
				color = true;
			}
			returned=returned+(color?level_progress_color:level_progress_noncolor)+level_progress_identifier;
		}
		return returned;
	}
}
