package com.sagaciousdevelopment.PVPPremium.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sagaciousdevelopment.PVPPremium.Core;

public class Configuration {
	
	public Configuration() {
		File dataFolder = Core.getInstance().getDataFolder();
		if(!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		File config = new File(dataFolder, "config.yml");
		if(!config.exists()) {
			try(InputStream in = Core.getInstance().getResource("config.yml")){
			    Files.copy(in, config.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		updateConfig();
	}
	
	public void updateConfig() {
        HashMap<String, Object> newConfig = getConfigVals();
        FileConfiguration c = Core.getInstance().getConfig();
        for (String var : c.getKeys(false)) {
            newConfig.remove(var);
        }
        if (newConfig.size()!=0) {
            for (String key : newConfig.keySet()) {
                c.set(key, newConfig.get(key));
            }
            try {
                c.set("version", Core.getInstance().version);
                c.save(new File(Core.getInstance().getDataFolder(), "config.yml"));
            } catch (IOException e) {}
        }
    }
    public HashMap<String, Object> getConfigVals() {
        HashMap<String, Object> var = new HashMap<>();
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringFromInputStream(Core.getInstance().getResource("config.yml")));
        } catch (InvalidConfigurationException e) {e.printStackTrace();}
        for (String key : config.getKeys(false)) {
            var.put(key, config.get(key));
        }
        return var;
    }
    @SuppressWarnings("resource")
	public String stringFromInputStream(InputStream in) {
        return new Scanner(in).useDelimiter("\\A").next();
    }

}
