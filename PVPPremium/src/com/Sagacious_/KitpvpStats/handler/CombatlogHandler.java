package com.Sagacious_.KitpvpStats.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.util.ActionBarAPI;


public class CombatlogHandler implements Listener{
	
	private boolean enabled;
	private String cmbt;
	private String cmbt_at;
	private String cmbt_nl;
	private String actionbar;
	private int time;
	private List<String> cmds;
	private HashMap<UUID, Long> combatlog = new HashMap<UUID, Long>();
	
	private boolean citizens;
	
	public int getCombatLog(UUID u) {
		long now = System.currentTimeMillis()/1000L;
		if(!combatlog.containsKey(u)) {return 0;}
		long timer = Long.valueOf(time) - (now - combatlog.get(u));
		return timer<0?0:Integer.parseInt(String.valueOf(timer));
	}
	
	public CombatlogHandler() {
		FileConfiguration conf = Core.getInstance().getConfig();
		enabled = conf.getBoolean("combatlog-enabled");
		time = conf.getInt("combatlog-time");
		cmds = conf.getStringList("combatlog-commands");
		cmbt = ChatColor.translateAlternateColorCodes('&', conf.getString("combatlog-combatant"));
		cmbt_at = ChatColor.translateAlternateColorCodes('&', conf.getString("combatlog-attacker"));
		cmbt_nl = ChatColor.translateAlternateColorCodes('&', conf.getString("combatlog-nolonger"));
		actionbar = ChatColor.translateAlternateColorCodes('&', conf.getString("combatlog-actionbar-message"));
		citizens = Bukkit.getPluginManager().isPluginEnabled("Citizens");
		if(enabled) {
			Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		}
		if(!actionbar.equals("")) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				long now = new Date().getTime()/1000L;
				for(Entry<UUID, Long> s : combatlog.entrySet()) {
					
					Player p = Bukkit.getPlayer(s.getKey());
					if(p!=null&&p.isOnline()) {
						long timeRem = time - (now - s.getValue());
						
						ActionBarAPI.sendActionBar(p, actionbar.replace("%time%", ""+timeRem));
					}
					
				}
			}
		}.runTaskTimer(Core.getInstance(), 20L, 20L);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(getCombatLog(e.getPlayer().getUniqueId())>0) {
			e.getPlayer().setHealth(0);
			for(String s : cmds) {
				if(s.startsWith("chat:")) {
					if(e.getPlayer().getKiller()!=null) {
						e.getPlayer().getKiller().sendMessage(ChatColor.translateAlternateColorCodes('&', s.substring(5).replace("%player%", e.getPlayer().getName())));
					}
				}else {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s.replace("%player%", e.getPlayer().getName())));
				}
			}
			combatlog.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof Player) {
		combatlog.remove(e.getEntity().getUniqueId());
	}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(citizens) {if(net.citizensnpcs.api.CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {return;}}
		if(e.getEntity() instanceof Player&&e.getDamager() instanceof Player) {
			if(getCombatLog(e.getEntity().getUniqueId())==0) {
				e.getEntity().sendMessage(cmbt.replace("%player%", e.getDamager().getName()));
			}
			if(!combatlog.containsKey(e.getDamager().getUniqueId())) {
				e.getDamager().sendMessage(cmbt_at.replace("%player%", e.getEntity().getName()));
			}
			long now = new Date().getTime()/1000L;
			combatlog.put(e.getEntity().getUniqueId(), now);
			combatlog.put(e.getDamager().getUniqueId(), now);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
				public void run() {
					if(combatlog.containsKey(e.getEntity().getUniqueId())&&getCombatLog(e.getEntity().getUniqueId())==0&&((Player)e.getEntity()).isOnline()) {
						e.getEntity().sendMessage(cmbt_nl);
						combatlog.remove(e.getEntity().getUniqueId());
					}
					if(combatlog.containsKey(e.getDamager().getUniqueId())&&getCombatLog(e.getDamager().getUniqueId())==0&&((Player)e.getDamager()).isOnline()) {
						e.getDamager().sendMessage(cmbt_nl);
						combatlog.remove(e.getDamager().getUniqueId());
					}
				}
			}, time*20L);
		}else if(e.getEntity()instanceof Player && e.getDamager() instanceof Projectile) {
			Projectile p = (Projectile) e.getDamager();
			if(p.getShooter() instanceof Player) {
				Player ps = (Player)p.getShooter();
				if(getCombatLog(e.getEntity().getUniqueId())==0) {
					e.getEntity().sendMessage(cmbt.replace("%player%", ps.getName()));
				}
				if(!combatlog.containsKey(ps.getUniqueId())) {
					ps.sendMessage(cmbt_at.replace("%player%", e.getEntity().getName()));
				}
				long now = new Date().getTime()/1000L;
				combatlog.put(e.getEntity().getUniqueId(), now);
				combatlog.put(ps.getUniqueId(), now);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
					public void run() {
						if(combatlog.containsKey(e.getEntity().getUniqueId())&&getCombatLog(e.getEntity().getUniqueId())==0&&((Player)e.getEntity()).isOnline()) {
							e.getEntity().sendMessage(cmbt_nl);
							combatlog.remove(e.getEntity().getUniqueId());
						}
						if(combatlog.containsKey(ps.getUniqueId())&&getCombatLog(ps.getUniqueId())==0&&ps.isOnline()) {
							ps.sendMessage(cmbt_nl);
							combatlog.remove(ps.getUniqueId());
						}
					}
				}, time*20L);
			}
		}
	}
}
