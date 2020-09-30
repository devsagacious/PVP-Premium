package com.Sagacious_.KitpvpStats.api.hook;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLManager;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;

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
		LeaderboardHandler lh = Core.getInstance().getLeaderboardHandler();
		DataHandler dh = Core.getInstance().getDataHandler();
		SLManager sl = Core.getInstance().getSLManager();
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return ""+data.getKills();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_deaths", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return ""+data.getDeaths();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return ""+data.getKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_top_killstreak", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return ""+data.getTopKillstreak();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kdr", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return ""+getKDR(data);
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return data.getLevel().getName();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_progress", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return sl.getLevelProgress(data);
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_progress_percent", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return sl.getLevelProgressPercent(data);
				}
			});
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_kills_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(lh.orderedKills.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.killTop.get(f):lh.orderedKills.get(f).getName();
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
						if(lh.orderedDeaths.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.deathTop.get(f):lh.orderedDeaths.get(f).getName();
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
						if(lh.orderedKillstreak.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.killstreakTop.get(f):lh.orderedKillstreak.get(f).getName();
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
						if(lh.orderedKDR.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.kdrTop.get(f):lh.orderedKDR.get(f).getName();
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
						if(lh.orderedCHR.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.chrTop.get(f):lh.orderedCHR.get(f).getName();
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
						if(lh.orderedHMR.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.hmrTop.get(f):lh.orderedHMR.get(f).getName();
						}
						return "None";
					}
				});
			}
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_level_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(lh.orderedLevel.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+lh.levelTop.get(f):lh.orderedLevel.get(f).getName();
						}
						return "None";
					}
				});
			}
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_curr_xp", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
				return ""+data.getXP();
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_nextlevel_xp", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
				return ""+sl.getNextLevelXP(data.getLevel());
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_combatlog", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					return ""+Core.getInstance().getCombatlogHandler().getCombatLog(e.getPlayer().getUniqueId());
				}
			});
			be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_uniquekills", new be.maximvdw.placeholderapi.PlaceholderReplacer() {
				@Override
				public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
					UserData data = dh.getData(e.getPlayer());
					return ""+data.getUniqueKills().entrySet().size();
				}
			});
			for(int i = 1; i < 11; i++) {
				be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(Core.getInstance(), "pvpstats_uniquekilltop_" + i, new be.maximvdw.placeholderapi.PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(be.maximvdw.placeholderapi.PlaceholderReplaceEvent e) {
						int f = Integer.parseInt(e.getPlaceholder().split("_")[2])-1;
						if(lh.orderedUnqiuekills.size()>f) {
							return e.getPlaceholder().endsWith("amount")?""+(int)Math.round(lh.unqiuekillTop.get(f)):lh.orderedUnqiuekills.get(f).getName();
						}
						return "None";
					}
				});
			}
	}
	}
	
	public String format(Player p, String s) {
		return PlaceholderAPI.replacePlaceholders(p, s);
	}
}

