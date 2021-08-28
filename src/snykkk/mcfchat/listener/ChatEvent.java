package snykkk.mcfchat.listener;

import java.util.LinkedList;
import java.util.List;

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
													    .replaceAll("%displayname%", p.getDisplayName());
			
			if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				chat = PlaceholderAPI.setPlaceholders(p, chat);
			}
			
			e.setFormat(chat.replaceAll("%message%", msg));
		} else if (FConfig.fc.getString("use-format").equals("per-world")) {
			String world = p.getWorld().getName();
			if (p.hasPermission("mcfchat.bypass")) {
				String chat = FConfig.fc.getString("bypass-format").replace("&", "§")
														   .replaceAll("%displayname%", p.getDisplayName());

				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					chat = PlaceholderAPI.setPlaceholders(p, chat);
				}
				e.setFormat(chat.replaceAll("%message%", msg));
			} else {
				e.setCancelled(true);
				if (FConfig.fc.getBoolean("per-world." + world + ".enable")) {
					String chat = FConfig.fc.getString("per-world." + world + ".format").replace("&", "§")
																				        .replaceAll("%displayname%", p.getDisplayName());
		
					if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
						chat = PlaceholderAPI.setPlaceholders(p, chat);
					}
					
					for (Player pi : p.getWorld().getPlayers()) {
						if (pi.hasPermission("mcfchat.bypass")) {
							continue;
						}
						pi.sendMessage(chat.replaceAll("%message%", msg));
					}
					
					for (Player pi : Bukkit.getOnlinePlayers()) {
						if (pi.hasPermission("mcfchat.bypass")) {
							pi.sendMessage(chat.replaceAll("%message%", msg));
						}
					}
					
					System.out.println(chat.replaceAll("%message%", msg));
				} else {
					p.sendMessage(FConfig.fc.getString("lang.disable").replaceAll("&", "§"));
				}
			}
		} else if (FConfig.fc.getString("use-format").equals("world-group")) {
			String w = p.getWorld().getName();
			if (p.hasPermission("mcfchat.bypass")) {
				String chat = FConfig.fc.getString("bypass-format").replace("&", "§")
														   .replaceAll("%displayname%", p.getDisplayName());

				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					chat = PlaceholderAPI.setPlaceholders(p, chat);
				}
				e.setFormat(chat.replaceAll("%message%", msg));
			} else {
				e.setCancelled(true);
				String g = "";
				
				List<Player> list = new LinkedList<Player>();
				
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
						Bukkit.getWorld(world).getPlayers().forEach(s -> list.add(s));
					}
					
					String chat = FConfig.fc.getString("world-group." + g + ".format").replace("&", "§")
						    												  .replaceAll("%displayname%", p.getDisplayName());
		
					if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
						chat = PlaceholderAPI.setPlaceholders(p, chat);
					}
					
					for (Player pi : list) {
						if (pi.hasPermission("mcfchat.bypass")) {
							continue;
						}
						pi.sendMessage(chat.replaceAll("%message%", msg)); 
					}
					
					for (Player pi : Bukkit.getOnlinePlayers()) {
						if (pi.hasPermission("mcfchat.bypass")) {
							pi.sendMessage(chat.replaceAll("%message%", msg));
						}
					}
					
					System.out.println(chat.replaceAll("%message%", msg));
				} else {
					p.sendMessage(FConfig.fc.getString("lang.not-exists").replaceAll("%group%", g).replaceAll("&", "§"));
				}
			}
		}
	}

}
