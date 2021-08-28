package snykkk.mcfchat;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import snykkk.mcfchat.file.FConfig;

public class MCFPlaceholder extends PlaceholderExpansion {

	private final String VERSION = getClass().getPackage().getImplementationVersion();

    /**
     * Defines the name of the expansion that is also used in the
     * placeholder itself.
     * 
     * @return {@code example} as String
     */
    @Override
    public String getIdentifier() {
        return "mcf";
    }

    /**
     * The author of the expansion.
     * 
     * @return {@code everyone} as String
     */
    @Override
    public String getAuthor() {
        return "Snykkk";
    }

    /**
     * Returns the version of the expansion as String.
     *
     * @return The VERSION String
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * Returns the name of the required plugin.
     *
     * @return {@code DeluxeTags} as String
     */
    @Override
    public String getRequiredPlugin() {
        return "MCFChat";
    }

    /**
     * Used to check if the expansion is able to register.
     * 
     * @return true or false depending on if the required plugin is installed
     */
    @Override
    public boolean canRegister() {
        if (!Bukkit.getPluginManager().isPluginEnabled(getRequiredPlugin())) { return false; }
        
        return true;
    }

    /**
     * This method is called when a placeholder is used and maches the set
     * {@link #getIdentifier() identifier}
     *
     * @param  offlinePlayer
     *         The player to parse placeholders for
     * @param  params
     *         The part after the identifier ({@code %identifier_params%})
     *
     * @return Possible-null String
     */
    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        if (params.equals("test")) { return "success"; }
        if (offlinePlayer == null || !offlinePlayer.isOnline()) { return "player is not online"; }

        Player p = offlinePlayer.getPlayer();
        switch (params) {
        	case "chatcolor":
        		for (String chat : FConfig.fc.getConfigurationSection("chat-color").getKeys(false)) {
        			if (p.hasPermission(FConfig.fc.getString("chat-color." + chat + ".permission"))) {
        				return FConfig.fc.getString("chat-color." + chat + ".color").replaceAll("&", "§");
        			}
        		}
        		return "";
 
            default:
                return null;
        }
    }
	
}
