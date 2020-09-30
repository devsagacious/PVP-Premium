package com.Sagacious_.KitpvpStats.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LeaderboardUpdateEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private int leaderboard;
	private boolean manual;
	
	public LeaderboardUpdateEvent(int leaderboard, boolean manual) {
		this.leaderboard = leaderboard;
		this.manual = manual;
	}
	
	public int getLeaderboard() {
		return leaderboard;
	}
	
	public boolean isManual() {
		return manual;
	}
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	

}
