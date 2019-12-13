package com.sagaciousdevelopment.PVPPremium.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sagaciousdevelopment.PVPPremium.Core;
import com.sagaciousdevelopment.PVPPremium.stats.rank.SLevel;

public class Stats {
	private DecimalFormat df = new DecimalFormat("##0.0");
	
	private UUID uuid;
	private String name;
	
	private int kills;
	private int deaths;
	private int killstreak;
	private int top_killstreak;
	private int resets;
	
	private int hits;
	private int misses;
	private int criticals;
	
	
	public Stats(UUID uuid, String name, int kills, int deaths, int killstreak, int top_killstreak, int resets, int hits, int misses, int criticals) {
		this.uuid = uuid;
		this.name = name;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.top_killstreak = top_killstreak;
		this.resets = resets;
		this.hits = hits;
		this.misses = misses;
		this.criticals = criticals;
	}
	
	public void save() {
		File f = new File(Core.getInstance().getDataFolder(), "data/" + uuid.toString() + ".yml");
		if(!f.exists()) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.println("name: '" + name + "'");pw.println("kills: " + kills);
				pw.println("deaths: " + deaths);pw.println("killstreak: " + killstreak);
				pw.println("top_killstreak: " + top_killstreak);pw.println("resets: " + resets);
				pw.println("hits: " + hits);pw.println("misses: " + misses);pw.println("criticals: " + criticals);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			conf.set("name", name);conf.set("kills", kills);
			conf.set("deaths", deaths);conf.set("killstreak", killstreak);
			conf.set("top_killstreak", top_killstreak);conf.set("resets", resets);
			conf.set("hits", hits);conf.set("misses", misses);conf.set("criticals", criticals);
			try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public UUID getUniqueId() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	public int getKillstreak() {
		return killstreak;
	}
	
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}
	
	public int getTopKillstreak() {
		return top_killstreak;
	}
	
	public void setTopKillstreak(int top_killstreak) {
		this.top_killstreak = top_killstreak;
	}
	
	public int getResets() {
		return resets;
	}
	
	public void setResets(int resets) {
		this.resets = resets;
	}
	
	public int getHits() {
		return hits;
	}
	
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	public int getMisses() {
		return misses;
	}
	
	public void setMisses(int misses) {
		this.misses = misses;
	}
	
	public int getCriticals() {
		return criticals;
	}
	
	public void setCriticals(int criticals) {
		this.criticals = criticals;
	}
	
	public String getKDR() {
		if(getKills()==getDeaths()&&getKills()>0) {return getKills()+".0";}
		if(getKills()==getDeaths()&&getKills()==0) {return "0.0";}
		return df.format(Double.valueOf((double)getKills()/(double)getDeaths()));
	}
	
	public String getHMR() {
		if(getHits()==getMisses()&&getHits()>0) {return getHits()+".0";}
		if(getHits()==getMisses()&&getHits()==0) {return "0.0";}
		return df.format(Double.valueOf((double)getHits()/(double)getMisses()));
	}
	
	public String getCHR() {
		if(getCriticals()==getHits()&&getCriticals()>0) {return getCriticals()+".0";}
		if(getCriticals()==getHits()&&getCriticals()==0) {return "0.0";}
		return df.format(Double.valueOf((double)getCriticals()/(double)getHits()));
	}
	
	public int getXP(){
	    return getKills()*Core.getInstance().getConfig().getInt("xp-per-kill");
    }
	 
	public SLevel getLevel() {
		return Core.getInstance().sl.getLevel(getXP());
	}
	
	public void reset(boolean use) {
		this.resets=use?this.resets-1:this.resets;
		this.kills = 0;
		this.killstreak = 0;
		this.top_killstreak = 0;
		this.deaths = 0;
		this.hits = 0;
		this.criticals = 0;
		this.misses = 0;
	}

}
