package com.Sagacious_.KitpvpStats.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.util.ItemUtil;



public class GUITopstats implements Listener{
	public Inventory mainInv;
	public Inventory killsInv;
	public Inventory topkillsInv;
	public Inventory deathsInv;
	public Inventory kdrInv;
	public Inventory chrInv;
	public Inventory hmrInv;
	
	private FileConfiguration topGUI;
	
	private String head_title;
	private String head_lore;
	
	public GUITopstats() {
		topGUI = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "topgui.yml"));
		updateInventories(new int[] {0,1,2,3,4,5});
	}

	private void updateInventories(int[] toUpdate) {
		if(mainInv == null) {
			mainInv = Bukkit.createInventory(null, topGUI.getInt("inv-size"), ChatColor.translateAlternateColorCodes('&', topGUI.getString("inv-title")));
			for(String k : topGUI.getConfigurationSection("contents").getKeys(false)) {
				ItemStack i = new ItemStack(Material.valueOf(topGUI.getString("contents." + k + ".item")));
				ItemMeta im = i.getItemMeta();
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', topGUI.getString("contents." + k + ".title")));
				List<String> lore = topGUI.getStringList("contents." + k + ".lore");
				List<String> t = new ArrayList<String>();
				for(String s : lore) {
					t.add(ChatColor.translateAlternateColorCodes('&', s));
				}
				im.setLore(lore);
				
				i.setItemMeta(im);
			}
			List<Inventory> s = new ArrayList<Inventory>();
			killsInv = Bukkit.createInventory(null, 45, topGUI.getString("inv-title-top").replace("%type%", "§6Top Kills"));
			topkillsInv = Bukkit.createInventory(null, 45, topGUI.getString("inv-title-top").replace("%type%", "§6Top Killstreaks"));
			deathsInv = Bukkit.createInventory(null, 45, topGUI.getString("inv-title-top").replace("%type%", "§6Top Deaths"));
			chrInv = Bukkit.createInventory(null, 45, topGUI.getString("inv-title-top").replace("%type%", "§6Top CHR"));
			kdrInv = Bukkit.createInventory(null, 45, topGUI.getString("inv-title-top").replace("%type%", "§6Top KDR"));
			hmrInv = Bukkit.createInventory(null, 45, topGUI.getString("inv-title-top").replace("%type%", "§6Top HMR"));
			s.add(killsInv);s.add(topkillsInv);s.add(deathsInv);s.add(hmrInv);s.add(kdrInv);s.add(hmrInv);
			ItemStack item = ItemUtil.createItem(Material.STAINED_GLASS_PANE, 1, Short.valueOf(topGUI.getString("glass-color")), "§r	");
			for(Inventory inv : s) {
				for(int i = 0; i < 45; i++) {
					if(i>9&&i<18||i>19&&i<27||i>28&&i<36) {return;}
					inv.setItem(i, item);
				}
			}
		}
		for(int i : toUpdate) {
			if(i==0) {
				//kills
			}else if(i==1) {
				//deaths
			}else if(i==2) {
				//killstreaks
			}else if(i==3) {
				//kdr
			}else if(i==4) {
				//chr
			}else if(i==5) {
				//hmr
			}
		}
	}
	
	
	@EventHandler
	public void onUpdate(LeaderboardUpdateEvent e) {
		if(e.hasChanged()) {
			updateInventories(new int[] {e.getLeaderboard()});
		}
	}
}
