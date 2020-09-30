package com.Sagacious_.KitpvpStats.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtil {
	
	private static String nmsver;
	private static boolean legacy;
	
	public ItemUtil() {
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		
        legacy = Integer.parseInt(nmsver.split("_")[1])<13;
	}

	public static ItemStack createItem(Material type) {
		ItemStack item = new ItemStack(type);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount) {
		ItemStack item = new ItemStack(type, amount);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data) {
		ItemStack item = new ItemStack(type, (short)data);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data) {
		ItemStack item = new ItemStack(type, amount, (short)data);
		return item;
	}
	
	public static ItemStack createItem(Material type, String displayname) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, String displayname) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data, String displayname) {
		ItemStack item = new ItemStack(type, data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data, String displayname) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, List<String> lore) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, List<String> lore) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data, List<String> lore) {
		ItemStack item = new ItemStack(type, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, String displayname, List<String> lore) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data, List<String> lore) {
		ItemStack item = new ItemStack(type, amount, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data, String displayname, List<String> lore) {
		ItemStack item = new ItemStack(type, amount, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, ItemFlag... flags) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta im = item.getItemMeta();
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, (short)data);
		ItemMeta im = item.getItemMeta();
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, String displayname, ItemFlag... flags) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, List<String> lore, ItemFlag... flags) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
        im.setLore(lore);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data, String displayname, List<String> lore, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, amount, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.setLore(lore);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data, String displayname, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, amount, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, short data, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, amount, (short)data);
		ItemMeta im = item.getItemMeta();
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, String displayname, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, int amount, List<String> lore, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data, String displayname, List<String> lore, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.setLore(lore);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data, String displayname, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, short data, List<String> lore, ItemFlag... flags) {
		ItemStack item = new ItemStack(type, (short)data);
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createItem(Material type, String displayname, List<String> lore, ItemFlag... flags) {
		ItemStack item = new ItemStack(type);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(displayname);
		im.setLore(lore);
		im.addItemFlags(flags);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createSkullItem(String owner, String display, List<String> lore, boolean enc) {
	ItemStack skull = new ItemStack(legacy?Material.getMaterial("SKULL_ITEM"):Material.getMaterial("LEGACY_SKULL_ITEM"), 1, Short.valueOf((short)3));
	SkullMeta sm = (SkullMeta)skull.getItemMeta();
    sm.setOwner(owner);
    sm.setDisplayName(display);
    if(lore!=null) {
    sm.setLore(lore);
    }
    if(enc) {
    	skull.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
    	sm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
    skull.setItemMeta(sm);
	return skull;
}
}
