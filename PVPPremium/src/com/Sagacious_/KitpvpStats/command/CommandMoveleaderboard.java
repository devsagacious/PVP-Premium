package com.Sagacious_.KitpvpStats.command;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler.Hologram;

public class CommandMoveleaderboard implements CommandExecutor{
	private DecimalFormat df = new DecimalFormat("####0.0##############");
	
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
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak/kdr/chr/hmr>");
				return true;
			}
			if(!Core.getInstance().useHolographic) {
			List<Hologram> toMove = null;
			boolean f = false;
			if(args[0].equalsIgnoreCase("kills")) {
				toMove = Core.getInstance().lh.hologramsKill;f=true;
			}else if(args[0].equalsIgnoreCase("deaths")) {
				toMove = Core.getInstance().lh.hologramsDeath;f=true;
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				toMove = Core.getInstance().lh.hologramsKillstreak;f=true;
			}else if(args[0].equalsIgnoreCase("kdr")) {
				toMove = Core.getInstance().lh.hologramsKDR;f=true;
			}else if(args[0].equalsIgnoreCase("chr")) {
				toMove = Core.getInstance().lh.hologramsCHR;f=true;
			}else if(args[0].equalsIgnoreCase("hmr")) {
				toMove = Core.getInstance().lh.hologramsHMR;f=true;
			}
			if(toMove == null || !f) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak/kdr/chr/hmr>");
				return true;
			}else {
				
				Location tp = new Location(p.getWorld(), Double.valueOf(df.format(p.getLocation().getX()).replaceAll(",", ".")), Double.valueOf(df.format(p.getLocation().getY()).replaceAll(",", ".")), Double.valueOf(df.format(p.getLocation().getZ()).replaceAll(",", ".")));
				toMove.get(0).teleport(tp);
				for(int i = 1; i < toMove.size(); i++) {
					tp.setY(tp.getY()-0.25D);
					toMove.get(i).teleport(tp);
				}
				p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
				return true;
			}
			}
			com.gmail.filoghost.holographicdisplays.api.Hologram s = null;
			if(args[0].equalsIgnoreCase("kills")) {
				s = Core.getInstance().h.killHologram;
			}else if(args[0].equalsIgnoreCase("deaths")) {
				s = Core.getInstance().h.deathsHologram;
			}else if(args[0].equalsIgnoreCase("killstreak")) {
				s = Core.getInstance().h.killstreakHologram;
			}else if(args[0].equalsIgnoreCase("kdr")) {
				s = Core.getInstance().h.kdrHologram;
			}else if(args[0].equalsIgnoreCase("chr")) {
				s = Core.getInstance().h.chrHologram;
			}else if(args[0].equalsIgnoreCase("hmr")) {
				s = Core.getInstance().h.hmrHologram;
			}
			if(s==null) {
				p.sendMessage("§c§lPVPStats §8| §rInvalid command syntax, use /moveleaderboard <kills/deaths/killstreak/kdr/chr/hmr>");
				return true;
			}
			s.teleport(p.getLocation());
			p.sendMessage("§c§lPVPStats §8| §rMoved leaderboard to your location!");
			return true;
			}
		
		return false;
	}

}
