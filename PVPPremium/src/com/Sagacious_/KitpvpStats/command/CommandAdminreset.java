package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandAdminreset implements CommandExecutor{
	
    public CommandAdminreset() {
		Core.getInstance().getCommand("adminreset").setExecutor(this);
		Core.getInstance().getCommand("adminreset").setAliases(new ArrayList<String>(Arrays.asList("ar")));
	}
    
    private DataHandler dh = Core.getInstance().getDataHandler();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("pvpstats.adminreset")) {
			return false;
		} 
		if(args.length<1) {
			sender.sendMessage("§c§lPVPStats §8| §rInvalid command syntax use /adminreset [<player>/add <player> <amount>]");
			return true;
		}
		if(args[0].equalsIgnoreCase("add") && args.length==3) {
			UserData data = dh.getData(args[1]);
			if(data == null) {
				sender.sendMessage("§c§lPVPStats §8| §rPlayer was not found!");
				return true;
			}
			int i = -1;
			try {
				i = Integer.parseInt(args[2]);
			}catch(NumberFormatException e) {}
			if(i < 0) {
				sender.sendMessage("§c§lPVPStats §8| §rPlease specify a number");
				return true;
			}
			data.setResets(data.getResets()+i);
			Player p = Bukkit.getPlayerExact(args[1]);
			if(p!=null) {
			sender.sendMessage("§c§lPVPStats §8| §rGave §4" + p.getName() + " §c" + i + " §rresets");
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString("reset-received")).replaceAll("%amount%", ""+i));
			}
			return true;
		}
		UserData data = dh.getData(args[0]);
		if(data == null) {
			sender.sendMessage("§c§lPVPStats §8| §rPlayer was not found!");
			return true;
		}
		data.reset(false);
		sender.sendMessage("§c§lPVPStats §8| §rReset §4" + data.getName() + "§r's stats");
		return true;
	}

}
