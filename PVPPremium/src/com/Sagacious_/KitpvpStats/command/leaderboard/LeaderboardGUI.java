package com.Sagacious_.KitpvpStats.command.leaderboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.api.event.LeaderboardUpdateEvent;
import com.Sagacious_.KitpvpStats.util.ItemUtil;

public class LeaderboardGUI implements Listener{
	
	private String format;
	private List<Inventory> inv = new ArrayList<Inventory>();
	
	private String killHeader;
	private String deathHeader;
	private String killstreakHeader;
	private String kdrHeader;
	private String chrHeader;
	private String hmrHeader;
	private String levelHeader;
	private String bountyHeader;
	private String uniquekillHeader;
	private String title;
	private HashMap<String, ItemStack> selectMenu = new HashMap<String, ItemStack>();
	private Inventory selectMenuInv = null;
	
	
	public LeaderboardGUI(CommandLeaderboard clb) {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		format = clb.format;
		killHeader = clb.killHeader;
		deathHeader = clb.deathHeader;
		killstreakHeader = clb.killstreakHeader;
		kdrHeader = clb.kdrHeader;
		chrHeader = clb.chrHeader;
		hmrHeader = clb.hmrHeader;
		levelHeader = clb.levelHeader;
		bountyHeader = clb.bountyHeader;
		uniquekillHeader = clb.uniquekillHeader;
		
		String[] f = new String[] {"kills","killstreak","deaths","kdr","chr","hmr","levels","bounties","uniquekills"};
		FileConfiguration conf = Core.getInstance().getConfig();
		for(String s : f) {
			boolean cont = true;
			String key = conf.getString("lb-selectmenu-" + s);
			if(key.equals("")) {cont=false;}
			if(!key.contains(":")&&cont) {Core.getInstance().getLogger().info("Something is wrong in your syntax for the key 'lb-selectmenu-" + s + "', you can change it in your config.yml");cont=true;}
		
			if(cont) {
			Material m = Material.getMaterial(key.split(":")[1]);
			if(m==null) {Core.getInstance().getLogger().info("Item '" + key.split(":")[1] + "' does not exist, you can change it in your config.yml (lb-selectmenu-"+ s + ")");cont=false;}
			
			if(cont) {
			if(m!=null&&key.contains(":")) {
			selectMenu.put(s, ItemUtil.createItem(m, ChatColor.translateAlternateColorCodes('&', key.split(":")[0])));
			}
			}
		}
		}
		
		updateInventories();
		setupMainInventory();
	}
	
	
	private void setupMainInventory() {
		title=ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("lb-selectmenu-title"));
		Inventory f = Bukkit.createInventory(null, 9, title);
		
		for(Entry<String, ItemStack> i : selectMenu.entrySet()) {
			f.addItem(i.getValue());
		}
		this.selectMenuInv=f;
	}

	
	public void open(Player p, String invs) {
		p.openInventory(invs.equalsIgnoreCase("kills")?inv.get(0):
			invs.equalsIgnoreCase("deaths")?inv.get(1):
				invs.equalsIgnoreCase("killstreak")?inv.get(2):
					invs.equalsIgnoreCase("kdr")?inv.get(3):
						invs.equalsIgnoreCase("chr")?inv.get(4):
							invs.equalsIgnoreCase("hmr")?inv.get(5):
								invs.equalsIgnoreCase("levels")?inv.get(6):
									invs.equalsIgnoreCase("bounty")?inv.get(7):inv.get(8));
	}
	
	private void updateInventories() {
		inv.clear();
		
		
		Inventory kill = Bukkit.createInventory(null, 18, killHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedKills!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedKills.size()>i) {
				kill.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedKills.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedKills.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().killTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(kill);
		
		Inventory death = Bukkit.createInventory(null, 18, deathHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedDeaths!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedDeaths.size()>i) {
				death.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedDeaths.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedDeaths.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().deathTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(death);
		
		Inventory killstreak = Bukkit.createInventory(null, 18, killstreakHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedKillstreak!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedKillstreak.size()>i) {
				killstreak.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedKillstreak.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedKillstreak.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().killstreakTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(killstreak);
			
		Inventory kdr = Bukkit.createInventory(null, 18, kdrHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedKDR!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedKDR.size()>i) {
				kdr.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedKDR.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedKDR.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().kdrTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(kdr);
		
		Inventory chr = Bukkit.createInventory(null, 18, chrHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedCHR!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedCHR.size()>i) {
				chr.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedCHR.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedCHR.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().chrTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(chr);
		
		Inventory hmr = Bukkit.createInventory(null, 18, hmrHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedHMR!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedHMR.size()>i) {
				hmr.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedHMR.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedHMR.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().hmrTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(hmr);
		
		Inventory level = Bukkit.createInventory(null, 18, levelHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedLevel!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedLevel.size()>i) {
				level.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedLevel.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedLevel.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().levelTop.get(i).getName()), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(level);
		
		Inventory bounty = Bukkit.createInventory(null, 18, bountyHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedBounties!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedBounties.size()>i) {
				bounty.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedBounties.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedBounties.get(i).getName()).replace("%amount%", ""+Core.getInstance().getLeaderboardHandler().bountiesTop.get(i)), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(bounty);
		
		Inventory unqieukills = Bukkit.createInventory(null, 18, uniquekillHeader);
		if(Core.getInstance().getLeaderboardHandler().orderedUnqiuekills!=null) {
		for(int i = 0; i < 10; i++) {
			if(Core.getInstance().getLeaderboardHandler().orderedUnqiuekills.size()>i) {
				unqieukills.setItem(i, ItemUtil.createSkullItem(Core.getInstance().getLeaderboardHandler().orderedUnqiuekills.get(i).getName(), format.replace("%place%", ""+(i+1)).replace("%player%", Core.getInstance().getLeaderboardHandler().orderedDeaths.get(i).getName()).replace("%amount%", ""+(int)Math.round(Core.getInstance().getLeaderboardHandler().unqiuekillTop.get(i))), new ArrayList<String>(), false));
			}
		}
		}
		inv.add(unqieukills);
	}
	
	private Long last = -1L;
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(inv.contains(e.getInventory())) {
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
		}else if(e.getView().getTitle().equals(title)) {
			
			if(e.getCurrentItem()!=null&&!e.getCurrentItem().getType().equals(Material.AIR)) {
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
			
			List<String> list = new ArrayList<String>(selectMenu.keySet());
			String s = list.get(e.getSlot());
			
			open((Player)e.getWhoClicked(), s);
			
			}
		}
	}
	
	@EventHandler
	public void onLb(LeaderboardUpdateEvent e) {
		if(!e.isManual()) {
			long now = new Date().getTime()/1000L;
			if(last<0||(now-last<3)) {
				last=now;
		updateInventories();
			}
	}
	}


	public void openSelector(Player p) {
		p.openInventory(this.selectMenuInv);
	}
}
