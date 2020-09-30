package com.Sagacious_.KitpvpStats.handler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;

public class ActivityHandler implements Listener{

	private DataHandler dh = Core.getInstance().getDataHandler();
	
	public ActivityHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(dh.getData(e.getPlayer())==null) {
			dh.stats.put(e.getPlayer().getUniqueId(), new UserData(e.getPlayer().getUniqueId(), e.getPlayer().getName(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null));
		}else {
			dh.getData(e.getPlayer()).setName(e.getPlayer().getName());
		}
		dh.getData(e.getPlayer()).online = true;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		dh.getData(e.getPlayer()).online = false;
		dh.getData(e.getPlayer()).save(false);
	}
	
}
