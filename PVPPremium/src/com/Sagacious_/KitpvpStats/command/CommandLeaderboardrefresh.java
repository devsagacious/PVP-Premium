package com.Sagacious_.KitpvpStats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;

public class CommandLeaderboardrefresh implements CommandExecutor{
	
	private LeaderboardHandler lh = Core.getInstance().getLeaderboardHandler();
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg0.hasPermission("pvpstats.leaderboardrefresh")) {
			return false;
		}
		lh.refresh(0, true);
		lh.refresh(1, true);
		lh.refresh(2, true);
		lh.refresh2(0, true);
		lh.refresh2(1, true);
		lh.refresh2(2, true);
		lh.refresh2(3, true);
		lh.refresh2(5, true);
		lh.refresh3(true);
		arg0.sendMessage("§cManually refreshed scoreboards");
		return true;
	}

}
