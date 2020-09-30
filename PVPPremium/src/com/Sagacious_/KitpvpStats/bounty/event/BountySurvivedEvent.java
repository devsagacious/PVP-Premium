package com.Sagacious_.KitpvpStats.bounty.event;

import java.util.UUID;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.Sagacious_.KitpvpStats.bounty.Bounty;

public class BountySurvivedEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	
	private Bounty bounty;
	private UUID bountiedBy;
	private UUID bountied;
	private double amount;
	
	private boolean cancelled;
	
	public BountySurvivedEvent(Bounty b) {
		this.bounty = b;
		bountiedBy = b.getUuidOwner();
		bountied = b.getUuidTarget();
		amount = b.getAmount();
		
	}

	public Bounty getBounty() {
		return bounty;
	}

	public UUID getBountiedBy() {
		return bountiedBy;
	}

	public UUID getBountied() {
		return bountied;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
	    this.cancelled = cancelled;
		
	}

}
