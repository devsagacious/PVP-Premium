package com.Sagacious_.KitpvpStats.api.hook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HolographicHook implements Listener{
	public Hologram killHologram = null;
	public Hologram deathsHologram = null;
	public Hologram killstreakHologram = null;
	public Hologram kdrHologram = null;
	public Hologram hmrHologram = null;
	public Hologram chrHologram = null;
	
	private String format;
	private Location lke_l;
	private Location lde_l;
	private Location lkie_l;
	private Location kdr_l;
	private Location chr_l;
	private Location hmr_l;
	
	private boolean lke = false;
	private boolean lde = false;
	private boolean lkie = false;
	private boolean kdr = false;
	private boolean chr = false;
	private boolean hmr = false;
	
	public HolographicHook(){
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "leaderboard.yml"));
		lke = conf.getBoolean("leaderboard-kills-enabled");
		lde = conf.getBoolean("leaderboard-deaths-enabled");
		lkie = conf.getBoolean("leaderboard-killstreak-enabled");
		kdr = conf.getBoolean("leaderboard-kdr-enabled");
		chr = conf.getBoolean("leaderboard-criticalhitratio-enabled");
		hmr = conf.getBoolean("leaderboard-hitmissratio-enabled");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
        if(lke) {
        	String[] l = conf.getString("leaderboard-kills-location").split(",");
			lke_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lke_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header")));
			killHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer")));
        }
        if(lde) {
        	String[] l = conf.getString("leaderboard-deaths-location").split(",");
			lde_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lde_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header")));
			deathsHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer")));
        }
        if(lkie) {
        	String[] l = conf.getString("leaderboard-killstreak-location").split(",");
			lkie_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lkie_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header")));
			killstreakHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer")));
        }
        if(kdr) {
        	String[] l = conf.getString("leaderboard-kdr-location").split(",");
			kdr_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), kdr_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-header")));
			kdrHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-footer")));
        }
        if(chr) {
        	String[] l = conf.getString("leaderboard-criticalhitratio-location").split(",");
			chr_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), chr_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-header")));
			chrHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-footer")));
        }
        if(hmr) {
        	String[] l = conf.getString("leaderboard-hitmissratio-location").split(",");
			hmr_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2])+0.3, Double.valueOf(l[3]));
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), hmr_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-header")));
			hmrHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-footer")));
        }
	}

	@EventHandler
	public void onUpdate(LeaderboardUpdateEvent e) {
		List<UserData> temp = new ArrayList<UserData>();
		if(e.getLeaderboard()==0) {
			if(killHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.killTop.size(); i++) {
				int curr = Core.getInstance().lh.killTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Integer> temp2 = Core.getInstance().dh.getKills(curr);
				for(UserData t : temp2.keySet()) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				killHologram.getLine(i).removeLine();
				killHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKills()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==1) {
			if(deathsHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.deathTop.size(); i++) {
				int curr = Core.getInstance().lh.deathTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Integer> temp2 = Core.getInstance().dh.getDeaths(curr);
				for(UserData t : temp2.keySet()) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				deathsHologram.getLine(i).removeLine();
				deathsHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getDeaths()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==2) {
			if(killstreakHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.killstreakTop.size(); i++) {
				int curr = Core.getInstance().lh.killstreakTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Integer> temp2 = Core.getInstance().dh.getKillstreak(curr);
				for(UserData t : temp2.keySet()) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				killstreakHologram.getLine(i).removeLine();
				killstreakHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKillstreak()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==3) {
			if(kdrHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.kdrTop.size(); i++) {
				Double curr = Core.getInstance().lh.kdrTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Double> temp2 = Core.getInstance().dh.getKDR(curr);
				for(UserData t : temp2.keySet()) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				kdrHologram.getLine(i).removeLine();
				kdrHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getKDR()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==4) {
			if(chrHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.chrTop.size(); i++) {
				Double curr = Core.getInstance().lh.chrTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Double> temp2 = Core.getInstance().dh.getCHR(curr);
				for(UserData t : temp2.keySet()) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				chrHologram.getLine(i).removeLine();
				chrHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getCHR()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==5) {
			if(hmrHologram!=null) {
			for(int i = 0; i < Core.getInstance().lh.hmrTop.size(); i++) {
				Double curr = Core.getInstance().lh.hmrTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Double> temp2 = Core.getInstance().dh.getHMR(curr);
				for(UserData t : temp2.keySet()) {
					if(!temp.contains(t)) {
					temp.add(t);
					}
				}
				}
			}
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp.size()) {
				hmrHologram.getLine(i).removeLine();
				hmrHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getHMR()));
				}
			}
			temp.clear();
			}
		}
	}
	
	public void killAll() {
		File f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(killHologram!=null) {killHologram.delete();conf.set("leaderboard-kills-location", killHologram.getWorld().getName() + "," + killHologram.getX() + "," + (killHologram.getY()+3) + "," + killHologram.getZ());}
		if(deathsHologram!=null) {deathsHologram.delete();deathsHologram.delete();conf.set("leaderboard-deaths-location", deathsHologram.getWorld().getName() + "," + deathsHologram.getX() + "," + (deathsHologram.getY()+3) + "," + deathsHologram.getZ());}
		if(killstreakHologram!=null) {killstreakHologram.delete();conf.set("leaderboard-killstreak-location", killstreakHologram.getWorld().getName() + "," + killstreakHologram.getX() + "," + (killstreakHologram.getY()+3) + "," + killstreakHologram.getZ());}
		if(kdrHologram!=null) {kdrHologram.delete();kdrHologram.delete();conf.set("leaderboard-kdr-location", kdrHologram.getWorld().getName() + "," + kdrHologram.getX() + "," + (kdrHologram.getY()+3) + "," + kdrHologram.getZ());}
		if(chrHologram!=null) {chrHologram.delete();chrHologram.delete();conf.set("leaderboard-criticalhitratio-location", chrHologram.getWorld().getName() + "," + chrHologram.getX() + "," + (chrHologram.getY()+3) + "," + chrHologram.getZ());}
		if(hmrHologram!=null) {hmrHologram.delete();hmrHologram.delete();conf.set("leaderboard-hitmissratio-location", hmrHologram.getWorld().getName() + "," + hmrHologram.getX() + "," + (hmrHologram.getY()+3) + "," + hmrHologram.getZ());}
		 try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}