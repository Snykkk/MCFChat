package snykkk.mcfchat.listener;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import snykkk.mcfchat.file.FConfig;

public class ChatEvent implements Listener {

	@EventHandler
	public void chat(AsyncPlayerChatEvent e ) {
		Player p = e.getPlayer();
		String msg = e.getMessage().replaceAll("%", "%%");
		
		if (FConfig.fc.getString("use-format").equals("default")) {
			String chat = FConfig.fc.getString("default-format").replace("&", "§")
													    .replaceAll("%displayname%", p.getDisplayName())
													    .replaceAll("%mcf_chatcolor%", getColor(p));
			
			if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				chat = PlaceholderAPI.setPlaceholders(p, chat);
			}
			
			e.setFormat(chat.replaceAll("%message%", msg));
		} else if (FConfig.fc.getString("use-format").equals("per-world")) {
			String world = p.getWorld().getName();
			if (p.hasPermission("mcfchat.bypass")) {
				String chat = FConfig.fc.getString("bypass-format").replace("&", "§")
														   .replaceAll("%displayname%", p.getDisplayName())
														    .replaceAll("%mcf_chatcolor%", getColor(p));

				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					chat = PlaceholderAPI.setPlaceholders(p, chat);
				}
				e.setFormat(chat.replaceAll("%message%", msg));
			} else {
				if (FConfig.fc.getBoolean("per-world." + world + ".enable")) {
					String chat = FConfig.fc.getString("per-world." + world + ".format").replace("&", "§")
																				        .replaceAll("%displayname%", p.getDisplayName())
																					    .replaceAll("%mcf_chatcolor%", getColor(p));
		
					if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
						chat = PlaceholderAPI.setPlaceholders(p, chat);
					}
					Set<Player> recipients = e.getRecipients();
					for (Player pi : recipients) {
						if (!pi.getWorld().getName().equals(world) && !pi.hasPermission("mcfchat.bypass")) {
							recipients.remove(pi);
							continue;
						}
					}
					
					e.setFormat(chat.replaceAll("%message%", msg));
				} else {
					p.sendMessage(FConfig.fc.getString("lang.disable").replaceAll("&", "§"));
				}
			}
		} else if (FConfig.fc.getString("use-format").equals("world-group")) {
			String w = p.getWorld().getName();
			if (p.hasPermission("mcfchat.bypass")) {
				String chat = FConfig.fc.getString("bypass-format").replace("&", "§")
														   .replaceAll("%displayname%", p.getDisplayName())
														    .replaceAll("%mcf_chatcolor%", getColor(p));

				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					chat = PlaceholderAPI.setPlaceholders(p, chat);
				}
				e.setFormat(chat.replaceAll("%message%", msg));
			} else {
				String g = "";

				Set<Player> recipients = e.getRecipients();
				
				recipients.clear();
				
				for (String group : FConfig.fc.getConfigurationSection("world-group").getKeys(false)) {
					for (String world : FConfig.fc.getStringList("world-group." + group + ".worlds")) {
						if (world.equals(w)) { 
							g = group;
							break; 
						}
					}
				}
				
				if (FConfig.fc.getConfigurationSection("world-group").getKeys(false).contains(g)) {
					for (String world : FConfig.fc.getStringList("world-group." + g + ".worlds")) {
						Bukkit.getWorld(world).getPlayers().forEach(s -> recipients.add(s));
					}
					
					String chat = FConfig.fc.getString("world-group." + g + ".format").replace("&", "§")
						    												  .replaceAll("%displayname%", p.getDisplayName())
																			    .replaceAll("%mcf_chatcolor%", getColor(p));
		
					if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
						chat = PlaceholderAPI.setPlaceholders(p, chat);
					}
					
					for (Player pi : Bukkit.getOnlinePlayers()) {
						if (pi.hasPermission("mcfchat.bypass")) {
							recipients.add(pi);
						}
					}
					
					e.setFormat(chat.replaceAll("%message%", msg));

				} else {
					p.sendMessage(FConfig.fc.getString("lang.not-exists").replaceAll("%group%", g).replaceAll("&", "§"));
				}
			}
		}
	}
	
	public String getColor(Player p) {
		for (String chat : FConfig.fc.getConfigurationSection("chat-color").getKeys(false)) {
			if (p.hasPermission(FConfig.fc.getString("chat-color." + chat + ".permission"))) {
				return FConfig.fc.getString("chat-color." + chat + ".color").replaceAll("&", "§");
			}
		}
		return "";
	}

}
