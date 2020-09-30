package com.Sagacious_.KitpvpStats.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.util.ItemUtil;

public class PlayerheadHandler implements Listener{
	
	private boolean enabled;
	private int chance;
	private String display;
	private int interval;
	private int health;
	private String intMsg;
	private int regeneration;
	
	private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
	
	private ItemStack createItem(Player p) {
		return ItemUtil.createSkullItem(p.getName(), ChatColor.translateAlternateColorCodes('&', display.replace("%player%", p.getName())), null, false);
	}
	
	public PlayerheadHandler() {
		FileConfiguration conf = Core.getInstance().getConfig();
		enabled = conf.getBoolean("playerheads");
		chance = conf.getInt("playerheads-chance");
		display = conf.getString("playerheads-display");
		intMsg = ChatColor.translateAlternateColorCodes('&', conf.getString("playerheads-interval-message"));
		interval = conf.getInt("playerheads-interval");
		health = conf.getInt("playerheads-health");
		regeneration = conf.getInt("playerheads-regeneration");
		
		if(enabled) {
			Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)||e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.hasItem()&&e.getItem().getType().toString().contains("SKULL_ITEM")&&e.getItem().hasItemMeta()) {
			if(cooldown.containsKey(e.getPlayer().getUniqueId())) {
				long now = new Date().getTime()/1000L;
			if(now-(cooldown.get(e.getPlayer().getUniqueId())/1000L)<interval) {
				 long time = interval - (now - (cooldown.get(e.getPlayer().getUniqueId())/1000L));
				e.getPlayer().sendMessage(intMsg.replace("%remaining%", String.valueOf(time)));
				return;
			}else {
				cooldown.remove(e.getPlayer().getUniqueId());
			}
			}
			if(e.getPlayer().getItemInHand().getAmount()==1) {
			e.getPlayer().getInventory().remove(e.getPlayer().getItemInHand());
			}else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			if(e.getPlayer().getHealth()+health>e.getPlayer().getMaxHealth()) {e.getPlayer().setHealth(e.getPlayer().getMaxHealth());}else {
			e.getPlayer().setHealth(e.getPlayer().getHealth()+health);
			}
			if(regeneration>0) {
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, regeneration-1));
			}
			cooldown.put(e.getPlayer().getUniqueId(), new Date().getTime());
		}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(new Random().nextInt(100)<chance) {
			e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), createItem(e.getEntity()));
		}
	}
}
