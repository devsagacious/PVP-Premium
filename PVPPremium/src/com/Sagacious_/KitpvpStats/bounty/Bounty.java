package com.Sagacious_.KitpvpStats.bounty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.event.BountySurvivedEvent;
import com.Sagacious_.KitpvpStats.data.UserData;

public class Bounty {
	public String id;
	
	private UUID uuidOwner;
	private UUID uuidTarget;
	
	private double amount;
	private int timeLeft;
	public boolean active=true;
	
	private boolean newb = false;
	
	public Bounty(String id, UUID uuidOwner, UUID uuidTarget, double amount, int timeLefts) {
		this.id = id;
		Bounty s = this;
		this.uuidOwner = uuidOwner;
		this.uuidTarget = uuidTarget;
		this.amount = amount;
		this.timeLeft = timeLefts;
		newb = timeLefts == Core.getInstance().bm.time;
		UserData u = Core.getInstance().dh.stats.get(uuidTarget);
		if(timeLefts>0) {
			new BukkitRunnable() {
				public void run() {
					if(u.online) {
						Player p = Bukkit.getPlayer(uuidTarget);
						if(!Bukkit.getPlayer(uuidTarget).isDead()) {
						timeLeft=timeLeft-1;
						if(timeLeft<=-1) {this.cancel();}
						if(timeLeft==0) {
							BountySurvivedEvent e = new BountySurvivedEvent(s);
							Bukkit.getPluginManager().callEvent(e);
							if(!e.isCancelled()) {
								String bountied = Core.getInstance().dh.stats.get(uuidTarget).getName();
								String bountier = Core.getInstance().dh.stats.get(uuidOwner).getName();
								for(String f : Core.getInstance().bm.bountySurvivedMsg) {
									Bukkit.broadcastMessage(f.replace("%bountied%", bountied).replace("%bountier%", bountier).replace("%amount%", ""+amount));
								}
								Core.getInstance().dh.stats.get(uuidTarget).setBountiesSurvived(Core.getInstance().dh.stats.get(uuidTarget).getBountiesSurvived()+1);
								Core.getInstance().bm.playSound(p, Core.getInstance().bm.survived);
								Core.getInstance().bm.ph.survivalPay(Bukkit.getPlayer(uuidTarget), bountier, amount);
								Core.getInstance().bm.removeBounty(s);
							}
							this.cancel();
						}
						}
					}
				}
			}.runTaskTimer(Core.getInstance(), 20L, 20L);
		}
	}
	
	public void save(boolean shutdown) {
		if(Core.getInstance().sql==null) {
			if(shutdown) {
		File f = new File(Core.getInstance().getDataFolder(), "bounties/" + id + ".yml");
		if(!f.exists()) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.println("uuidOwner: '" + uuidOwner.toString() + "'");pw.println("uuidTarget: '" + uuidTarget.toString() + "'");
				pw.println("amount: " + amount);pw.println("timeLeft: " + timeLeft);pw.println("active: " + active);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			conf.set("uuidOwner", uuidOwner.toString());conf.set("uuidTarget", uuidTarget.toString());
			conf.set("amount", amount);conf.set("timeLeft", timeLeft);conf.set("active", active);
			try {
				conf.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			}
		}else {
			String query = "";
			if(newb) {
				query = "INSERT INTO Bounty(id, uuidOwner, uuidTarget, amount, timeLeft) VALUES ('" + id + "', '" + uuidOwner.toString() + "', '" + uuidTarget.toString() + "', '" + amount + "', '" + timeLeft + "');";
			}else {
				query = "UPDATE Bounty SET timeLeft='" + timeLeft + "' WHERE id='" + id + "';";
			}
			final String queried = query;
			if(shutdown) {
				Core.getInstance().sql.executeStatement(queried);
				return;
			}
			Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
				public void run() {
					Core.getInstance().sql.executeStatement(queried);
				}
			});
		}
	}

	public UUID getUuidOwner() {
		return uuidOwner;
	}

	public void setUuidOwner(UUID uuidOwner) {
		this.uuidOwner = uuidOwner;
	}

	public UUID getUuidTarget() {
		return uuidTarget;
	}

	public void setUuidTarget(UUID uuidTarget) {
		this.uuidTarget = uuidTarget;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

}
