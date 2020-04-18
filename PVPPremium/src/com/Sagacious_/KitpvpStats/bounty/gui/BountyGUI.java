package com.Sagacious_.KitpvpStats.bounty.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.util.ItemUtil;

public class BountyGUI implements Listener{
	private int size;
	private short color;
	private String display;
	private List<String> lore;
	private String title;
	public HashMap<UUID, Integer> s = new HashMap<UUID, Integer>();
	
	public BountyGUI() {
		FileConfiguration conf = Core.getInstance().bm.conf;
		size = conf.getInt("tracker-size");
		color = Short.valueOf((short)conf.getInt("tracker-glass-color"));
		display = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-display"));
		lore = Core.getInstance().bm.translateList(conf.getStringList("tracker-lores"));
		title = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-title"));
	}

	public void openInventory(Player p, int page) {
		s.put(p.getUniqueId(), page);
		Inventory inv = Bukkit.createInventory(null, size, title);
		
		int start = page*18;
		List<Bounty> s = Core.getInstance().bm.getBounties();
		for(int i = 0; i < size-9; i++) {
			if(i+start<s.size()) {
				Bounty b = s.get(i+start);
				List<String> l = new ArrayList<String>();
				for(String f : lore) {
					f=f.replace("%bountied%", Bukkit.getOfflinePlayer(b.getUuidTarget()).getName())
					.replace("%bountier%", Bukkit.getOfflinePlayer(b.getUuidOwner()).getName()).replace("%amount%", ""+b.getAmount()).replace("%timeleft%", ""+b.getTimeLeft());
				   l.add(f);
				}
				inv.setItem(i, ItemUtil.createSkullItem(Bukkit.getOfflinePlayer(b.getUuidTarget()).getName(), display.replace("%bountied%", Bukkit.getOfflinePlayer(b.getUuidTarget()).getName())
						.replace("%bountier%", Bukkit.getOfflinePlayer(b.getUuidOwner()).getName()).replace("%amount%", ""+b.getAmount()).replace("%timeleft%", ""+b.getTimeLeft()), l, b.getUuidTarget().equals(p.getUniqueId())));
			}
		}
		for(int i = size-9; i < size; i++) {
			if((i==size-4&&(page+1)*18<s.size())||(i==size-6&&page>0)) {
				inv.setItem(i, ItemUtil.createItem(Material.ARROW, i==size-6?"§8< §6Previous page":"§6Next page §8>"));
		}
			if(inv.getItem(i)==null) {
				inv.setItem(i, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 1, (short)color, "§7"));
			}
		}
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			if(s.containsKey(p.getUniqueId())) {
				if(e.getCurrentItem()!=null&&!e.getCurrentItem().getType().equals(Material.AIR)) {
					e.setCancelled(true);
					int z = s.get(p.getUniqueId());
					if(e.getCurrentItem().getType().equals(Material.ARROW)) {
						p.closeInventory();
						if(e.getCurrentItem().getItemMeta().getDisplayName().contains(">")) {
							openInventory(p, z+1);
						}else {
							openInventory(p, z-1);
						}
					}
				}
			}
		}
	}
	
	public void onQuit(PlayerQuitEvent e) {
		if(s.containsKey(e.getPlayer().getUniqueId())) {
			s.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(s.containsKey(e.getPlayer().getUniqueId())) {
			s.remove(e.getPlayer().getUniqueId());
		}
	}

}
