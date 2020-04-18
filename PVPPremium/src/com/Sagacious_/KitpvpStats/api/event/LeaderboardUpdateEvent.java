package com.Sagacious_.KitpvpStats.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LeaderboardUpdateEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private int leaderboard;
	private boolean changed;
	
	public LeaderboardUpdateEvent(int leaderboard, boolean changed) {
		this.leaderboard = leaderboard;
		this.changed = changed;
	}
	
	public int getLeaderboard() {
		return leaderboard;
	}
	
	public boolean hasChanged() {
		return changed;
	}
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	

}
