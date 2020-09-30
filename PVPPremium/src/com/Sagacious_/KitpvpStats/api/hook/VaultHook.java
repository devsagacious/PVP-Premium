package com.Sagacious_.KitpvpStats.api.hook;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.Sagacious_.KitpvpStats.Core;
import com.Sagacious_.KitpvpStats.data.UserData;

import net.milkbowl.vault.chat.Chat;

public class VaultHook implements Listener{
private Chat chat = null;
private boolean use = false;
private String format;

private boolean setupChat()
{
    RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
    if (chatProvider != null) {
        chat = chatProvider.getProvider();
}
    return (chat != null);
}

public VaultHook() {
	Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
	setupChat();
	File f = new File(Core.getInstance().getDataFolder(), "config.yml");
	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		use = conf.getBoolean("use-format");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("format"));
}

@EventHandler
public void onFormat(AsyncPlayerChatEvent e) {
	PlaceholdersHook ph = Core.getInstance().getMVDWPlaceholderAPIHook();
	PlaceholderAPIHook pa = Core.getInstance().getPlaceholderAPIHook();
	if(use) {
		String f = format;
		if(ph!=null) {
			f=ph.format(e.getPlayer(), f);
		}
		if(pa!=null&&me.clip.placeholderapi.PlaceholderAPI.containsPlaceholders(f)) {
			f=me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), f);
		}
		UserData dh = Core.getInstance().getDataHandler().getData(e.getPlayer());
		if(chat!=null){f = f.replace("{prefix}", ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(e.getPlayer()))).replace("{suffix}", ChatColor.translateAlternateColorCodes('&', chat.getPlayerSuffix(e.getPlayer())));}
		f=f.replace("{pvplevel}", dh.getLevel().getName())
				.replace("{player}", e.getPlayer().getName())
				.replace("{displayname}", e.getPlayer().getDisplayName());
		f=f.replace("{message}", e.getMessage());
		f=f.replace("%", "%%");
		e.setFormat(f);
	}
}
}
