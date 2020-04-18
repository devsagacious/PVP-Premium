package com.Sagacious_.KitpvpStats.bounty.item;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Sagacious_.KitpvpStats.Core;

public class TargetSelector {
	
	private String item;
	private String display;
	private List<String> lore;
	public ItemStack is;
	
	public TargetSelector() {
		FileConfiguration conf = Core.getInstance().bm.conf;
		item = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-item"));
		display = ChatColor.translateAlternateColorCodes('&', conf.getString("tracker-displayname"));
		lore = Core.getInstance().bm.translateList(conf.getStringList("tracker-lore"));
		
		is = new ItemStack(Material.valueOf(item));
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(display);
		im.setLore(lore);
		
		is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
	}
	
	public ItemStack createItem() {
		return is;
	}

}
