package com.sagaciousdevelopment.PVPPremium.api;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.sagaciousdevelopment.PVPPremium.Core;

import net.milkbowl.vault.chat.Chat;

public class VaultHook implements Listener{
	private Chat chat;
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
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
		setupChat();
		}
		File f = new File(Core.getInstance().getDataFolder(), "config.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		use = conf.getBoolean("use-format");
		format = ChatColor.translateAlternateColorCodes('&', conf.getString("format"));
	}

	@EventHandler
	public void onFormat(AsyncPlayerChatEvent e) {
		if(use) {
			String f = format.replace("%level%", Core.getInstance().sl.getLevel(Core.getInstance().sm.getData(e.getPlayer()).getXP()).getName()).replace("%prefix%", chat.getPlayerPrefix(e.getPlayer())).replace("%player%", e.getPlayer().getName()).replace("%suffix%", chat.getPlayerSuffix(e.getPlayer())).replace("%message%", e.getMessage());
			if(Core.getInstance().ph!=null) {
				Core.getInstance().ph.format(e.getPlayer(), format);
			}
			if(Core.getInstance().pa!=null&&me.clip.placeholderapi.PlaceholderAPI.containsPlaceholders(format)) {
				me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), format);
			}
			e.setFormat(f);
		
		}
	}
}
