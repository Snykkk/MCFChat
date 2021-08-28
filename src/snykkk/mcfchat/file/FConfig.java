package snykkk.mcfchat.file;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import snykkk.mcfchat.MCFMain;

public class FConfig {

	public static File f;
	public static FileConfiguration fc;
	
	public FConfig() {
		f = new File(MCFMain.m.getDataFolder(), "config.yml");
		
		if(!f.exists()) {
			f.getParentFile().mkdirs();
			MCFMain.m.saveResource("config.yml", false);
		}
		
		fc = new YamlConfiguration();
		
		try {
			fc.load(f);
		} catch (Exception ex) {}
	}
	
	public static void save() {
		File f = new File(MCFMain.m.getDataFolder(), "config.yml");
		try {fc.save(f);}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void reload() {
		save();
	}
	
}
