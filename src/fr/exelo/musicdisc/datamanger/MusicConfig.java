package fr.exelo.musicdisc.datamanger;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.exelo.musicdisc.Main;

public class MusicConfig {
	
	static Main main;
    public static YamlConfiguration music;
    public static File musicFile;
    
    static {
    	MusicConfig.music = null;
    	MusicConfig.musicFile = null;
    }
    
    public MusicConfig(final Main main) {
    	MusicConfig.main = main;
    }
    
    public boolean reloadMusic() {
        if (MusicConfig.musicFile == null) {
        	MusicConfig.musicFile = new File(Bukkit.getPluginManager().getPlugin("MusicDisc").getDataFolder(), "music.yml");
        }
        MusicConfig.music = YamlConfiguration.loadConfiguration(MusicConfig.musicFile);
        final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new File(Bukkit.getPluginManager().getPlugin("MusicDisc").getDataFolder(), "music.yml"));
        if (!MusicConfig.musicFile.exists() || MusicConfig.musicFile.length() == 0L) {
        	MusicConfig.music.setDefaults((Configuration)defConfig);
        }
        return true;
    }
    
    public FileConfiguration getMusic() {
        if (MusicConfig.music == null) {
            reloadMusic();
        }
        return (FileConfiguration)MusicConfig.music;
    }
    
    public void saveMusic() {
        if (MusicConfig.music == null || MusicConfig.musicFile == null) {
            return;
        }
        try {
            getMusic().save(MusicConfig.musicFile);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + MusicConfig.musicFile, ex);
        }
    }

}
