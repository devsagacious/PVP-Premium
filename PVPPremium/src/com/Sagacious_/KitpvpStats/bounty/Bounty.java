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
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.sql.SQLConnection;

public class Bounty {
	public String id;
	private boolean console;
	
	private UUID uuidOwner;
	private UUID uuidTarget;
	
	private double amount;
	private int timeLeft;
	public boolean active=true;
	
	private boolean newb = false;
	
	private DataHandler dh = Core.getInstance().getDataHandler();
	
	public Bounty(BountyManager bm, String id, UUID uuidOwner, UUID uuidTarget, double amount, int timeLefts, boolean console) {
		this.id = id;
		Bounty s = this;
		this.console = console;
		if(!console) {this.uuidOwner = uuidOwner;}
		this.uuidTarget = uuidTarget;
		this.amount = amount;
		this.timeLeft = timeLefts;
		newb = timeLefts == bm.time;
		UserData u = dh.stats.get(uuidTarget);
		if(timeLefts>0) {
			new BukkitRunnable() {
				public void run() {
					if(u.online) {
						Player p = Bukkit.getPlayer(uuidTarget);
						if(Core.getInstance().getWorldguardHook().countstats(p)) {
						if(!Bukkit.getPlayer(uuidTarget).isDead()) {
						timeLeft=timeLeft-1;
						if(timeLeft<=-1) {this.cancel();}
						if(timeLeft==0) {
							BountySurvivedEvent e = new BountySurvivedEvent(s);
							Bukkit.getPluginManager().callEvent(e);
							if(!e.isCancelled()) {
								String bountied = dh.stats.get(uuidTarget).getName();
								String bountier = console?"CONSOLE":dh.stats.get(uuidOwner).getName();
								for(String f : bm.bountySurvivedMsg) {
									Bukkit.broadcastMessage(f.replace("%bountied%", bountied).replace("%bountier%", bountier).replace("%amount%", ""+amount));
								}
								dh.stats.get(uuidTarget).setBountiesSurvived(dh.stats.get(uuidTarget).getBountiesSurvived()+1);
								bm.playSound(p, bm.survived);
								bm.ph.survivalPay(Bukkit.getPlayer(uuidTarget), bountier, amount);
								bm.removeBounty(s);
							}
							this.cancel();
						}
						}
						}
					}
				}
			}.runTaskTimer(Core.getInstance(), 20L, 20L);
		}
	}
	
	public boolean isConsole() {
		return console;
	}
	
	public void save(boolean shutdown) {
		SQLConnection sql = Core.getInstance().getSQLConnection();
		if(sql==null) {
			if(shutdown) {
		File f = new File(Core.getInstance().getDataFolder(), "bounties/" + id + ".yml");
		if(!f.exists()) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				if(!console) {pw.println("uuidOwner: '" + uuidOwner.toString() + "'");}
				pw.println("uuidTarget: '" + uuidTarget.toString() + "'");
				pw.println("amount: " + amount);pw.println("timeLeft: " + timeLeft);pw.println("active: " + active);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
			if(!console) {conf.set("uuidOwner",  uuidOwner.toString());}
			conf.set("uuidTarget", uuidTarget.toString());
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
				query = "INSERT INTO Bounty(id, uuidOwner, uuidTarget, amount, timeLeft) VALUES ('" + id + "', '" +(console?"null":uuidOwner.toString()) + "', '" + uuidTarget.toString() + "', '" + amount + "', '" + timeLeft + "');";
			}else {
				query = "UPDATE Bounty SET timeLeft='" + timeLeft + "' WHERE id='" + id + "';";
			}
			final String queried = query;
			if(shutdown) {
				sql.executeStatement(queried);
				return;
			}
			Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
				public void run() {
					sql.executeStatement(queried);
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
