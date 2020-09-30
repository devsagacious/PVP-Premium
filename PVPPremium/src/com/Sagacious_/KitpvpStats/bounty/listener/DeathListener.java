package com.Sagacious_.KitpvpStats.bounty.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.Bounty;
import com.Sagacious_.KitpvpStats.bounty.BountyManager;
import com.Sagacious_.KitpvpStats.bounty.event.BountyKilledEvent;
import com.Sagacious_.KitpvpStats.data.UserData;

public class DeathListener implements Listener{
	private List<String> bountyKilledMsg = new ArrayList<String>();
	private BountyManager bm = Core.getInstance().getBountyManager();
	
	public DeathListener() {
		FileConfiguration conf = bm.conf;
		bountyKilledMsg = bm.translateList(conf.getStringList("bounty-killed-message"));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			Player killed = e.getEntity();
			Player killer = killed.getKiller();
			
			if(bm.getBounties(killed.getUniqueId())!=null) {
				List<Bounty> bounties = bm.getBounties(killed.getUniqueId());
				for(int i = 0; i < bounties.size(); i++) {
					BountyKilledEvent event = new BountyKilledEvent(bounties.get(i), killer.getUniqueId());
					Bukkit.getPluginManager().callEvent(event);
					Bounty b = event.getBounty();
					if(!event.isCancelled()) {
					for(String s : bountyKilledMsg) {
						Bukkit.broadcastMessage(s.replace("%bountied%", killed.getName()).replace("%killer%", killer.getName()).replace("%amount%", ""+b.getAmount()));
					}
					b.setTimeLeft(-1);
					UserData d = Core.getInstance().getDataHandler().getData(killer);
					d.setBountiesKilled(d.getBountiesKilled()+1);
					bounties.remove(b);
					bm.ph.pay(killer, b.getAmount());
					bm.removeBounty(b);
					bm.tl.dead(killed, killer);
					bm.playSound(killer, bm.kill);
					bm.playSound(killed, bm.killed);
					}
				}
				bm.bounties.remove(killed.getUniqueId());
			}
		}
	}

}
