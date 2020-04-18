package com.Sagacious_.KitpvpStats.api.hook;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

import be.maximvdw.placeholderapi.PlaceholderAPI;



public class PlaceholdersHook {
	private DecimalFormat df = new DecimalFormat("##0.0");
	private String getKDR(UserData p) {
		if(p.getKills()==0&&p.getDeaths()==0) {return "0.0";}
		if(p.getKills()>p.getDeaths()&&p.getDeaths()==0) {return p.getKills()+".0";}
		if(p.getKills()==p.getDeaths()&&p.getKills()==0) {return "0.0";}
		return df.format(Double.valueOf((double)p.getKills()/(double)p.getDeaths()));
	}

	public PlaceholdersHook() {
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getKills();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_deaths", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getDeaths();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_top_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+data.getTopKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kdr", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return ""+getKDR(data);
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return data.getLevel().getName();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_progress", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return Core.getInstance().sl.getLevelProgress(data);
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_progress_percent", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
					return Core.getInstance().sl.getLevelProgressPercent(data);
				}
			});
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(Core.getInstance().lh.orderedKills.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+Core.getInstance().lh.orderedKills.get(f).getKills():Core.getInstance().lh.orderedKills.get(f).getName();
						}
						return "None";
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_deaths_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(Core.getInstance().lh.orderedDeaths.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+Core.getInstance().lh.orderedDeaths.get(f).getDeaths():Core.getInstance().lh.orderedDeaths.get(f).getName();
						}
						return "None";
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_killstreak_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(Core.getInstance().lh.orderedKillstreak.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+Core.getInstance().lh.orderedKillstreak.get(f).getKillstreak():Core.getInstance().lh.orderedKillstreak.get(f).getName();
						}
						return "None";
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kdr_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(Core.getInstance().lh.orderedKDR.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+Core.getInstance().lh.orderedKDR.get(f).getKDR():Core.getInstance().lh.orderedKDR.get(f).getName();
						}
						return "None";
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_chr_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(Core.getInstance().lh.orderedCHR.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+Core.getInstance().lh.orderedCHR.get(f).getCHR():Core.getInstance().lh.orderedCHR.get(f).getName();
						}
						return "None";
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_hmr_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(Core.getInstance().lh.orderedHMR.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+Core.getInstance().lh.orderedHMR.get(f).getHMR():Core.getInstance().lh.orderedHMR.get(f).getName();
						}
						return "None";
					}
				});
			}
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_curr_xp", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
				return ""+data.getXP();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_nextlevel_xp", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = Core.getInstance().dh.getData(e.getPlayer());
				return ""+Core.getInstance().sl.getNextLevelXP(data.getLevel());
				}
			});
	}
	}
	
	public String format(Player p, String s) {
		return PlaceholderAPI.replacePlaceholders(p, s);
	}
}

