package com.Sagacious_.KitpvpStats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Sagacious_.KitpvpStats.Core;

public class CommandLeaderboardrefresh implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg0.hasPermission("pvpstats.leaderboardrefresh")) {
			return false;
		}
		Core.getInstance().lh.refresh(0);
		Core.getInstance().lh.refresh(1);
		Core.getInstance().lh.refresh(2);
		Core.getInstance().lh.refresh2(0);
		Core.getInstance().lh.refresh2(1);
		Core.getInstance().lh.refresh2(2);
		arg0.sendMessage("§cManually refreshed scoreboards");
		return true;
	}

}
