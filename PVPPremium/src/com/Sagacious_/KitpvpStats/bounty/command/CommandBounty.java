package com.Sagacious_.KitpvpStats.bounty.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

public class CommandBounty implements CommandExecutor{
	private int minAmount;
	private int maxAmount;
	private List<String> bountySet;
	private List<String> bountySyntax;
	private int bountyTimeout;
	private String bountyCooldown;
	private String targetNotFound;
	private String validNumber;
	private String insufficientFunds;
	private String numberBetween;
	
	public CommandBounty() {
		Core.getInstance().getCommand("bounty").setExecutor(this);
		FileConfiguration conf = Core.getInstance().bm.conf;
		minAmount = conf.getInt("bounty-min-amount");
		maxAmount = conf.getInt("bounty-max-amount");
		bountySet = Core.getInstance().bm.translateList(conf.getStringList("bounty-set-message"));
		bountySyntax = Core.getInstance().bm.translateList(conf.getStringList("bounty-syntax-message"));
		bountyTimeout = conf.getInt("bounty-timeout");
		bountyCooldown = ChatColor.translateAlternateColorCodes('&', conf.getString("bounty-cooldown"));
		targetNotFound = ChatColor.translateAlternateColorCodes('&', conf.getString("target-not-found"));
		validNumber = ChatColor.translateAlternateColorCodes('&', conf.getString("valid-number"));
		insufficientFunds = ChatColor.translateAlternateColorCodes('&', conf.getString("insufficient-funds"));
		numberBetween = ChatColor.translateAlternateColorCodes('&', conf.getString("number-between"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(!p.hasPermission("pvpstats.bounty")) {
				p.sendMessage("§cYou don't have access to this command!");
				return true;
			}
			if(args.length!=1&&args.length!=3) {
				for(String f : bountySyntax) {
				p.sendMessage(f);
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("list")) {
				Core.getInstance().bm.bg.openInventory(p, 0);
				return true;
			}
			if(args[0].equalsIgnoreCase("set")&&args.length==3) {
				UserData u = Core.getInstance().dh.getData(p);
				if(bountyTimeout>0) {
				if((System.currentTimeMillis()-u.lastBounty)/1000L<bountyTimeout) {
					p.sendMessage(bountyCooldown.replace("%time%", ""+(bountyTimeout-((System.currentTimeMillis()-u.lastBounty)/1000L))));
					return true;
				}
				}
				Player target = Bukkit.getPlayer(args[1]);
				if(target==null||!target.isOnline()) {
					p.sendMessage(targetNotFound);
					return true;
				}
				double am = -1;
				try {
				am = Double.valueOf(args[2]);
				}catch(NumberFormatException e) {}
				if(am<0) {
					p.sendMessage(validNumber);
					return true;
				}
				if(minAmount>0&&am<minAmount||maxAmount>0&&am>maxAmount) {
					p.sendMessage(numberBetween);
					return true;
				}
				if(!Core.getInstance().bm.ph.hasAmount(p, am)) {
					p.sendMessage(insufficientFunds);
					return true;
				}
		        for(String f : bountySet) {
		        	Bukkit.broadcastMessage(f.replace("%bountied%", target.getName()).replace("%bountier%", p.getName()).replace("%amount%", ""+am));
		        }
		        Core.getInstance().bm.ph.subtract(p, am);
		        Core.getInstance().bm.createBounty(p.getUniqueId(), target.getUniqueId(), am, null);
		        u.lastBounty = System.currentTimeMillis();
		        return true;
			}
			for(String f : bountySyntax) {
				p.sendMessage(f);
				}
			return true;
		}
		return false;
	}

}
