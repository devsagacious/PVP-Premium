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
import com.Sagacious_.KitpvpStats.command.CommandRemoveleaderboard;
import com.Sagacious_.KitpvpStats.command.CommandRemovestats;
import com.Sagacious_.KitpvpStats.command.CommandSetstats;
import com.Sagacious_.KitpvpStats.command.CommandStats;
import com.Sagacious_.KitpvpStats.command.CommandStatsreset;
import com.Sagacious_.KitpvpStats.command.leaderboard.CommandLeaderboard;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.data.rank.SLManager;
import com.Sagacious_.KitpvpStats.handler.ActivityHandler;
import com.Sagacious_.KitpvpStats.handler.AntistatsHandler;
import com.Sagacious_.KitpvpStats.handler.CombatlogHandler;
import com.Sagacious_.KitpvpStats.handler.KillstreakHandler;
import com.Sagacious_.KitpvpStats.handler.PlayerheadHandler;
import com.Sagacious_.KitpvpStats.leaderboard.LeaderboardHandler;
import com.Sagacious_.KitpvpStats.metrics.Metrics;
import com.Sagacious_.KitpvpStats.sql.SQLConnection;
import com.Sagacious_.KitpvpStats.util.ActionBarAPI;
import com.Sagacious_.KitpvpStats.util.FileUtil;
import com.Sagacious_.KitpvpStats.util.ItemUtil;

public class Core extends JavaPlugin{
	
	private static Core instance;
	public static Core getInstance() {
		return instance;
	}
	
	private BountyManager bm;
	private DataHandler dh;
	private LeaderboardHandler lh;
	private KillstreakHandler kh;
	private boolean useHolographic = false;
	
	private PlaceholderAPIHook pa = null;
	private PlaceholdersHook ph = null;
	private WorldguardHook wh = null;
	private String version = "1.6";
	private SLManager sl;
	private SQLConnection sql = null;
	private CombatlogHandler cl;
	
	private HolographicHook h = null;
	
	private String kdr_f;
	
	private String versionBM = "1.1";
	
	public BountyManager getBountyManager() {
		return bm;
	}
	
	public DataHandler getDataHandler() {
		return dh;
	}
	
	public LeaderboardHandler getLeaderboardHandler() {
		return lh;
	}
	
	public KillstreakHandler getKillstreakHandler() {
		return kh;
	}
	
	public boolean doUseHolographic() {
		return useHolographic;
	}
	
	public PlaceholderAPIHook getPlaceholderAPIHook() {
		return pa;
	}
	
	public PlaceholdersHook getMVDWPlaceholderAPIHook() {
		return ph;
	}
	
	public WorldguardHook getWorldguardHook() {
		return wh;
	}
	
	public String getVersion() {
		return version;
	}
	
	public SLManager getSLManager() {
		return sl;
	}
	
	public SQLConnection getSQLConnection() {
		return sql;
	}
	
	public HolographicHook getHolographicHook() {
		return h;
	}
	
	public String kdr_f() {
		return kdr_f;
	}
	
	public String getVersionBM() {
		return versionBM;
	}
	
	public CombatlogHandler getCombatlogHandler() {
		return cl;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		setupConfig();
		if (!getConfig().getString("version").equals(version)) {
            getLogger().info("Your configuration file was not up to date. Updating it now...");
            new FileUtil().updateConfig("config.yml");
            getLogger().info("Configuration file updated.");
        }
		new ItemUtil();
		new Metrics(this, 7877);
		kdr_f = getConfig().getString("kdr-format");
		if(getConfig().getBoolean("mysql-enabled")) {
			sql = new SQLConnection(getConfig().getString("mysql-database"), getConfig().getString("mysql-port"), getConfig().getString("mysql-username"), getConfig().getString("mysql-password"), getConfig().getString("mysql-host"));
		    sql.setupTables();
		}
		dh = new DataHandler();
		wh = new WorldguardHook();
		sl = new SLManager();
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
		bm = new BountyManager();
		bm.register();
		}
	    new ActivityHandler(); new CommandStats();
	    new CommandAdminreset(); new CommandStatsreset(); new CommandAddstats();
	    new CommandSetstats(); new CommandRemovestats();
		lh = new LeaderboardHandler();
		h = new HolographicHook();
		new CommandMoveleaderboard();
		kh = new KillstreakHandler();
		new AntistatsHandler(); new PlayerheadHandler();
		getCommand("leaderboardrefresh").setExecutor(new CommandLeaderboardrefresh());
		new CommandRemoveleaderboard();
		new CommandLeaderboard();
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			new VaultHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			ph = new PlaceholdersHook();
		}
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			pa = new PlaceholderAPIHook();pa.register();
		}
		cl = new CombatlogHandler();
		new ActionBarAPI().onEnable();
	}

	@Override
	public void onDisable() {
		for(UserData d : dh.getAllUserData()) {d.save(true);}
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
