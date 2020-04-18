package com.Sagacious_.KitpvpStats.bounty.event;

import java.util.UUID;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.Sagacious_.KitpvpStats.bounty.Bounty;

public class BountyKilledEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	
	private Bounty bounty;
	private UUID bountiedBy;
	private UUID bountied;
	private UUID killer;
	private double amount;
	private int timeLeft;
	
	private boolean cancelled;
	
	public BountyKilledEvent(Bounty b, UUID killer) {
		this.bounty = b;
		bountiedBy = b.getUuidOwner();
		bountied = b.getUuidTarget();
		this.killer = killer;
		amount = b.getAmount();
		timeLeft = b.getTimeLeft();
		
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

	public UUID getKiller() {
		return killer;
	}

	public double getAmount() {
		return amount;
	}

	public int getTimeLeft() {
		return timeLeft;
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
