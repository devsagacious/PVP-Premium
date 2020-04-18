package com.Sagacious_.KitpvpStats.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLevel;

public class AntistatsHandler implements Listener{
	
	private HashMap<Player, UUID> last_kill = new HashMap<Player, UUID>();
	private HashMap<UUID, Integer> kills = new HashMap<UUID, Integer>();
	private HashMap<Player, Integer> tasks = new HashMap<Player, Integer>();
	private List<UUID> timeoutList = new ArrayList<UUID>();
	
	private int max_kills;
	private int max_kills_time;
	private int timeout;
	private String message;
	private boolean same_address_check;
	private int xp_per_kill;
	private int xploss_per_kill;
	
	public AntistatsHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		FileConfiguration conf = Core.getInstance().getConfig();
		xp_per_kill = conf.getInt("xp-per-kill");
		xploss_per_kill = conf.getInt("xploss-per-death");
		max_kills = conf.getInt("antistats-max-kills");
		max_kills_time = conf.getInt("antistats-max-kills-time");
		timeout = conf.getInt("antistats-timeout");
		message = ChatColor.translateAlternateColorCodes('&', conf.getString("antistats-message"));
		same_address_check = conf.getBoolean("same-address-check");
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		int t = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
			public void run() {
				if(kills.containsKey(e.getPlayer().getUniqueId())) {
					kills.remove(e.getPlayer().getUniqueId());
				}
			}
		}, max_kills_time*20L, max_kills_time*20L);
		tasks.put(e.getPlayer(), t);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(tasks.containsKey(e.getPlayer())) {
			Bukkit.getScheduler().cancelTask(tasks.get(e.getPlayer()));
			tasks.remove(e.getPlayer());
		}
		if(last_kill.containsKey(e.getPlayer())) {
			last_kill.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			if(Core.getInstance().wh.countstats(e.getPlayer())) {
				UserData d = Core.getInstance().dh.getData(e.getPlayer());
				d.setMisses(d.getMisses()+1);
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player)e.getDamager();
			if(Core.getInstance().wh.countstats(p)) {
				UserData d = Core.getInstance().dh.getData(p);
				if(p.getFallDistance() > 0) {
					d.setCriticals(d.getCriticals()+1);
				}else {
					d.setHits(d.getHits()+1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent e) {
		if(Core.getInstance().wh.countstats(e.getEntity())) {
		if(max_kills>0) {
	       if(e.getEntity().getKiller()instanceof Player) {
	    	   Player p = (Player)e.getEntity().getKiller();
	    	   if(same_address_check&&e.getEntity()instanceof Player) {
	    		   Player f = (Player)e.getEntity();
	    		   if(f.getAddress().getAddress().equals(p.getAddress().getAddress())) {
	    			   return;
	    		   }
	    	   }
	    	   if(last_kill.containsKey(p) && last_kill.get(p).equals(e.getEntity().getUniqueId())) {
	    		   if(kills.containsKey(p.getUniqueId())) {
	    	   kills.put(p.getUniqueId(), kills.get(p.getUniqueId())+1);
	    		   }else {
	    			   kills.put(p.getUniqueId(), 1);
	    		   }
	    	   if(kills.get(p.getUniqueId())>max_kills) {
	    		   p.sendMessage(message);
	    		   timeoutList.add(p.getUniqueId());
	    		   Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
					public void run() {
						if(timeoutList.contains(p.getUniqueId())) {
							timeoutList.remove(p.getUniqueId());
						}
					}
				}, timeout*20L);
	    	   }
	    	   }else {
	    		   last_kill.put(p, e.getEntity().getUniqueId());
	    		   kills.put(p.getUniqueId(), 1);
	    	   }
			UserData u = Core.getInstance().dh.getData(e.getEntity());
			if(u.getXP()>0) {
				int need = u.getLevel().getXPNeeded();
				if(u.getXP()-xploss_per_kill<need) {
					u.setXP(need);
				}else {
					u.setXP(u.getXP()-xploss_per_kill);
				}
			}
			Core.getInstance().kh.endkillstreak(e.getEntity().getKiller(), e.getEntity(), u.getKillstreak());
 		   if(u!=null) {
 			u.setKillstreak(0);u.setDeaths(u.getDeaths()+1);
 		   }
 			if(e.getEntity().getKiller()!=null && e.getEntity().getKiller() instanceof Player) {
 				if(!timeoutList.contains(e.getEntity().getKiller().getUniqueId())) {
 				UserData u2 = Core.getInstance().dh.getData(e.getEntity().getKiller());
 				u2.setKillstreak(u2.getKillstreak()+1);
 				SLevel l = u2.getLevel();
 				u2.setXP(u2.getXP()+xp_per_kill);
 				u2.setKills(u2.getKills()+1);
 				if(!l.equals(u2.getLevel())) {
 					for(String s : u2.getLevel().getCMD()) {
 						if(s.startsWith("chat:")) {
								s = s.substring(5);
								e.getEntity().getKiller().sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%player%", e.getEntity().getKiller().getName())));
								
							}else {
 							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replace("%player%", e.getEntity().getKiller().getName())));
 						}	
 					}
 				}
 				Core.getInstance().kh.reward(e.getEntity().getKiller(), u2.getKillstreak());
 			}
 			}
		}
		}
		}
	}
}
