package com.Sagacious_.KitpvpStats.bounty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.bounty.command.CommandBounty;
import com.Sagacious_.KitpvpStats.bounty.gui.BountyGUI;
import com.Sagacious_.KitpvpStats.bounty.item.TargetSelector;
import com.Sagacious_.KitpvpStats.bounty.listener.DeathListener;
import com.Sagacious_.KitpvpStats.bounty.listener.PayoutHandler;
import com.Sagacious_.KitpvpStats.bounty.listener.TargetListener;
import com.Sagacious_.KitpvpStats.data.DataHandler;
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.sql.SQLConnection;
import com.Sagacious_.KitpvpStats.util.FileUtil;

public class BountyManager {
	public HashMap<UUID, List<Bounty>> bounties = new HashMap<UUID, List<Bounty>>();
	public TargetSelector ts;
	public File f;
	private File dir;
	public FileConfiguration conf;
	public int time;
	private Sound bounty;
	public Sound survived;
	public Sound kill;
	public Sound killed;
	
	private boolean enabled;
	public boolean isEnabled() {
		return enabled;
	}
	
	public BountyGUI bg;
	public TargetListener tl;
	private boolean error = false;
	public PayoutHandler ph;
	public List<String> bountySurvivedMsg = new ArrayList<String>();
	
	private SQLConnection sql = Core.getInstance().getSQLConnection();
	private DataHandler dh = Core.getInstance().getDataHandler();
	
	private boolean soundExists(String sound) {
		for(Sound s : Sound.values()) {
			if(s.toString().equalsIgnoreCase(sound)) {
				return true;
			}
		}
		return false;
	}
	
