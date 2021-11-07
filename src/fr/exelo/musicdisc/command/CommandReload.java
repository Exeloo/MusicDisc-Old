package fr.exelo.musicdisc.command;

import org.bukkit.entity.Player;

import fr.exelo.musicdisc.Main;

public class CommandReload {
	
	private Main main;
	
	public CommandReload(Main main) {
		this.main = main;
	}

	public boolean getMusicReload(Player player) {
		if (player.hasPermission("music.reload")) {
			player.sendMessage("§6[§eMusicDisc§6] §7" + main.pluginName + " " + main.v + " is reloaded");
			if (main.musicConfig.reloadMusic()) player.sendMessage("§6[§eMusicDisc§6] §7" + main.pluginName + " was succesfully reloaded");
			else player.sendMessage("§6[§eMusicDisc§6] §7 an error occurred while reloading");
		}
		return false;
	}
}
