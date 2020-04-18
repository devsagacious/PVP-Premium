package com.Sagacious_.KitpvpStats.bounty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
import com.Sagacious_.KitpvpStats.data.UserData;
import com.Sagacious_.KitpvpStats.util.FileUtil;

public class BountyManager {
	public HashMap<UUID, List<Bounty>> bounties = new HashMap<UUID, List<Bounty>>();
	public TargetSelector ts;
	private File f;
	private File dir;
	public FileConfiguration conf;
	private int time;
	private Sound bounty;
	public Sound survived;
	public Sound kill;
	public Sound killed;
	
	public BountyGUI bg;
	public TargetListener tl;
	public PayoutHandler ph;
	public List<String> bountySurvivedMsg = new ArrayList<String>();
	
	public BountyManager() {
		Core.getInstance().bm = this;
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
		conf = YamlConfiguration.loadConfiguration(f);
		bounty = Sound.valueOf(conf.getString("sound-bountied"));
		survived = Sound.valueOf(conf.getString("sound-survival"));
		kill = Sound.valueOf(conf.getString("sound-kill"));
		killed = Sound.valueOf(conf.getString("sound-killed"));
		if (!conf.getString("version").equals(Core.getInstance().versionBM)) {
            new FileUtil().updateConfig("bounty.yml");
	}
			if(conf.getBoolean("bounty-enabled")) {
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
			}
		}
	}
	
	public void playSound(Player p, Sound s) {
		UserData u = Core.getInstance().dh.getData(p);
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
	
	private void loadAll() {
		FileConfiguration confe;
		for(File z : dir.listFiles()) {
			confe = YamlConfiguration.loadConfiguration(z);
			if(!confe.getBoolean("active")) {return;}
			Bounty b = new Bounty(z.getName().replace(".yml", ""), UUID.fromString(confe.getString("uuidOwner")), UUID.fromString(confe.getString("uuidTarget")), 
					confe.getInt("amount"), confe.getInt("timeLeft"));
			if(bounties.containsKey(b.getUuidTarget())) {
				List<Bounty> s = bounties.get(b.getUuidTarget());
				s.add(b);
				bounties.put(b.getUuidTarget(), s);
			}else {
				bounties.put(b.getUuidTarget(), new ArrayList<Bounty>(Arrays.asList(b)));
			}
		}
	}
	
	public void createBounty(UUID owner, UUID target, double amount) {
		String id = generateID();
		Bounty b = new Bounty(id, owner, target, amount, time);
		if(bounties.containsKey(b.getUuidTarget())) {
			List<Bounty> s = bounties.get(b.getUuidTarget());
			s.add(b);
			bounties.put(b.getUuidTarget(), s);
		}else {
			bounties.put(b.getUuidTarget(), new ArrayList<Bounty>(Arrays.asList(b)));
		}
		playSound(Bukkit.getPlayer(target), bounty);
	}
	
	public List<Bounty> getBounties(){
		List<Bounty> s = new ArrayList<Bounty>();
		for(Entry<UUID, List<Bounty>> b : bounties.entrySet()) {
				s.addAll(b.getValue());
		}
		return s;
	}
	
	public void save() {
		if(!bounties.isEmpty()) {
		for(Entry<UUID, List<Bounty>> b : bounties.entrySet()) {
			for(Bounty bo : b.getValue()) {
				bo.save();
			}
		}
		}
	}
	
	public void removeBounty(Bounty b) {
		for(Entry<UUID, List<Bounty>> s : bounties.entrySet()) {
			for(Bounty bo : s.getValue()) {
				if(bo.equals(b)){
					s.getValue().remove(b);
				}
				b.active=false;
				File f = new File(dir, b.id + ".yml");
				f.delete();
			}
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
