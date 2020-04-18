package com.Sagacious_.KitpvpStats.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.rank.SLevel;

public class UserData {
	private DecimalFormatSymbols f = new DecimalFormatSymbols(Locale.ENGLISH);
	private DecimalFormat df = new DecimalFormat(Core.getInstance().kdr_f, f);
	
	private UUID uuid;
	private String name;
	
	private int kills;
	private int deaths;
	private int killstreak;
	private int top_killstreak;
	private int resets;
    private double xp;
    public boolean online = false;
    public long lastBounty = -1;
    public long lastSound = -1;
	
	private int hits;
	private int misses;
	private int criticals;
	private int bountiesKilled;
	private int bountiesSurvived;
	
	public UserData(UUID uuid, String name, int kills, int deaths, int killstreak, int top_killstreak, int resets, double xp, int hits, int misses, int criticals,
			int bountiesKilled, int bountiesSurvived) {
		this.uuid = uuid;
		this.name = name;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.top_killstreak = top_killstreak;
		this.resets = resets;
		this.xp = xp;
		this.hits = hits;
		this.misses = misses;
		this.criticals = criticals;
		this.bountiesKilled = bountiesKilled;
		this.bountiesSurvived = bountiesSurvived;
	}
	
	public void save() {
		File f = new File(Core.getInstance().getDataFolder(), "data/" + uuid.toString() + ".yml");
		if(!f.exists()) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.println("name: '" + name + "'");pw.println("kills: " + kills);
				pw.println("deaths: " + deaths);pw.println("killstreak: " + killstreak);
				pw.println("top_killstreak: " + top_killstreak);pw.println("resets: " + resets);
				pw.println("hits: " + hits);pw.println("misses: " + misses);pw.println("criticals: " + criticals);pw.println("xp: " + xp);
				pw.println("bountiesKilled: " + bountiesKilled);pw.println("bountiesSurvived: " + bountiesSurvived);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			conf.set("name", name);conf.set("kills", kills);
			conf.set("deaths", deaths);conf.set("killstreak", killstreak);
			conf.set("top_killstreak", top_killstreak);conf.set("resets", resets);
			conf.set("hits", hits);conf.set("misses", misses);conf.set("criticals", criticals);conf.set("xp", xp);
			conf.set("bountiesKilled", bountiesKilled);conf.set("bountiesSurvived", bountiesSurvived);
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
		this.name=name;
	}
	
	public int getKills() {
		return kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public int getKillstreak() {
		return killstreak;
	}
	
	public int getTopKillstreak() {
		return top_killstreak;
	}
	
	public int getResets() {
		return resets;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		if(killstreak>top_killstreak) {
			top_killstreak=killstreak;
		}
	}
	
	public void setTopKillstreak(int killstreak) {
		this.top_killstreak = killstreak;
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
		if(getKills()==0&&getDeaths()==0) {return "0.0";}
		if(getKills()>getDeaths()&&getDeaths()==0) {return getKills()+".0";}
		return df.format(Double.valueOf((double)getKills()/(double)getDeaths()));
	}
	
	public String getHMR() {
		if(getHits()==0&&getMisses()==0) {return "0.0";}
		if(getHits()>getMisses()&&getMisses()==0) {return getHits()+".0";}
		return df.format(Double.valueOf((double)getHits()/(double)getMisses()));
	}
	
	public String getCHR() {
		if(getCriticals()==0&&getHits()==0) {return "0.0";}
		if(getCriticals()>getHits()&&getHits()>0) {return getCriticals()+".0";}
		return df.format(Double.valueOf((double)getCriticals()/(double)getHits()));
	}
	
	public double getXP(){
	    return xp;
    }
	
	public void setXP(double xp) {
		this.xp = xp;
	}
	 
	public SLevel getLevel() {
		return Core.getInstance().sl.getLevel(getXP());
	}
	
	public int getBountiesKilled() {
		return bountiesKilled;
	}
	
	public void setBountiesKilled(int amount) {
		this.bountiesKilled = amount;
	}
	
	public int getBountiesSurvived() {
		return bountiesSurvived;
	}
	
	public void setBountiesSurvived(int amount) {
		this.bountiesSurvived = amount;
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
		this.xp = 0;
	}

}
