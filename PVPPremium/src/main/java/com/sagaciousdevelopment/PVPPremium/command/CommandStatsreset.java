package com.sagaciousdevelopment.PVPPremium.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sagaciousdevelopment.PVPPremium.Core;
import com.sagaciousdevelopment.PVPPremium.stats.Stats;

public class CommandStatsreset implements CommandExecutor{
	
    public CommandStatsreset() {
		Core.getInstance().getCommand("statsreset").setExecutor(this);
		Core.getInstance().getCommand("statsreset").setAliases(new ArrayList<String>(Arrays.asList("sr")));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
		if(args.length<0) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("syntax-error")).replaceAll("%command_usage%", "/" + label.toLowerCase()));
			return true;
		}
		Stats data = Core.getInstance().sm.getData(p);
		if(data.getResets()<1) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("reset-none")));
			return true;
		}
		data.reset(true);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("reset")));
		return true;
		}
		return false;
	}

}
