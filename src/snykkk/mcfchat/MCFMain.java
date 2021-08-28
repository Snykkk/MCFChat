package snykkk.mcfchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import snykkk.mcfchat.file.FConfig;
import snykkk.mcfchat.listener.ChatEvent;

public class MCFMain extends JavaPlugin {

	public static MCFMain m;
	public String server_version = "";
	
	@Override
	public void onEnable() {
		
		m = this;
		
		reloadConfig();
		
		server_version = Bukkit.getServer().getClass().getPackage().getName();
		server_version = server_version.substring(server_version.lastIndexOf(".") + 1);
		server_version = server_version.substring(1, server_version.length());
		server_version = server_version.toUpperCase();
		
		Bukkit.getConsoleSender().sendMessage("§e----------§6===== §bMCF §6=====§e----------");
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§aPlugin: MCFChat");
		Bukkit.getConsoleSender().sendMessage("§aServer version: " + server_version);
		Bukkit.getConsoleSender().sendMessage("§aPlugin version: " + this.getDescription().getVersion() + (MCFUpdate.checkUpdate() ? " (Latest version)" : " (Need update)"));
		Bukkit.getConsoleSender().sendMessage("§aAuthor: Snykkk");
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§e------------- §b=========== §e-------------");

		
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
			new MCFPlaceholder().register();
		}

		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
		
	}
	
	@Override
	public void onDisable() {

		Bukkit.getConsoleSender().sendMessage("§bMCFChat disabling ...");
		
	}
	
	public void reloadConfig() {
		new FConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {
		
		if (a.equalsIgnoreCase("mcfchat") && (sender.isOp() || sender.hasPermission("mcfchat.admin"))) {
			if (args.length == 0) {
				sender.sendMessage("§e------§6===== §bMCFChat §6=====§e------");
				sender.sendMessage("§b/mcfchat reload");
				sender.sendMessage("§b/mcfchat version");
				sender.sendMessage("§e------------- §b=========== §e-------------");
			}
			
			if (args.length == 1 && args[0].equals("reload")) {
				reloadConfig();
				sender.sendMessage(FConfig.fc.getString("prefix").replaceAll("&", "§") + FConfig.fc.getString("lang.reload").replaceAll("&", "§"));
			}

			if (args.length == 1 && args[0].equals("version")) {
				sender.sendMessage("§e----------§6===== §bMCF §6=====§e----------");
				sender.sendMessage("");
				sender.sendMessage("§aPlugin: MCFChat");
				sender.sendMessage("§aServer version: " + server_version);
				sender.sendMessage("§aPlugin version: " + this.getDescription().getVersion() + (MCFUpdate.checkUpdate() ? " (Latest version)" : " (Need update)"));
				sender.sendMessage("§aAuthor: Snykkk");
				sender.sendMessage("");
				sender.sendMessage("§e------------- §b=========== §e-------------");
			}
		}
		
		if (a.equalsIgnoreCase("broadcast") && (sender.isOp() || sender.hasPermission("mcfchat.broadcast"))) {
			String msg = "";
			for(int i = 0; i < args.length; i++) {
				msg = msg + args[i] + " ";
			}
			Bukkit.broadcastMessage(FConfig.fc.getString("prefix").replaceAll("&", "§") + msg.replaceAll("&", "§"));
		}
		
		return true;
	}

}
