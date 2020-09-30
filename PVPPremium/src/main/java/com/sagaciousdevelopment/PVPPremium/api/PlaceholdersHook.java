package com.sagaciousdevelopment.PVPPremium.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sagaciousdevelopment.PVPPremium.Core;
import com.sagaciousdevelopment.PVPPremium.stats.Stats;

import be.maximvdw.placeholderapi.PlaceholderAPI;



public class PlaceholdersHook {

	public PlaceholdersHook() {
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return ""+data.getKills();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_deaths", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return ""+data.getDeaths();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return ""+data.getKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_top_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return ""+data.getTopKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kdr", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return data.getKDR();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_chr", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return data.getHMR();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_hmr", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
					return data.getCHR();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					Stats data = Core.getInstance().sm.getData(e.getPlayer());
				return Core.getInstance().sl.getLevel(data.getXP()).getName();
				}
			});
	}
	}
	
	public String format(Player p, String s) {
		return PlaceholderAPI.replacePlaceholders(p, s);
	}
}

