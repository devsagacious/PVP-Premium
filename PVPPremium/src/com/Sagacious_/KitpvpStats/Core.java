package com.Sagacious_.KitpvpStats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.Sagacious_.KitpvpStats.api.hook.HolographicHook;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholderAPIHook;
import com.Sagacious_.KitpvpStats.api.hook.PlaceholdersHook;
import com.Sagacious_.KitpvpStats.api.hook.VaultHook;
import com.Sagacious_.KitpvpStats.api.hook.WorldguardHook;
import com.Sagacious_.KitpvpStats.bounty.BountyManager;
import com.Sagacious_.KitpvpStats.command.CommandAddstats;
import com.Sagacious_.KitpvpStats.command.CommandAdminreset;
import com.Sagacious_.KitpvpStats.command.CommandLeaderboardrefresh;
import com.Sagacious_.KitpvpStats.command.CommandMoveleaderboard;
import com.Sagacious_.KitpvpStats.command.CommandRemovestats;
import com.Sagacious_.KitpvpStats.command.CommandSetstats;
import com.Sagacious_.KitpvpStats.command.CommandStats;
import com.Sagacious_.KitpvpStats.command.CommandStatsreset;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLManager;
import com.Sagacious_.KitpvpStats.handler.ActivityHandler;
import com.Sagacious_.KitpvpStats.handler.AntistatsHandler;
import com.Sagacious_.KitpvpStats.handler.KillstreakHandler;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;
import com.Sagacious_.KitpvpStats.sql.SQLConnection;
import com.Sagacious_.KitpvpStats.util.FileUtil;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	public BountyManager bm;
	public DataHandler dh;
	public LeaderboardHandler lh;
	public KillstreakHandler kh;
	public boolean useHolographic = false;
	
	public PlaceholderAPIHook pa = null;
	public PlaceholdersHook ph = null;
	public WorldguardHook wh = null;
	public String version = "1.0";
	public SLManager sl;
	public SQLConnection sql = null;
	
	public HolographicHook h = null;
	
	public String kdr_f;
	
	public String versionBM = "1.0";

	
	@Override
	public void onEnable() {
		instance = this;
		setupConfig();
		if (!getConfig().getString("version").equals(version)) {
            getLogger().info("Your configuration file was not up to date. Updating it now...");
            new FileUtil().updateConfig("config.yml");
            getLogger().info("Configuration file updated.");
        }
		kdr_f = getConfig().getString("kdr-format");
		if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			useHolographic = getConfig().getBoolean("use-holographicdisplays");
			if(useHolographic) {
			     h = new HolographicHook();
			}
		}
		if(getConfig().getBoolean("mysql-enabled")) {
			sql = new SQLConnection(getConfig().getString("mysql-database"), getConfig().getString("mysql-port"), getConfig().getString("mysql-username"), getConfig().getString("mysql-password"), getConfig().getString("mysql-host"));
		    sql.setupTables();
		}
		wh = new WorldguardHook();
		sl = new SLManager();
		dh = new DataHandler();
		bm = new BountyManager();
	    new ActivityHandler(); new CommandStats(); new CommandMoveleaderboard();
	    new CommandAdminreset(); new CommandStatsreset(); new CommandAddstats();
	    new CommandSetstats(); new CommandRemovestats();
		lh = new LeaderboardHandler();
		kh = new KillstreakHandler();
		new AntistatsHandler();
		getCommand("leaderboardrefresh").setExecutor(new CommandLeaderboardrefresh());
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			new VaultHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			ph = new PlaceholdersHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			pa = new PlaceholderAPIHook();pa.register();
		}
		
	}

	@Override
	public void onDisable() {
		for(UserData d : dh.getAllUserData()) {d.save(true);}
		lh.saveHologramLocations();
		if(h!=null) {
			h.killAll();
		}
		bm.save();
	}
	
	private File dataFolder;
	private File config;
	
	private void setupConfig() {
		dataFolder = getDataFolder();
		if(!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		config = new File(dataFolder, "config.yml");
		if(!config.exists()) {
			try(InputStream in = Core.getInstance().getResource("config.yml")){
			    Files.copy(in, config.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
