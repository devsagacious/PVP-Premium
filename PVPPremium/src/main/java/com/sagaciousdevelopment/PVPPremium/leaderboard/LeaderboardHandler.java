package com.sagaciousdevelopment.PVPPremium.leaderboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import com.sagaciousdevelopment.PVPPremium.Core;
import com.sagaciousdevelopment.PVPPremium.stats.Stats;

public class LeaderboardHandler implements Listener{
	
	public LeaderboardHandler() {
		setupHolograms();
		format = ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("leaderboard-format"));
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
		}
		
		public void setText(String text) {
			as.setCustomName(text);
		}
	}
	
	
	public List<Integer> killTop;
	public List<Integer> deathTop;
	public List<Double> kdrTop;
	public List<Double> hmrTop;
	public List<Double> chrTop;
	public List<Integer> killstreakTop;
	
	public List<Hologram> hologramsKill;
	public List<Hologram> hologramsDeath;
	public List<Hologram> hologramsKDR;
	public List<Hologram> hologramsHMR;
	public List<Hologram> hologramsCHR;
	public List<Hologram> hologramsKillstreak;
	private String format;
	public void setupHolograms() {
		FileConfiguration conf = Core.getInstance().getConfig();
		if(conf.getBoolean("leaderboard-kills-enabled")) {
			String[] l = conf.getString("leaderboard-kills-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsKill.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header"))));
			hologramsKill.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kills-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.3D);
				hologramsKill.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh(0);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-deaths-enabled")) {
			String[] l = conf.getString("leaderboard-deaths-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsDeath.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header"))));
			hologramsDeath.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-deaths-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.3D);
				hologramsDeath.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh(1);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-kdr-enabled")) {
			String[] l = conf.getString("leaderboard-kdr-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsKDR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-header"))));
			hologramsKDR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-kdr-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.3D);
				hologramsKDR.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh2(0);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-Killstreakstreak-enabled")) {
			String[] l = conf.getString("leaderboard-Killstreakstreak-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsKillstreak.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-Killstreakstreak-header"))));
			hologramsKillstreak.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-Killstreakstreak-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.3D);
				hologramsKillstreak.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh(2);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-hitmissratio-enabled")) {
			String[] l = conf.getString("leaderboard-hitmissratio-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsHMR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-header"))));
			hologramsHMR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-hitmissratio-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.3D);
				hologramsHMR.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh2(2);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
		if(conf.getBoolean("leaderboard-criticalhitratio-enabled")) {
			String[] l = conf.getString("leaderboard-criticalhitratio-location").split(",");
			Location loc = new Location(Bukkit.getWorld(l[0]), Double.valueOf(l[1]), Double.valueOf(l[2]), Double.valueOf(l[3]));
			hologramsCHR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-header"))));
			hologramsCHR.add(new Hologram(loc, ChatColor.translateAlternateColorCodes('&', conf.getString("leaderboard-criticalhitratio-header"))));
			for(int i = 0; i < 10; i++) {
				loc.setY(loc.getY()-0.3D);
				hologramsCHR.add(new Hologram(loc, format.replaceAll("%number%", ""+(i+1)).replaceAll("%name%", "None").replaceAll("%integer%", "0")));
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
				public void run() {
					refresh2(1);
				}
			}, 40L, 20L*conf.getInt("leaderboard-update-time"));
		}
	}
	
	public void saveHologramLocations() {
		File f = new File(Core.getInstance().getDataFolder(), "config.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(!hologramsKill.isEmpty()) {
			Location l = hologramsKill.get(0).getLocation();
			conf.set("leaderboard-kills-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
		if(!hologramsDeath.isEmpty()) {
			Location l = hologramsDeath.get(0).getLocation();
			conf.set("leaderboard-deaths-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
		if(!hologramsKDR.isEmpty()) {
			Location l = hologramsKDR.get(0).getLocation();
			conf.set("leaderboard-kdr-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
		if(!hologramsCHR.isEmpty()) {
			Location l = hologramsCHR.get(0).getLocation();
			conf.set("leaderboard-criticalshitratio-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
		if(!hologramsHMR.isEmpty()) {
			Location l = hologramsHMR.get(0).getLocation();
			conf.set("leaderboard-hitmissratio-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
		}
		if(!hologramsKillstreak.isEmpty()) {
			Location l = hologramsKillstreak.get(0).getLocation();
			conf.set("leaderboard-killstreak-location", l.getWorld().getName() + "," + l.getX() + "," + (l.getY()+3) + "," + l.getZ());
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

    public void refresh(int toRefresh) {
    	List<Stats> order = new ArrayList<Stats>();
    	List<Integer> toorder = new ArrayList<Integer>();
    	if(toRefresh == 0) {
    		for(Stats t : Core.getInstance().sm.getAllStats()) {
    			toorder.add(t.getKills());
    		}
    		sort(toorder);
    		killTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<Stats> temp2 = Core.getInstance().sm.getKills(curr);
    			for(Stats t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		for(int i = 1; i < hologramsKill.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsKill.get(i);
    			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getKills()));
    			}
    		}
    		order.clear();
    		toorder.clear();
    	}else if(toRefresh == 1) {
    		for(Stats t : Core.getInstance().sm.getAllStats()) {
    			toorder.add(t.getDeaths());
    		}
    		sort(toorder);
    		deathTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<Stats> temp2 = Core.getInstance().sm.getDeaths(curr);
    			for(Stats t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		for(int i = 1; i < hologramsDeath.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsDeath.get(i);
    			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getDeaths()));
    			}
    		}
    		order.clear();
    		toorder.clear();
    	}else if(toRefresh == 2) {
    		for(Stats t : Core.getInstance().sm.getAllStats()) {
    			toorder.add(t.getKillstreak());
    		}
    		sort(toorder);
    		killstreakTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<Stats> temp2 = Core.getInstance().sm.getKillstreak(curr);
    			for(Stats t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		for(int i = 1; i < hologramsKillstreak.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsKillstreak.get(i);
    			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getKillstreak()));
    			}
    		}
    		order.clear();
    		toorder.clear();
    	}
    }
    
    private List<Double> sort2(List<Double> toorder){
		Collections.sort(toorder, Collections.reverseOrder());
	    return toorder;
	}

    public void refresh2(int toRefresh) {
    	List<Stats> order = new ArrayList<Stats>();
    	List<Double> toorder = new ArrayList<Double>();
    	if(toRefresh == 0) {
    		for(Stats t : Core.getInstance().sm.getAllStats()) {
    			toorder.add(Double.valueOf(t.getKDR()));
    		}
    		sort2(toorder);
    		kdrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<Stats> temp2 = Core.getInstance().sm.getKDR(curr);
    			for(Stats t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		for(int i = 1; i < hologramsKDR.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsKDR.get(i);
    			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getKDR()));
    			}
    		}
    		order.clear();
    		toorder.clear();
    	}else if(toRefresh == 1) {
    		for(Stats t : Core.getInstance().sm.getAllStats()) {
    			toorder.add(Double.valueOf(t.getCHR()));
    		}
    		sort2(toorder);
    		chrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    		    double curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<Stats> temp2 = Core.getInstance().sm.getCHR(curr);
    			for(Stats t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		for(int i = 1; i < hologramsCHR.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsCHR.get(i);
    			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getCHR()));
    			}
    		}
    		order.clear();
    		toorder.clear();
    	}else if(toRefresh == 2) {
    		for(Stats t : Core.getInstance().sm.getAllStats()) {
    			toorder.add(Double.valueOf(t.getHMR()));
    		}
    		sort2(toorder);
    		hmrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<Stats> temp2 = Core.getInstance().sm.getHMR(curr);
    			for(Stats t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		for(int i = 1; i < hologramsHMR.size(); i++) {
    			if(i-1 < order.size()) {
    			Hologram h = hologramsHMR.get(i);
    			h.setText(format.replaceAll("%number%", ""+i).replaceAll("%name%", order.get(i-1).getName()).replaceAll("%integer%", ""+order.get(i-1).getHMR()));
    			}
    		}
    		order.clear();
    		toorder.clear();
    	}
    }
	
}
