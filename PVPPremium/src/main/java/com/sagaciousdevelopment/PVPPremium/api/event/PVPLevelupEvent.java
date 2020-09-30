package com.sagaciousdevelopment.PVPPremium.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sagaciousdevelopment.PVPPremium.stats.rank.SLevel;

public class PVPLevelupEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private Player p;
	private SLevel oldlevel;
	private SLevel newlevel;
	
	public PVPLevelupEvent(Player p, SLevel oldlevel, SLevel newlevel) {
		this.p = p;
		this.oldlevel = oldlevel;
		this.newlevel = newlevel;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public SLevel getOldLevel() {
		return oldlevel;
	}
	
	public SLevel getNewLevel() {
		return newlevel;
	}
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	

}
