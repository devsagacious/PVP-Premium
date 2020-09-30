package com.Sagacious_.KitpvpStats.command.leaderboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;

public class CommandLeaderboard implements CommandExecutor{
	
	private List<String> invalid_syntax = new ArrayList<String>();
	
	public String killHeader;
	public String deathHeader;
	public String killstreakHeader;
	public String kdrHeader;
	public String chrHeader;
	public String hmrHeader;
	public String levelHeader;
	public String bountyHeader;
	public String uniquekillHeader;
	
	private boolean guiSelector = false;
	
	public String format;
	private boolean gui = false;
	
	public LeaderboardHandler lh;
	private LeaderboardGUI lg;
	
	public CommandLeaderboard() {
		FileConfiguration conf = Core.getInstance().getConfig();
		
		if(conf.getBoolean("lb-cmd-enabled")) {
			
			Core.getInstance().getCommand("leaderboard").setExecutor(this);
			Core.getInstance().getCommand("leaderboard").setAliases(new ArrayList<String>(Arrays.asList("lb")));
			
			for(String s : conf.getStringList("lb-invalid-syntax")) {
				invalid_syntax.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			killHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-kills"));
			deathHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-deaths"));
			killstreakHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-killstreak"));
			kdrHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-kdr"));
			chrHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-chr"));
			hmrHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-hmr"));
			levelHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-levels"));
			bountyHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-bounties"));
			uniquekillHeader = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-header-uniquekills"));
			lh = Core.getInstance().getLeaderboardHandler();
			guiSelector = conf.getBoolean("lb-selectmenu-enabled");
			format = ChatColor.translateAlternateColorCodes('&', conf.getString("lb-cmd-format"));
			
			if(conf.getBoolean("lb-as-gui")) {
				gui=true;
				lg = new LeaderboardGUI(this);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length< 1) {
			if(lg!=null&&guiSelector&&sender instanceof Player) {
				Player p = (Player)sender;
				lg.openSelector(p);
				return true;
			}
			for(String s : invalid_syntax) {
				
				sender.sendMessage(s);
				
			}
			return true;
		}
		
		String header = args[0].equalsIgnoreCase("kills")?killHeader:
			args[0].equalsIgnoreCase("deaths")?deathHeader:
				args[0].equalsIgnoreCase("killstreak")?killstreakHeader:
					args[0].equalsIgnoreCase("kdr")?kdrHeader:
						args[0].equalsIgnoreCase("chr")?chrHeader:
							args[0].equalsIgnoreCase("hmr")?hmrHeader:
								args[0].equalsIgnoreCase("levels")?levelHeader:
									args[0].equalsIgnoreCase("bounty")?bountyHeader:
										args[0].equalsIgnoreCase("uniquekills")?uniquekillHeader:"None";
		
		if(header.equals("None")) {
			for(String s : invalid_syntax) {
				
				sender.sendMessage(s);
				
			}
			return true;
		}
		if(gui&&sender instanceof Player) {
			lg.open((Player)sender, args[0]);
			return true;
		}
		
		sender.sendMessage(header);
		if(args[0].equalsIgnoreCase("kills")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedKills.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedKills.get(i).getName()).replace("%amount%", ""+lh.killTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("deaths")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedDeaths.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedDeaths.get(i).getName()).replace("%amount%", ""+lh.deathTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("killstreak")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedKillstreak.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedKillstreak.get(i).getName()).replace("%amount%", ""+lh.killstreakTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("kdr")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedKDR.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedKDR.get(i).getName()).replace("%amount%", ""+lh.kdrTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0.0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("chr")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedCHR.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedCHR.get(i).getName()).replace("%amount%", ""+lh.chrTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0.0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("hmr")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedHMR.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedHMR.get(i).getName()).replace("%amount%", ""+lh.hmrTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0.0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("levels")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedLevel.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedLevel.get(i).getName()).replace("%amount%", ""+lh.levelTop.get(i).getName()));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("bounty")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedBounties.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedBounties.get(i).getName()).replace("%amount%", ""+lh.bountiesTop.get(i)));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0"));
				}
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("uniquekills")) {
			for(int i = 0; i < 10; i++) {
				if(lh.orderedUnqiuekills.size()>i) {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", lh.orderedUnqiuekills.get(i).getName()).replace("%amount%", ""+(int)Math.round(lh.unqiuekillTop.get(i))));
				}else {
					sender.sendMessage(format.replace("%place%", ""+(i+1)).replace("%player%", "None").replace("%amount%", "0"));
				}
			}
			return true;
		}
		
		
		return true;
	}

}
