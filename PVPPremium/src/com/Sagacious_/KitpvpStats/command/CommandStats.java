package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandStats implements CommandExecutor{

	private List<String> stat;
	public CommandStats() {
		stat = Core.getInstance().getConfig().getStringList("stats-command");
		Core.getInstance().getCommand("stats").setExecutor(this);
		Core.getInstance().getCommand("stats").setAliases(new ArrayList<String>(Arrays.asList("statistics")));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			UserData data = null;
			String pl = Core.getInstance().getConfig().getString("me");
			if(arg3.length > 0) {
				if(arg3[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("pvpstats.reload")) {
						sender.sendMessage("§aReloading plugin...");
						Bukkit.getPluginManager().disablePlugin(Core.getInstance());
						Bukkit.getPluginManager().enablePlugin(Core.getInstance());
						sender.sendMessage("§aReloaded plugin!");
						return true;
					}
				}
				Player p = Bukkit.getPlayerExact(arg3[0]);
				if(p == null) {
					sender.sendMessage("§cUnknown player §4" + arg3[0]);
				return true;
				}
				data = Core.getInstance().dh.getData(p);
				pl = p.getName() + "'s";
			}else {
			 data = Core.getInstance().dh.getData((Player)sender);
			}
			for(String s : stat) {
				String f = ChatColor.translateAlternateColorCodes('&', s.replace("%kills%", ""+data.getKills())
						.replace("%player%", pl)
						.replace("%kdr%", ""+data.getKDR())
						.replace("%chr%", ""+data.getCHR())
						.replace("%hmr%", ""+data.getHMR())
						.replace("%deaths%", ""+data.getDeaths())
						.replace("%hits%", ""+data.getHits())
						.replace("%misses%", ""+data.getMisses())
						.replace("%criticals%", ""+data.getCriticals())
						.replace("%killstreak%", ""+data.getKillstreak())
						.replace("%top_killstreak%", ""+data.getTopKillstreak())
						.replace("%level%", Core.getInstance().sl.getLevel(data.getXP()).getName()))
						.replace("%curr_xp%", data.getXP()+"")
						.replace("%nextlevel_xp%", ""+Core.getInstance().sl.getNextLevelXP(data.getLevel()))
						.replace("%level_progress%", Core.getInstance().sl.getLevelProgress(data))
						.replace("%level_progress_percent%", Core.getInstance().sl.getLevelProgressPercent(data))
						.replace("%level_xp_tonextlevel%", ""+(Core.getInstance().sl.getRealNextLevelXP(data.getLevel(), data.getXP())));
				if(Core.getInstance().ph!=null) {
					f=Core.getInstance().ph.format((Player)sender, f);
				}
				if(Core.getInstance().pa!=null&&me.clip.placeholderapi.PlaceholderAPI.containsPlaceholders(f)) {
					f=me.clip.placeholderapi.PlaceholderAPI.setPlaceholders((Player)sender, f);
				}
				sender.sendMessage(f);}
			return true;
		}
		return false;
	}

}
