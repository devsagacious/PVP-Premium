package com.sagaciousdevelopment.PVPPremium;

import org.bukkit.plugin.java.JavaPlugin;

import com.sagaciousdevelopment.PVPPremium.api.PlaceholderAPIHook;
import com.sagaciousdevelopment.PVPPremium.api.PlaceholdersHook;
import com.sagaciousdevelopment.PVPPremium.api.WorldguardHook;
import com.sagaciousdevelopment.PVPPremium.config.Configuration;
import com.sagaciousdevelopment.PVPPremium.leaderboard.LeaderboardHandler;
import com.sagaciousdevelopment.PVPPremium.listener.KillstreakHandler;
import com.sagaciousdevelopment.PVPPremium.stats.StatManager;
import com.sagaciousdevelopment.PVPPremium.stats.rank.SLManager;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	public String version = "1.0";
	public StatManager sm;
	public LeaderboardHandler lh;
	public SLManager sl;
	public KillstreakHandler kh;
	
	public PlaceholderAPIHook pa = null;
	public PlaceholdersHook ph = null;
	public WorldguardHook wh = null;
	
	@Override
	public void onEnable() {
		instance=this;
		new Configuration();
		sm = new StatManager();
		lh = new LeaderboardHandler();
	}
	
	@Override
	public void onDisable() {
		
	}

}
