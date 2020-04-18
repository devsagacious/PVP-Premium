package com.Sagacious_.KitpvpStats.leaderboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.data.UserData;

public class LeaderboardHandler implements Listener{
	
	private File f;
	public LeaderboardHandler() {
		f = new File(Core.getInstance().getDataFolder(), "leaderboard.yml");
		if(!f.exists()) {
			 try (InputStream in = Core.getInstance().getResource("leaderboard.yml")) {
                 Files.copy(in, f.toPath());
             } catch (IOException e) {
                 e.printStackTrace();
             }
		}
		setupHolograms();
	}
	
	public boolean isLeaderboard(ArmorStand as) {
		for(Hologram k : hologramsKill) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : hologramsDeath) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : hologramsKillstreak) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : hologramsKDR) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : hologramsCHR) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		for(Hologram k : hologramsHMR) {
			if(k.getStand().equals(as)) {
				return true;
			}
		}
		return false;
	}
	
	public class Hologram {
		private ArmorStand as;
		private Location loc;
		public Hologram(Location loc, String text) {
			this.loc = loc;
			as = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setVisible(false);
			as.setBasePlate(false);
			as.setCustomName(text);
			as.setCustomNameVisible(true);
			as.setRemoveWhenFarAway(false);
		}
		
		public Location getLocation() {
			return loc;
		}
		
		public void teleport(Location loc) {
			this.loc = loc;
			as.teleport(loc);
		}
		
		public ArmorStand getStand() {
			return as;
		}
		
		public void kill() {
			as.remove();
			if(!as.isDead()) {
				as.setHealth(0);
			}
		}
		
		public void setText(String text) {
			as.setCustomName(text);
		}
	}
	
	
	public List<Integer> killTop = new ArrayList<Integer>();
	public List<Integer> deathTop = new ArrayList<Integer>();
	public List<Double> kdrTop = new ArrayList<Double>();
	public List<Double> hmrTop = new ArrayList<Double>();
	public List<Double> chrTop = new ArrayList<Double>();
	public List<Integer> killstreakTop = new ArrayList<Integer>();
	
	public List<Hologram> hologramsKill = new ArrayList<Hologram>();
	public List<Hologram> hologramsDeath = new ArrayList<Hologram>();
	public List<Hologram> hologramsKDR = new ArrayList<Hologram>();
	public List<Hologram> hologramsHMR = new ArrayList<Hologram>();
	public List<Hologram> hologramsCHR = new ArrayList<Hologram>();
	public List<Hologram> hologramsKillstreak = new ArrayList<Hologram>();
	private String format;
	public void setupHolograms() {
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-format"));
		if(conf.getBoolean("leaderboard-kills-enabled")) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-kills-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsKill.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.25D);
				hologramsKill.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			loc.setY(loc.getY()-0.25D);
			hologramsKill.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh(0);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-deaths-enabled")) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-deaths-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsDeath.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.25D);
				hologramsDeath.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			loc.setY(loc.getY()-0.25D);
			hologramsDeath.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh(1);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-kdr-enabled")) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-kdr-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsKDR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.25D);
				hologramsKDR.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0.0")));
			}
			loc.setY(loc.getY()-0.25D);
			hologramsKDR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh2(0);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-killstreak-enabled")) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-killstreak-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsKillstreak.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.25D);
				hologramsKillstreak.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			loc.setY(loc.getY()-0.25D);
			hologramsKillstreak.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-killstreak-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh(2);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-hitmissratio-enabled")) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-hitmissratio-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsHMR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.25D);
				hologramsHMR.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0.0")));
			}
			loc.setY(loc.getY()-0.25D);
			hologramsHMR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh2(2);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-criticalhitratio-enabled")) {
			if(!Core.getInstance().useHolographic) {
			String[] l = conf.getString("leaderboard-criticalhitratio-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsCHR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.25D);
				hologramsCHR.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0.0")));
			}
			loc.setY(loc.getY()-0.25D);
			hologramsCHR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-footer"))));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh2(1);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
	}
	
	private void killHolograms(List<Hologram> h) {
		for(Hologram s : h) {
			s.as.remove();
		}
	}
	
	public void saveHologramLocations() {
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(!hologramsKill.isEmpty()) {
			Location l = hologramsKill.get(0).getLocation();
			conf.set("leaderboard-kills-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2.75) + "," + l.getZ());
			killHolograms(hologramsKill);
		}
		if(!hologramsDeath.isEmpty()) {
			Location l = hologramsDeath.get(0).getLocation();
			conf.set("leaderboard-deaths-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2.75) + "," + l.getZ());
			killHolograms(hologramsDeath);
		}
		if(!hologramsKDR.isEmpty()) {
			Location l = hologramsKDR.get(0).getLocation();
			conf.set("leaderboard-kdr-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2.75) + "," + l.getZ());
			killHolograms(hologramsKDR);
		}
		if(!hologramsCHR.isEmpty()) {
			Location l = hologramsCHR.get(0).getLocation();
			conf.set("leaderboard-criticalhitratio-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2.75) + "," + l.getZ());
			killHolograms(hologramsCHR);
		}
		if(!hologramsHMR.isEmpty()) {
			Location l = hologramsHMR.get(0).getLocation();
			conf.set("leaderboard-hitmissratio-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2.75) + "," + l.getZ());
			killHolograms(hologramsHMR);
		}
		if(!hologramsKillstreak.isEmpty()) {
			Location l = hologramsKillstreak.get(0).getLocation();
			conf.set("leaderboard-killstreak-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+2.75) + "," + l.getZ());
			killHolograms(hologramsKillstreak);
		}
        try {
			conf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<Integer> sort(List<Integer> toorder){
		Collections.sort(toorder, Collections.reverseOrder());
	    return toorder;
	}
	public List<UserData> orderedKills = new ArrayList<UserData>();
	public List<UserData> orderedDeaths = new ArrayList<UserData>();
	public List<UserData> orderedKillstreak = new ArrayList<UserData>();
	public List<UserData> orderedCHR = new ArrayList<UserData>();
	public List<UserData> orderedKDR = new ArrayList<UserData>();
	public List<UserData> orderedHMR = new ArrayList<UserData>();

    public void refresh(int toRefresh) {
    	boolean changed = false;
    	List<UserData> order = new ArrayList<UserData>();
    	List<Integer> toorder = new ArrayList<Integer>();
    	if(toRefresh == 0) {
    		for(UserData t : Core.getInstance().dh.getAllUserData()) {
    			toorder.add(t.getKills());
    		}
    		sort(toorder);
    		killTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Integer> temp2 = Core.getInstance().dh.getKills(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedKills = new ArrayList<UserData>(order);
    		for(int i = 1; i < hologramsKill.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsKill.get(i);
    			String f = format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getKills());
    			changed=f==h.getStand().getCustomName()&&i-1<10;
    			h.setText(f);
    			}
    		}

    	}else if(toRefresh == 1) {
    		for(UserData t : Core.getInstance().dh.getAllUserData()) {
    			toorder.add(t.getDeaths());
    		}
    		sort(toorder);
    		deathTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    				HashMap<UserData, Integer> temp2 = Core.getInstance().dh.getDeaths(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedDeaths = new ArrayList<UserData>(order);
    		for(int i = 1; i < hologramsDeath.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsDeath.get(i);
    			String f = format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getDeaths());
    			changed=f==h.getStand().getCustomName()&&i-1<10;
    			h.setText(f);
    			}
    		}

    	}else if(toRefresh == 2) {
    		for(UserData t : Core.getInstance().dh.getAllUserData()) {
    			toorder.add(t.getTopKillstreak());
    		}
    		sort(toorder);
    		killstreakTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Integer> temp2 = Core.getInstance().dh.getKillstreak(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedKillstreak = new ArrayList<UserData>(order);
    		for(int i = 1; i < hologramsKillstreak.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsKillstreak.get(i);
    			String f = format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getTopKillstreak());
    			changed=f==h.getStand().getCustomName()&&i-1<10;
    			h.setText(f);
    			}
    		}

    	}
    	if(changed) {
    		Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(toRefresh, changed));
    	}
    }
    
    private List<Double> sort2(List<Double> toorder){
		Collections.sort(toorder, Collections.reverseOrder());
	    return toorder;
	}

    public void refresh2(int toRefresh) {
    	boolean changed = false;
    	List<UserData> order = new ArrayList<UserData>();
    	List<Double> toorder = new ArrayList<Double>();
    	if(toRefresh == 0) {
    		for(UserData t : Core.getInstance().dh.getAllUserData()) {
    			toorder.add(Double.valueOf(t.getKDR()));
    		}
    		sort2(toorder);
    		kdrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().dh.getKDR(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedKDR = new ArrayList<UserData>(order);
    		for(int i = 1; i < hologramsKDR.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsKDR.get(i);
    			String f = format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getKDR());
    			changed=f==h.getStand().getCustomName()&&i-1<10;
    			h.setText(f);
    			}
    		}

    	}else if(toRefresh == 1) {
    		for(UserData t : Core.getInstance().dh.getAllUserData()) {
    			toorder.add(Double.valueOf(t.getCHR()));
    		}
    		sort2(toorder);
    		chrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    		    double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().dh.getCHR(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedCHR = new ArrayList<UserData>(order);
    		for(int i = 1; i < hologramsCHR.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsCHR.get(i);
    			String f = format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getCHR());
    			changed=f==h.getStand().getCustomName()&&i-1<10;
    			h.setText(f);
    			}
    		}

    	}else if(toRefresh == 2) {
    		for(UserData t : Core.getInstance().dh.getAllUserData()) {
    			toorder.add(Double.valueOf(t.getHMR()));
    		}
    		sort2(toorder);
    		hmrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().dh.getHMR(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedHMR = new ArrayList<UserData>(order);
    		for(int i = 1; i < hologramsHMR.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsHMR.get(i);
    			String f = format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getHMR());
    			changed=f==h.getStand().getCustomName()&&i-1<10;
    			h.setText(f);
    			}
    		}

    	}
    	if(changed) {
    		Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(toRefresh+3, changed));
    	}
    }
	
}
