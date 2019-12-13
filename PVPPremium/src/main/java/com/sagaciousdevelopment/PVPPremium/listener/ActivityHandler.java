package com.sagaciousdevelopment.PVPPremium.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sagaciousdevelopment.PVPPremium.Core;
import com.sagaciousdevelopment.PVPPremium.stats.Stats;

public class ActivityHandler implements Listener{

	
	public ActivityHandler() {
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Core.getInstance().sm.getData(e.getPlayer())==null) {
			Core.getInstance().sm.stats.add(new Stats(e.getPlayer().getUniqueId(), e.getPlayer().getName(), 0, 0, 0, 0, 0, 0, 0, 0));
		}else {
			Core.getInstance().sm.getData(e.getPlayer()).setName(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) {
			if(Core.getInstance().lh.isLeaderboard((ArmorStand)e.getRightClicked())){
				e.setCancelled(true);
			}
		}
	}
}
