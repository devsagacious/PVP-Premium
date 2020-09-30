package com.Sagacious_.KitpvpStats.bounty.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.BountyManager;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandBounty implements CommandExecutor{
	private int minAmount;
	private int maxAmount;
	private List<String> bountySet;
	private List<String> bountySyntax;
	private List<String> bountySetonself;
	private int bountyTimeout;
	private String bountyCooldown;
	private String targetNotFound;
	private String validNumber;
	private String insufficientFunds;
	private String numberBetween;
	private BountyManager bm = Core.getInstance().getBountyManager();
	
	public CommandBounty() {
		Core.getInstance().getCommand("bounty").setExecutor(this);
		FileConfiguration conf = bm.conf;
		minAmount = conf.getInt("bounty-min-amount");
		maxAmount = conf.getInt("bounty-max-amount");
		bountySet = bm.translateList(conf.getStringList("bounty-set-message"));
		bountySyntax = bm.translateList(conf.getStringList("bounty-syntax-message"));
		bountySetonself = bm.translateList(conf.getStringList("bounty-set-onself"));
		bountyTimeout = conf.getInt("bounty-timeout");
		bountyCooldown = ChatColor.translateAlternateColorCodes('&', conf.getString("bounty-cooldown"));
		targetNotFound = ChatColor.translateAlternateColorCodes('&', conf.getString("target-not-found"));
		validNumber = ChatColor.translateAlternateColorCodes('&', conf.getString("valid-number"));
		insufficientFunds = ChatColor.translateAlternateColorCodes('&', conf.getString("insufficient-funds"));
		numberBetween = ChatColor.translateAlternateColorCodes('&', conf.getString("number-between"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if(!sender.hasPermission("pvpstats.bounty")) {
				sender.sendMessage("§cYou don't have access to this command!");
				return true;
			}
			if(args.length!=1&&args.length!=3) {
				for(String f : bountySyntax) {
					sender.sendMessage(f);
				}
				return true;
			}
			if(sender instanceof Player) {
			if(args[0].equalsIgnoreCase("list")) {
				bm.bg.openInventory(((Player)sender), 0);
				return true;
			}
			}
			if(args[0].equalsIgnoreCase("set")&&args.length==3) {
				if(sender instanceof Player&&!sender.hasPermission("pvpstats.bounty.bypass")) {
					Player p = (Player)sender;
				UserData u = Core.getInstance().getDataHandler().getData(p);
				if(bountyTimeout>0) {
				if((System.currentTimeMillis()-u.lastBounty)/1000L<bountyTimeout) {
					sender.sendMessage(bountyCooldown.replace("%time%", ""+(bountyTimeout-((System.currentTimeMillis()-u.lastBounty)/1000L))));
					return true;
				}
				}
				}
				Player target = Bukkit.getPlayer(args[1]);
				if(target==null||!target.isOnline()) {
					sender.sendMessage(targetNotFound);
					return true;
				}
				if(target==sender) {
					for(String f : bountySetonself) {
						sender.sendMessage(f);
						}
					return true;
				}
				double am = -1;
				try {
				am = Double.valueOf(args[2]);
				}catch(NumberFormatException e) {}
				if(am<0) {
					sender.sendMessage(validNumber);
					return true;
				}
				if(minAmount>0&&am<minAmount||maxAmount>0&&am>maxAmount) {
					sender.sendMessage(numberBetween);
					return true;
				}
				if(sender instanceof Player) {
					Player p = (Player)sender;
				if(!bm.ph.hasAmount(p, am)) {
					sender.sendMessage(insufficientFunds);
					return true;
				}
				}
		        for(String f : bountySet) {
		        	Bukkit.broadcastMessage(f.replace("%bountied%", target.getName()).replace("%bountier%", sender instanceof Player?((Player)sender).getName():"CONSOLE").replace("%amount%", ""+am));
		        }
		        if(sender instanceof Player) {
		        	Player p = (Player)sender;
		        bm.ph.subtract(p, am);
		        UserData u = Core.getInstance().getDataHandler().getData(p);
		        u.lastBounty = System.currentTimeMillis();
		        }
		        UUID uu = sender instanceof Player?((Player)sender).getUniqueId():null;
		        bm.createBounty(uu, target.getUniqueId(), am, null);
		        return true;
			}
			for(String f : bountySyntax) {
				sender.sendMessage(f);
				}
			return true;
	}

}
