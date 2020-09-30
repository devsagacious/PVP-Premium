package com.Sagacious_.KitpvpStats.bounty.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.Sagacious_.KitpvpStats.Core;

import net.milkbowl.vault.economy.Economy;

public class PayoutHandler {
	private boolean tax;
	private int tax_amount=0;
	private String tax_payment;
	private String survival;
	private int survivalPayout;
	private Economy econ = null;
	
	
	public PayoutHandler() {
		FileConfiguration conf = YamlConfiguration.loadConfiguration(Core.getInstance().getBountyManager().f);
		tax = conf.getBoolean("tax-enabled");
		if(tax) {tax_amount = conf.getInt("tax-amount");}
		tax_payment = ChatColor.translateAlternateColorCodes('&', conf.getString("tax-payment"));
		survival = ChatColor.translateAlternateColorCodes('&', conf.getString("survival"));
		survivalPayout = conf.getInt("bounty-time-payout");
		setupEconomy();
	}
	
	public boolean hasAmount(Player p, double amount) {
		return econ.has(p, amount);
	}
	
	public void subtract(Player p, double amount) {
		econ.withdrawPlayer(p, amount);
	}
	
	private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public void pay(Player p, double amount) {
		double amounts = amount/100*(100-tax_amount);
		if(amounts<amount) {p.sendMessage(tax_payment.replace("%amount%", ""+(amount-amounts)));}
		econ.depositPlayer(p, amounts);
	}
	
	public void survivalPay(Player p, String bountier, double amount) {
		double amounts = amount/100*survivalPayout;
		econ.depositPlayer(p, amounts);
		p.sendMessage(survival.replace("%bountier%", bountier).replace("%amount%", ""+amounts));
	}

}
