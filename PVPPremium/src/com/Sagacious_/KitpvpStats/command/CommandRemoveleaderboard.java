package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class CommandRemoveleaderboard implements CommandExecutor{
	
	public CommandRemoveleaderboard() {
		Core.getInstance().getCommand("removeleaderboard").setExecutor(this);
		Core.getInstance().getCommand("removeleaderboard").setAliases(new ArrayList<String>(Arrays.asList("removelb")));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(!p.hasPermission("pvpstats.removeleaderboard")) {
				return false;
			}
			if(args.length == 0) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /removeleaderboard <kills/deaths/killstreak/kdr/chr/hmr/level/bounty/uniquekills>");
				return true;
			}
			com.gmail.filoghost.holographicdisplays.api.Hologram s = null;
			boolean f = false;
			if(args[0].equalsIgnoreCase("kills")) {
				s = Core.getInstance().getHolographicHook().killHologram;
				Core.getInstance().getHolographicHook().lke_l=new Location(Core.getInstance().getHolographicHook().lke_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("deaths")) {
				s = Core.getInstance().getHolographicHook().deathsHologram;
				Core.getInstance().getHolographicHook().lde_l=new Location(Core.getInstance().getHolographicHook().lde_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				s = Core.getInstance().getHolographicHook().killstreakHologram;
				Core.getInstance().getHolographicHook().lkie_l=new Location(Core.getInstance().getHolographicHook().lkie_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("kdr")) {
				s = Core.getInstance().getHolographicHook().kdrHologram;	
				Core.getInstance().getHolographicHook().kdr_l=new Location(Core.getInstance().getHolographicHook().kdr_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("chr")) {
				s = Core.getInstance().getHolographicHook().chrHologram;
				Core.getInstance().getHolographicHook().chr_l=new Location(Core.getInstance().getHolographicHook().chr_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("hmr")) {
				s = Core.getInstance().getHolographicHook().hmrHologram;
				Core.getInstance().getHolographicHook().hmr_l=new Location(Core.getInstance().getHolographicHook().hmr_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("level")) {
				s = Core.getInstance().getHolographicHook().levelHologram;
				Core.getInstance().getHolographicHook().level_l=new Location(Core.getInstance().getHolographicHook().level_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("bounty")) {
				s = Core.getInstance().getHolographicHook().bountyHologram;	
				Core.getInstance().getHolographicHook().bounty_l=new Location(Core.getInstance().getHolographicHook().bounty_l.getWorld(), 0, 5, 0);
			}else if(args[0].equalsIgnoreCase("uniquekills")) {
				s = Core.getInstance().getHolographicHook().uniquekillHologram;	
				Core.getInstance().getHolographicHook().unique_l=new Location(Core.getInstance().getHolographicHook().unique_l.getWorld(), 0, 5, 0);
			}
			if(s==null&&!f) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /removeleaderboard <kills/deaths/killstreak/kdr/chr/hmr/level/bounty/uniquekills>");
				return true;
			}
			if(s.isDeleted()) {
				p.sendMessage("§c§lPVPStats §8| §rThat leaderboard is already deleted!");
				return true;
			}
			s.delete();
			p.sendMessage("§c§lPVPStats §8| §rRemoved leaderboard!");
			return true;
			}
		
		return false;
	}
}
