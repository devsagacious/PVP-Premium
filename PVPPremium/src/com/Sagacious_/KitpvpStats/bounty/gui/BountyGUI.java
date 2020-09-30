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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.bounty.BountyManager;
import com.Sagacious_.KitpvpStats.util.ItemUtil;

public class BountyGUI implements Listener{
	private int size;
	private short color;
	private String display;
	private List<String> lore;
	private String title;
	public HashMap<UUID, Integer> s = new HashMap<UUID, Integer>();
	private BountyManager bm = Core.getInstance().getBountyManager();
	private HashMap<Short, String> translateColor = new HashMap<Short, String>();
	private String nmsver;
	private boolean updated;
	
	public BountyGUI() {
		FileConfiguration conf = bm.conf;
		size = conf.getInt("tracker-size");
		color = Short.valueOf((short)conf.getInt("tracker-glass-color"));
		display = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-display"));
		lore = bm.translateList(conf.getStringList("tracker-lores"));
		title = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-title"));
		
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		
        updated = Integer.parseInt(nmsver.split("_")[1])>=13;
        
		translateColor.put((short)0, "WHITE_STAINED_GLASS_PANE");
		translateColor.put((short)1, "ORANGE_STAINED_GLASS_PANE");
		translateColor.put((short)2, "MAGENTA_STAINED_GLASS_PANE");
		translateColor.put((short)3, "LIGHT_BLUE_STAINED_GLASS_PANE");
		translateColor.put((short)4, "YELLOW_STAINED_GLASS_PANE");
		translateColor.put((short)5, "LIME_STAINED_GLASS_PANE");
		translateColor.put((short)6, "PINK_STAINED_GLASS_PANE");
		translateColor.put((short)7, "GRAY_STAINED_GLASS_PANE");
		translateColor.put((short)8, "LIGHT_GRAY_STAINED_GLASS_PANE");
		translateColor.put((short)9, "CYAN_STAINED_GLASS_PANE");
		translateColor.put((short)10, "PURPLE_STAINED_GLASS_PANE");
		translateColor.put((short)11, "BLUE_STAINED_GLASS_PANE");
		translateColor.put((short)12, "BROWN_STAINED_GLASS_PANE");
		translateColor.put((short)13, "GREEN_STAINED_GLASS_PANE");
		translateColor.put((short)14, "RED_STAINED_GLASS_PANE");
		translateColor.put((short)15, "BLACK_STAINED_GLASS_PANE");
	}

	public void openInventory(Player p, int page) {
		s.put(p.getUniqueId(), page);
		Inventory inv = Bukkit.createInventory(null, size, title);
		
		int start = page*18;
		List<Bounty> s = bm.getBounties();
		for(int i = 0; i < size-9; i++) {
			if(i+start<s.size()) {
				Bounty b = s.get(i+start);
				List<String> l = new ArrayList<String>();
				for(String f : lore) {
					f=f.replace("%bountied%", Bukkit.getOfflinePlayer(b.getUuidTarget()).getName()).replace("%online%", ""+Bukkit.getOfflinePlayer(b.getUuidTarget()).isOnline())
					.replace("%bountier%", b.getUuidOwner()!=null?Bukkit.getOfflinePlayer(b.getUuidOwner()).getName():"CONSOLE").replace("%amount%", ""+b.getAmount()).replace("%timeleft%", ""+b.getTimeLeft());
				   l.add(f);
				}
				inv.setItem(i, ItemUtil.createSkullItem(Bukkit.getOfflinePlayer(b.getUuidTarget()).getName(), display.replace("%bountied%", Bukkit.getOfflinePlayer(b.getUuidTarget()).getName())
						.replace("%bountier%", b.getUuidOwner()!=null?Bukkit.getOfflinePlayer(b.getUuidOwner()).getName():"CONSOLE").replace("%amount%", ""+b.getAmount()).replace("%timeleft%", ""+b.getTimeLeft()).replace("%online%", ""+Bukkit.getOfflinePlayer(b.getUuidTarget()).isOnline()), l, b.getUuidTarget().equals(p.getUniqueId())));
			}
		}
		for(int i = size-9; i < size; i++) {
			if((i==size-4&&(page+1)*18<s.size())||(i==size-6&&page>0)) {
				inv.setItem(i, ItemUtil.createItem(Material.ARROW, i==size-6?"§8< §6Previous page":"§6Next page §8>"));
		}
			if(inv.getItem(i)==null) {
				if(updated) {
				inv.setItem(i, ItemUtil.createItem(fromString(translateColor.get(Short.valueOf((short)color))), "§7"));	
				}else {
					ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, Short.valueOf((short)color));
					ItemMeta im = is.getItemMeta();
					im.setDisplayName("§7");
					is.setItemMeta(im);
					
				inv.setItem(i, is);
			}
			}
		}
		p.openInventory(inv);
	}
	
	private Material fromString(String n) {
		for(Material m : Material.values()) {
			if(m.toString().equalsIgnoreCase(n)) {
				return m;
			}
		}
		return null;
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
