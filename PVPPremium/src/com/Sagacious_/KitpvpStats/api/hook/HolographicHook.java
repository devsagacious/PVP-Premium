package com.Sagacious_.KitpvpStats.api.hook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLevel;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HolographicHook implements Listener{
	public Hologram killHologram = null;
	public Hologram deathsHologram = null;
	public Hologram killstreakHologram = null;
	public Hologram kdrHologram = null;
	public Hologram hmrHologram = null;
	public Hologram chrHologram = null;
	public Hologram levelHologram = null;
	public Hologram bountyHologram = null;
	public Hologram uniquekillHologram = null;
	
	private String format;
	public Location lke_l;
	public Location lde_l;
	public Location lkie_l;
	public Location kdr_l;
	public Location chr_l;
	public Location hmr_l;
	public Location level_l;
	public Location bounty_l;
	public Location unique_l;
	
	private boolean lke = false;
	private boolean lde = false;
	private boolean lkie = false;
	private boolean kdr = false;
	private boolean chr = false;
	private boolean hmr = false;
	private boolean level = false;
	private boolean bounty = false;
	private boolean uniquekills = false;
	
	private void deleteIfExists(Location l) {
		if(l!=null&&l.getWorld()!=null&&l.getWorld().getEntities()!=null) {
		for(Entity e : l.getWorld().getEntities()) {
			if(e.getLocation().equals(l)) {
				if(HologramsAPI.isHologramEntity(e)) {
					e.remove();
				}
			}
		}
		}
	}

	public void teleport(String f, Hologram h, Location l) {
		if(h==null||h.isDeleted()) {
			setupNew(f, l);
			return;
		}
		h.teleport(l);
		if(f.equals("hmr")) {
			hmr_l =l;
		}else if(f.equals("chr")) {
			chr_l =l;
		}else if(f.equals("kills")) {
			lke_l =l;
		}else if(f.equals("deaths")) {
			lde_l =l;
		}else if(f.equals("level")) {
			level_l=l;
		}else if(f.equals("bounty")) {
			bounty_l=l;
		}else if(f.equals("killstreak")) {
			lkie_l =l;
		}else if(f.equals("kdr")) {
			kdr_l=l;
		}else if(f.equals("uniquekills")) {
			unique_l=l;
		}
	}
	
	public void setupNew(String holo, Location l) {
		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "leaderboard.yml"));
		if(holo.equals("lke")) {
			lke_l=l;
			deleteIfExists(lke_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lke_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header")));
			killHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer")));
		}else if(holo.equals("lde")) {
			lde_l=l;
			deleteIfExists(lde_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lde_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header")));
			deathsHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer")));
		}else if(holo.equals("lkie")) {
			lkie_l=l;
			deleteIfExists(lkie_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), lkie_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header")));
			killstreakHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer")));
		}
		else if(holo.equals("kdr")) {
			kdr_l=l;
			deleteIfExists(kdr_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), kdr_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-header")));
			kdrHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-footer")));
		}else if(holo.equals("chr")) {
			chr_l=l;
			deleteIfExists(chr_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), chr_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-header")));
			chrHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-footer")));
		}else if(holo.equals("hmr")) {
			hmr_l=l;
			deleteIfExists(hmr_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), hmr_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-header")));
			hmrHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-footer")));
		}else if(holo.equals("level")) {
			level_l=l;
			deleteIfExists(level_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), level_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-level-header")));
			levelHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-level-footer")));
		}
		else if(holo.equals("bounties")) {
			bounty_l=l;
			deleteIfExists(bounty_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), bounty_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-bounty-header")));
			bountyHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-bounty-footer")));
		}
		else if(holo.equals("uniquekills")) {
			unique_l=l;
			deleteIfExists(unique_l);
			Hologram h = HologramsAPI.createHologram(Core.getInstance(), unique_l);
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-uniquekills-header")));
			uniquekillHologram = h;
			for(int i = 0; i < 10; i++) {
				h.appendTextLine(format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0"));
			}
			h.appendTextLine(ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-uniquekills-footer")));
		}
	}
	
	public HolographicHook(){
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "leaderboard.yml"));
		lke = conf.getBoolean("leaderboard-kills-enabled");
		lde = conf.getBoolean("leaderboard-deaths-enabled");
		lkie = conf.getBoolean("leaderboard-killstreak-enabled");
		kdr = conf.getBoolean("leaderboard-kdr-enabled");
		chr = conf.getBoolean("leaderboard-criticalhitratio-enabled");
		hmr = conf.getBoolean("leaderboard-hitmissratio-enabled");
		bounty = conf.getBoolean("leaderboard-bounty-enabled");
		uniquekills = conf.getBoolean("leaderboard-uniquekills-enabled");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
        if(lke) {
        	String[] l = conf.getString("leaderboard-kills-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			lke_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("lke", lke_l);
        }
        if(lde) {
        	String[] l = conf.getString("leaderboard-deaths-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
		lde_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("lde", lde_l);
        }
        if(lkie) {
        	String[] l = conf.getString("leaderboard-killstreak-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			lkie_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("lkie", lkie_l);
        }
        if(kdr) {
        	String[] l = conf.getString("leaderboard-kdr-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			kdr_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("kdr", kdr_l);
        }
        if(chr) {
        	String[] l = conf.getString("leaderboard-criticalhitratio-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			chr_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("chr", chr_l);
        }
        if(hmr) {
        	String[] l = conf.getString("leaderboard-hitmissratio-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			hmr_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("hmr", hmr_l);
        }
        if(level) {
        	String[] l = conf.getString("leaderboard-level-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			level_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("level", level_l);
        }
        if(bounty) {
        	String[] l = conf.getString("leaderboard-bounty-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			bounty_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("bounties", bounty_l);
        }
        if(uniquekills) {
        	String[] l = conf.getString("leaderboard-uniquekills-location").split(",");
        	if(Bukkit.getWorld(l[0])==null) {
        		Core.getInstance().getLogger().info("World '" + l[0] + "' does not exist, change it in your leaderboard.yml");
        		return;
        	}
			unique_l = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
        	setupNew("uniquekills", unique_l);
        }
	}
	
	private HashMap<Bounty, Double> temp2 = new HashMap<Bounty, Double>();
	private List<Bounty> bountyTop = new ArrayList<Bounty>();
	public List<Bounty> getBounty(double amount){
		List<Bounty> b = new ArrayList<Bounty>();
		for(Double s : temp2.values()) {
			if(amount==s) {
				for(Entry<Bounty, Double> s2 : temp2.entrySet()) {
					if(s2.getValue()==s) {
						b.add(s2.getKey());
					}
				}
			}
		}
		return b;
	}
	
	private void sortBountyTops() {
		List<Double> totals = new ArrayList<Double>(temp2.values());
		Collections.sort(totals, Collections.reverseOrder());
		bountyTop.clear();
		List<Double> had = new ArrayList<Double>();
		for(Double t : totals) {
			if(had.contains(t)) {return;}
			bountyTop.addAll(getBounty(t));had.add(t);
		}
	}
	
	

	@EventHandler
	public void onUpdate(LeaderboardUpdateEvent e) {
		List<UserData> temp = new ArrayList<UserData>();
		if(e.getLeaderboard()==0) {
			if(killHologram!=null&&!killHologram.isDeleted()) {
			for(int i = 0; i < Core.getInstance().getLeaderboardHandler().killTop.size(); i++) {
				int curr = Core.getInstance().getLeaderboardHandler().killTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Integer> temp2 = Core.getInstance().getDataHandler().getKills(curr);
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
			if(deathsHologram!=null&&!deathsHologram.isDeleted()) {
			for(int i = 0; i < Core.getInstance().getLeaderboardHandler().deathTop.size(); i++) {
				int curr = Core.getInstance().getLeaderboardHandler().deathTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Integer> temp2 = Core.getInstance().getDataHandler().getDeaths(curr);
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
			if(killstreakHologram!=null&&!killstreakHologram.isDeleted()) {
			for(int i = 0; i < Core.getInstance().getLeaderboardHandler().killstreakTop.size(); i++) {
				int curr = Core.getInstance().getLeaderboardHandler().killstreakTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Integer> temp2 = Core.getInstance().getDataHandler().getKillstreak(curr);
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
				killstreakHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getTopKillstreak()));
				}
			}
			temp.clear();
			}
		}else if(e.getLeaderboard()==3) {
			if(kdrHologram!=null&&!kdrHologram.isDeleted()) {
			for(int i = 0; i < Core.getInstance().getLeaderboardHandler().kdrTop.size(); i++) {
				Double curr = Core.getInstance().getLeaderboardHandler().kdrTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getKDR(curr);
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
			if(chrHologram!=null&&!chrHologram.isDeleted()) {
			for(int i = 0; i < Core.getInstance().getLeaderboardHandler().chrTop.size(); i++) {
				Double curr = Core.getInstance().getLeaderboardHandler().chrTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getCHR(curr);
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
			if(hmrHologram!=null&&!hmrHologram.isDeleted()) {
			for(int i = 0; i < Core.getInstance().getLeaderboardHandler().hmrTop.size(); i++) {
				Double curr = Core.getInstance().getLeaderboardHandler().hmrTop.get(i);
				if(temp.size() < 10) {
				HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getHMR(curr);
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
		}else if(e.getLeaderboard()==7) {
			if(levelHologram!=null&&!levelHologram.isDeleted()) {
				for(int i = 0; i < Core.getInstance().getLeaderboardHandler().levelTop.size(); i++) {
					SLevel curr = Core.getInstance().getLeaderboardHandler().levelTop.get(i);
					if(temp.size() < 10) {
					List<UserData> temp2 = Core.getInstance().getDataHandler().getLevel(curr);
					for(UserData t : temp2) {
						if(!temp.contains(t)) {
						temp.add(t);
						}
					}
					}
				}
				for(int i = 1; i < 11; i++) {
					if(i-1 < temp.size()) {
					levelHologram.getLine(i).removeLine();
					levelHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp.get(i-1).getName()).replaceAll("%integer%", ""+temp.get(i-1).getLevel().getName()));
					}
				}
				temp.clear();
				}
		}else if(e.getLeaderboard()==6) {
			if(bountyHologram!=null&&!bountyHologram.isDeleted()) {
				temp2 = Core.getInstance().getBountyManager().getBountiesTotal();
						sortBountyTops();
				for(int i = 1; i < 11; i++) {
					if(i-1 < bountyTop.size()) {
						bountyHologram.getLine(i).removeLine();
						bountyHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", Bukkit.getOfflinePlayer(bountyTop.get(i-1).getUuidTarget()).getName()).replaceAll("%integer%", ""+bountyTop.get(i-1).getAmount()));
					}
				}
				
				}
		}else if(e.getLeaderboard()==8) {
			if(uniquekillHologram!=null&&!uniquekillHologram.isDeleted()) {
			List<UserData> temp2 = new ArrayList<UserData>(Core.getInstance().getLeaderboardHandler().orderedUnqiuekills);
			for(int i = 1; i < 11; i++) {
				if(i-1 < temp2.size()) {
					uniquekillHologram.removeLine(i);
				uniquekillHologram.insertTextLine(i, format.replaceAll("%number%", ""+i).replaceAll("%name%", temp2.get(i-1).getName()).replaceAll("%integer%", ""+temp2.get(i-1).getUniqueKills().entrySet().size()));
			}
			}
			temp2.clear();
		}
	}
	}
	
	public void killAll() {
		File f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(lke_l!=null) {if(killHologram!=null) {killHologram.delete();}conf.set("leaderboard-kills-location", lke_l.getWorld().getName() + "," + lke_l.getX() + "," + (lke_l.getY()) + "," + lke_l.getZ());}
		if(lde_l!=null) {if(deathsHologram!=null) {deathsHologram.delete();}conf.set("leaderboard-deaths-location", lde_l.getWorld().getName() + "," + lde_l.getX() + "," + (lde_l.getY()) + "," + lde_l.getZ());}
		if(lkie_l!=null) {if(killstreakHologram!=null) {killstreakHologram.delete();}conf.set("leaderboard-killstreak-location", lkie_l.getWorld().getName() + "," + lkie_l.getX() + "," + (lkie_l.getY()) + "," + lkie_l.getZ());}
		if(kdr_l!=null) {if(kdrHologram!=null) {kdrHologram.delete();}conf.set("leaderboard-kdr-location", kdr_l.getWorld().getName() + "," + kdr_l.getX() + "," + (kdr_l.getY()) + "," + kdr_l.getZ());}
		if(chr_l!=null) {if(chrHologram!=null) {chrHologram.delete();}conf.set("leaderboard-criticalhitratio-location", chr_l.getWorld().getName() + "," + chr_l.getX() + "," + (chr_l.getY()) + "," + chr_l.getZ());}
		if(hmr_l!=null) {if(hmrHologram!=null) {hmrHologram.delete();}conf.set("leaderboard-hitmissratio-location", hmr_l.getWorld().getName() + "," + hmr_l.getX() + "," + (hmr_l.getY()) + "," + hmr_l.getZ());}
		if(level_l!=null) {if(levelHologram!=null) { levelHologram.delete();}conf.set("leaderboard-level-location", level_l.getWorld().getName() + "," + level_l.getX() + "," + (level_l.getY()) + "," + level_l.getZ());}
		if(bounty_l!=null) {if(bountyHologram!=null) {bountyHologram.delete();}conf.set("leaderboard-bounty-location", bounty_l.getWorld().getName() + "," + bounty_l.getX() + "," + (bounty_l.getY()) + "," + bounty_l.getZ());}
		if(unique_l!=null) {if(uniquekillHologram!=null) {uniquekillHologram.delete();}conf.set("leaderboard-uniquekills-location", unique_l.getWorld().getName() + "," + unique_l.getX() + "," + (unique_l.getY()) + "," + unique_l.getZ());}
		try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}