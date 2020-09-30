package com.Sagacious_.KitpvpStats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandRemovestats implements CommandExecutor{
	
	public CommandRemovestats() {
		Core.getInstance().getCommand("removestats").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!sender.hasPermission("pvpstats.removestats")) {
			return false;
		}
			if(arg3.length < 3) {
				sender.sendMessage("§a/removestats kills <player> <amount>");
				sender.sendMessage("§a/removestats deaths <player> <amount>");
				sender.sendMessage("§a/removestats killstreak <player> <amount>");
				sender.sendMessage("§a/removestats topkillstreak <player> <amount>");
				sender.sendMessage("§a/removestats xp <player> <amount>");
				return true;
			}else {
				UserData d = Core.getInstance().getDataHandler().getData(arg3[1]);
				if(d==null) {
					sender.sendMessage("§cUnknown player §4" + arg3[1]);
					return true;
				}
				int amount = -1;
				try {
					amount = Integer.valueOf(arg3[2]);
				}catch(NumberFormatException e) {sender.sendMessage("§cPlease specify a correct number!"); return true;}
				if(arg3[0].equalsIgnoreCase("xp")) {d.setXP(d.getXP()-amount);}
				if(arg3[0].equalsIgnoreCase("kills")) {d.setKills(d.getKills()-amount);}
				if(arg3[0].equalsIgnoreCase("deaths")) {d.setDeaths(d.getDeaths()-amount);}
				if(arg3[0].equalsIgnoreCase("killstreak")) {d.setKillstreak(d.getKillstreak()-amount);}
				if(arg3[0].equalsIgnoreCase("topkillstreak")) {d.setTopKillstreak(d.getTopKillstreak()-amount);}
				sender.sendMessage("§aUpdated §2" + arg3[1] + "§a's statistics!");
			}
		return true;
	}

}