	public BountyManager() {
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
		f = new File(Core.getInstance().getDataFolder(), "bounty.yml");
		dir = new File(Core.getInstance().getDataFolder(), "bounties");
		if(!dir.exists()) {
			dir.mkdir();
		}
		if(!f.exists()) {
			 try (InputStream in = Core.getInstance().getResource("bounty.yml")) {
                 Files.copy(in, f.toPath());
             } catch (IOException e) {
                 e.printStackTrace();
             }
		}
		this.conf = YamlConfiguration.loadConfiguration(f);
		if (!conf.getString("version").equals(Core.getInstance().getVersionBM())) {
            new FileUtil().updateConfig("bounty.yml");
	}
		if(!soundExists(conf.getString("sound-bountied"))||!soundExists(conf.getString("sound-survival"))||!soundExists(conf.getString("sound-kill"))||!soundExists(conf.getString("sound-killed"))) {
			Core.getInstance().getLogger().info("ISSUE IN YOUR BOUNTY.YML");
			Core.getInstance().getLogger().info("");
			Core.getInstance().getLogger().info("The sounds in your bounty.yml do not exist and thus this will not work!");
			Core.getInstance().getLogger().info("");
			Core.getInstance().getLogger().info("ISSUE IN YOUR BOUNTY.YML");
			error=true;
			return;
		}
		
		bounty = Sound.valueOf(conf.getString("sound-bountied"));
		survived = Sound.valueOf(conf.getString("sound-survival"));
		kill = Sound.valueOf(conf.getString("sound-kill"));
		killed = Sound.valueOf(conf.getString("sound-killed"));
		}
	}
	
	public void register() {
		if(conf.getBoolean("bounty-enabled")&&!error) {
			enabled=true;
			loadAll();
			time = conf.getInt("bounty-time");
		bountySurvivedMsg = translateList(conf.getStringList("bounty-survived-message"));
		ph = new PayoutHandler();
		if(conf.getBoolean("tracker-enabled")) {
			ts = new TargetSelector();
		}
		Bukkit.getPluginManager().registerEvents(new DeathListener(), Core.getInstance());
		tl = new TargetListener();
		Bukkit.getPluginManager().registerEvents(tl, Core.getInstance());
		bg = new BountyGUI();
		Bukkit.getPluginManager().registerEvents(bg, Core.getInstance());
		new CommandBounty();
		if(sql!=null) {
			updateBounties();
		}
		}
	}
	
	public HashMap<Bounty, Double> getBounties(double amount){
		HashMap<Bounty, Double> t = new HashMap<Bounty, Double>();
		for(Bounty u : getBounties()) {
			if(u.active) {
			if(u.getAmount()==amount) {
				t.put(u, amount);
			}
		}
		}
		return t;
	}
	
	public HashMap<Bounty, Double> getBountiesTotal(){
		HashMap<Bounty, Double> t = new HashMap<Bounty, Double>();
		for(Bounty u : getBounties()) {
			if(u.active) {
				t.put(u, u.getAmount());
			}
		}
		return t;
	}
	
	public void playSound(Player p, Sound s) {
		UserData u = dh.getData(p);
		if(System.currentTimeMillis()-u.lastSound<300) {return;}
		p.playSound(p.getLocation(), s, 1F, 1F);
		u.lastSound=System.currentTimeMillis();
	}
	
	private Random r = new Random();
	private String generateID() {
		String s = "";
		String syntax = "abcdefghijklmnopqrstuvwxyz0123456789";
		for(int i = 0; i < 16; i++) {
			s = s + syntax.charAt(r.nextInt(syntax.length()));
		}
		return s;
	}
	
	private void updateBounties() {
		for(Bounty b : getBounties()) {
			try {
				ResultSet set = sql.prepareStatement("SELECT * FROM Bounty WHERE id='" + b.id + "'").executeQuery();
				while(set.next()) {
				boolean console = set.getString("uuidOwner").equals("null");
				Bounty s = new Bounty(this, set.getString("id"), console?null:UUID.fromString(set.getString("uuidOwner")), UUID.fromString(set.getString("uuidTarget")), set.getDouble("amount"), set.getInt("timeLeft"), console);
				if(s.getTimeLeft()>b.getTimeLeft()) {
			    b.save(false);
				}else {
				replaceBounty(b, s);
				}
				}
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void replaceBounty(Bounty first, Bounty second) {
		UUID z = null;
		for(Entry<UUID, List<Bounty>> s : bounties.entrySet()) {
			for(Bounty f : s.getValue()) {
				if(f.equals(first)) {
					z=s.getKey();
				}
			}
		}
		if(z!=null) {
			List<Bounty> q = bounties.get(z);
			q.remove(first);q.add(second);
			bounties.put(z, q);
		}
	}
	
	private void loadAll() {
		if(sql==null) {
		FileConfiguration confe;
		for(File z : dir.listFiles()) {
			confe = YamlConfiguration.loadConfiguration(z);
			if(!confe.getBoolean("active")) {return;}
			boolean console = !confe.getKeys(false).contains("uuidOwner");
			Bounty b = new Bounty(this, z.getName().replace(".yml", ""), console?null:UUID.fromString(confe.getString("uuidOwner")), UUID.fromString(confe.getString("uuidTarget")), 
					confe.getInt("amount"), confe.getInt("timeLeft"), console);
			if(bounties.containsKey(b.getUuidTarget())) {
				List<Bounty> s = bounties.get(b.getUuidTarget());
				s.add(b);
				bounties.put(b.getUuidTarget(), s);
			}else {
				bounties.put(b.getUuidTarget(), new ArrayList<Bounty>(Arrays.asList(b)));
			}
		}
		}else {
			try {
				ResultSet set = sql.prepareStatement("SELECT * FROM Bounty").executeQuery();
				while(set.next()) {
					boolean console = set.getString("uuidOwner").equals("null");
					Bounty b = new Bounty(this, set.getString("id"), console?null:UUID.fromString(set.getString("uuidOwner")), UUID.fromString(set.getString("uuidTarget")), set.getDouble("amount"), set.getInt("timeLeft"), console);
				    createBounty(b.getUuidOwner(), b.getUuidTarget(), b.getAmount(), b);
				}
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createBounty(UUID owner, UUID target, double amount, Bounty z) {
		String id = generateID();
		Bounty b = null;
		boolean fq = false;
		for(Bounty zs : getBounties()) {
			if((zs.getUuidOwner()==null&&owner==null||zs.getUuidOwner()==owner)&&zs.getUuidTarget()==target) {
				fq=true;
				zs.setAmount(zs.getAmount()+amount);
			}
		}
		if(!fq) {
		if(z==null) {b=new Bounty(this, id, owner, target, amount, time, owner==null);}else {
			b=z;
		}
		if(bounties.containsKey(b.getUuidTarget())) {
			List<Bounty> s = bounties.get(b.getUuidTarget());
			s.add(b);
			bounties.put(b.getUuidTarget(), s);
		}else {
			bounties.put(b.getUuidTarget(), new ArrayList<Bounty>(Arrays.asList(b)));
		}
		if(z==null) {
		playSound(Bukkit.getPlayer(target), bounty);
		}
		}
	}
	
	public List<Bounty> getBounties(){
		List<Bounty> s = new ArrayList<Bounty>();
		for(Entry<UUID, List<Bounty>> b : bounties.entrySet()) {
			for(Bounty f : b.getValue()) {
				if(f.active) {
					s.add(f);
				}
			}
		}
		return s;
	}
	
	public void save() {
		if(!bounties.isEmpty()) {
		for(Entry<UUID, List<Bounty>> b : bounties.entrySet()) {
			for(Bounty bo : b.getValue()) {
				bo.save(true);
			}
		}
		}
	}
	
	public void removeBounty(Bounty b) {
				b.active=false;
				if(sql==null) {
				File f = new File(dir, b.id + ".yml");
				f.delete();
				}else {
					Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
						public void run() {
							sql.executeStatement("DELETE FROM Bounty WHERE id='" + b.id + "';");
						}
					});
			}
		}
	
	public List<String> translateList(List<String> f){
		List<String> s = new ArrayList<String>();
		for(String q : f) {
			s.add(ChatColor.translateAlternateColorCodes('&', q));
		}
		return s;
	}
	
	public List<Bounty> getBounties(UUID uuid){
		return bounties.get(uuid);
	}

}
