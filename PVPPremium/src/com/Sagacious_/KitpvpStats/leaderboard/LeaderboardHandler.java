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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLevel;

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
	
	
	public List<Integer> killTop = new ArrayList<Integer>();
	public List<Integer> deathTop = new ArrayList<Integer>();
	public List<Double> kdrTop = new ArrayList<Double>();
	public List<Double> hmrTop = new ArrayList<Double>();
	public List<Double> chrTop = new ArrayList<Double>();
	public List<Integer> killstreakTop = new ArrayList<Integer>();
	public List<Double> unqiuekillTop = new ArrayList<Double>();
	public List<SLevel> levelTop = new ArrayList<SLevel>();
	public List<Double> bountiesTop = new ArrayList<Double>();
	public void setupHolograms() {
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(conf.getInt("leaderboard-update-time")>0) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
			public void run() {
				refresh(0, false);
				refresh(1, false);
				refresh(2, false);
				refresh2(0, false);
				refresh2(1, false);
				refresh2(2, false);
				refresh2(5, false);
				if(Core.getInstance().getBountyManager()!=null&&Core.getInstance().getBountyManager().isEnabled()) {refresh2(3, false);}
				refresh3(false);
			}
		}, 60L, 20L*conf.getInt("leaderboard-update-time"));
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
	public List<UserData> orderedLevel = new ArrayList<UserData>();
	public List<UserData> orderedBounties = new ArrayList<UserData>();
	public List<UserData> orderedUnqiuekills = new ArrayList<UserData>();

    public void refresh(int toRefresh, boolean manual) {
    	List<UserData> order = new ArrayList<UserData>();
    	List<Integer> toorder = new ArrayList<Integer>();
    	if(toRefresh == 0) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(t.getKills());
    		}
    		toorder = sort(toorder);
    		killTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Integer> temp2 = Core.getInstance().getDataHandler().getKills(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedKills = new ArrayList<UserData>(order);

    	}else if(toRefresh == 1) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(t.getDeaths());
    		}
    		toorder = sort(toorder);
    		deathTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    				HashMap<UserData, Integer> temp2 = Core.getInstance().getDataHandler().getDeaths(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedDeaths = new ArrayList<UserData>(order);

    	}else if(toRefresh == 2) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(t.getTopKillstreak());
    		}
    		toorder = sort(toorder);
    		killstreakTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			int curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Integer> temp2 = Core.getInstance().getDataHandler().getKillstreak(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedKillstreak = new ArrayList<UserData>(order);

    	}
    		Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(toRefresh, manual));
    }
    
    private List<Double> sort2(List<Double> toorder){
		Collections.sort(toorder, Collections.reverseOrder());
	    return toorder;
	}

    public void refresh2(int toRefresh, boolean manual) {
    	List<UserData> order = new ArrayList<UserData>();
    	List<Double> toorder = new ArrayList<Double>();
    	if(toRefresh == 0) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(Double.valueOf(t.getKDR()));
    		}
    		toorder = sort2(toorder);
    		kdrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getKDR(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedKDR = new ArrayList<UserData>(order);

    	}else if(toRefresh == 1) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(Double.valueOf(t.getCHR()));
    		}
    		toorder = sort2(toorder);
    		chrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    		    double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getCHR(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedCHR = new ArrayList<UserData>(order);

    	}else if(toRefresh == 2) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(Double.valueOf(t.getHMR()));
    		}
    		toorder = sort2(toorder);
    		hmrTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getHMR(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedHMR = new ArrayList<UserData>(order);
    	}else if(toRefresh==3) {
    		for(Bounty t : Core.getInstance().getBountyManager().getBounties()) {
    			if(t.active) {
    			toorder.add(t.getAmount());
    		}
    		}
    		toorder = sort2(toorder);
    		bountiesTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<Bounty, Double> temp2 = Core.getInstance().getBountyManager().getBounties(curr);
    			for(Bounty t : temp2.keySet()) {
    				if(!order.contains(Core.getInstance().getDataHandler().getData(t.getUuidTarget()))) {
    				order.add(Core.getInstance().getDataHandler().getData(t.getUuidTarget()));
    				}
    			}
    			}
    		}
    		orderedBounties = new ArrayList<UserData>(order);
    	}else if(toRefresh == 5) {
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(Double.valueOf(t.getUniqueKills().entrySet().size()));
    		}
    		toorder = sort2(toorder);
    		unqiuekillTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			double curr = toorder.get(i);
    			if(order.size() < 50) {
    			HashMap<UserData, Double> temp2 = Core.getInstance().getDataHandler().getUnqiueKills(curr);
    			for(UserData t : temp2.keySet()) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedUnqiuekills = new ArrayList<UserData>(order);
    	}
    		Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(toRefresh+3, manual));
    }
    
    private List<SLevel> sort3(List<SLevel> toorder){
    	List<Integer> need = new ArrayList<Integer>();
    	for(SLevel f : toorder) {
    		need.add(f.getXPNeeded());
    	}
		Collections.sort(need, Collections.reverseOrder());
		List<SLevel> returner = new ArrayList<SLevel>();
		for(int f : need) {
			for(int i = 0; i < toorder.size(); i++) {
				if(toorder.get(i).getXPNeeded()==f) {
					returner.add(toorder.get(i));
				}
			}
		}
	    return returner;
	}
    
    public void refresh3(boolean manual) {
    	List<UserData> order = new ArrayList<UserData>();
    	List<SLevel> toorder = new ArrayList<SLevel>();
    		for(UserData t : Core.getInstance().getDataHandler().getAllUserData()) {
    			toorder.add(t.getLevel());
    		}
    		toorder = sort3(toorder);
    		levelTop = toorder;
    		for(int i = 0; i < toorder.size(); i++) {
    			SLevel curr = toorder.get(i);
    			if(order.size() < 50) {
    			List<UserData> temp2 = Core.getInstance().getDataHandler().getLevel(curr);
    			for(UserData t : temp2) {
    				if(!order.contains(t)) {
    				order.add(t);
    				}
    			}
    			}
    		}
    		orderedLevel = new ArrayList<UserData>(order);
    		Bukkit.getPluginManager().callEvent(new LeaderboardUpdateEvent(7, manual));
}
    }
