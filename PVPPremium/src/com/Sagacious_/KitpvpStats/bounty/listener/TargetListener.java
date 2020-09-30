package com.Sagacious_.KitpvpStats.bounty.listener;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.bounty.BountyManager;
import com.Sagacious_.KitpvpStats.bounty.gui.TargetGUI;

public class TargetListener implements Listener{
	private TargetGUI gui;
	private String bountyOffline;
	private String targetKilled;
	private String targetSelected;
	private BountyManager bm = Core.getInstance().getBountyManager();
	
	public TargetListener() {
		gui = new TargetGUI();
		FileConfiguration conf = bm.conf;
		bountyOffline = ChatColor.translateAlternateColorCodes('&', conf.getString("bounty-offline"));
		targetKilled = ChatColor.translateAlternateColorCodes('&', conf.getString("target-killed"));
		targetSelected = ChatColor.translateAlternateColorCodes('&', conf.getString("target-selected"));
		updateLocations();
	}
	
	public void dead(Player p, Player l) {
		if(sdfe.containsValue(p)) {
			for(Player f : sdfe.keySet()) {
				sdfe.remove(f);
				if(!f.equals(l)) {
					f.sendMessage(targetKilled.replace("%killer%", l.getName()));
				}
			}
		}
	}
	
	private HashMap<Player, Player> sdfe = new HashMap<Player, Player>();
	private void updateLocations() {
		new BukkitRunnable() {
			public void run() {
				for(Entry<Player, Player> p : sdfe.entrySet()) {
					p.getKey().setCompassTarget(p.getValue().getLocation());
				}
			}
		}.runTaskTimer(Core.getInstance(), 40L, 40L);
	}
	
	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getItem()!=null&&e.getItem().getItemMeta()!=null) {
				if(bm.ts!=null) {
					if(e.getItem().getType().equals(bm.ts.is.getType())) {
				if(e.getItem().getItemMeta().getDisplayName().equals(bm.ts.is.getItemMeta().getDisplayName())) {
					gui.openInventory(e.getPlayer(), 0);
				}
				}
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(gui.s.containsKey(e.getPlayer().getUniqueId())) {
			gui.s.remove(e.getPlayer().getUniqueId());
		}
		if(sdfe.containsKey(e.getPlayer())) {
			sdfe.remove(e.getPlayer());
		}
		if(sdfe.containsValue(e.getPlayer())) {
			for(Player f : sdfe.keySet()) {
				sdfe.remove(f);
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(gui.s.containsKey(e.getPlayer().getUniqueId())) {
			gui.s.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			if(gui.s.containsKey(p.getUniqueId())) {
				if(e.getCurrentItem()!=null&&!e.getCurrentItem().getType().equals(Material.AIR)) {
					e.setCancelled(true);
					int z = gui.s.get(p.getUniqueId());
					if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
						p.closeInventory();
						Bounty b = bm.getBounties().get(z+e.getSlot());
						if(Core.getInstance().getDataHandler().stats.get(b.getUuidTarget()).online) {
							Player target = Bukkit.getPlayer(b.getUuidTarget());
							p.setCompassTarget(target.getLocation());
							p.sendMessage(targetSelected.replace("%bounty%", target.getName()).replace("%distance%", ""+Integer.valueOf((int) p.getLocation().distance(target.getLocation()))));
						    sdfe.put(p, target);
						    if(Core.getInstance().getBountyManager().ts!=null) {
						    	ItemStack is = Core.getInstance().getBountyManager().ts.is;
						    	if(!p.getInventory().contains(is)) {
						    		p.getInventory().addItem(is);
						    	}
						    }
						}else {
							p.sendMessage(bountyOffline);
						}
					}else if(e.getCurrentItem().getType().equals(Material.ARROW)) {
						p.closeInventory();
						if(e.getCurrentItem().getItemMeta().getDisplayName().contains(">")) {
							gui.openInventory(p, z+1);
						}else {
							gui.openInventory(p, z-1);
						}
					}
				}
			}
		}
	}
}
