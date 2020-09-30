package com.Sagacious_.KitpvpStats.command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;

public class CommandMoveleaderboard implements CommandExecutor{
	
	public CommandMoveleaderboard() {
		Core.getInstance().getCommand("moveleaderboard").setExecutor(this);
		Core.getInstance().getCommand("moveleaderboard").setAliases(new ArrayList<String>(Arrays.asList("movelb")));
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof Player) {
			Player p = (Player)cs;
			if(!p.hasPermission("pvpstats.moveleaderboard")) {
				return false;
			}
			if(args.length == 0) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak/kdr/chr/hmr/level/bounty/uniquekills>");
				return true;
			}
			com.gmail.filoghost.holographicdisplays.api.Hologram s = null;
			boolean f = false;
			if(args[0].equalsIgnoreCase("kills")) {
				s = Core.getInstance().getHolographicHook().killHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("lke", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("deaths")) {
				s = Core.getInstance().getHolographicHook().deathsHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("lde", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				s = Core.getInstance().getHolographicHook().killstreakHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("lkie", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("kdr")) {
				s = Core.getInstance().getHolographicHook().kdrHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("kdr", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("chr")) {
				s = Core.getInstance().getHolographicHook().chrHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("chr", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("hmr")) {
				s = Core.getInstance().getHolographicHook().hmrHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("hmr", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("level")) {
				s = Core.getInstance().getHolographicHook().levelHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("level", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("bounty")) {
				s = Core.getInstance().getHolographicHook().bountyHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("bounties", p.getLocation());
					f=true;
				}
			}else if(args[0].equalsIgnoreCase("uniquekills")) {
				s = Core.getInstance().getHolographicHook().uniquekillHologram;
				if(s==null) {
					Core.getInstance().getHolographicHook().setupNew("uniquekills", p.getLocation());
					f=true;
				}
			}
			if(s==null&&!f) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak/kdr/chr/hmr/level/bounty/uniquekills>");
				return true;
			}
			Core.getInstance().getHolographicHook().teleport(args[0], s, p.getLocation());
			p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
			return true;
			}
		
		return false;
	}

}
