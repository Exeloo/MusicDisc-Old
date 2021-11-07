package fr.exelo.musicdisc.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.exelo.musicdisc.Main;

public class CommandMusic {
	
	private Main main;
	
	public CommandMusic(Main main) {
		this.main = main;
	}

	public boolean getMusic(Player player) {
		if (player.hasPermission("music.music")) {
			if (player.hasPermission("music.music.others")) {
				Inventory inv = main.getDefaultInventory("§5 La sainte jukebox §o§8 page ", true, 1, player);
				if (inv == null) return false;
				player.openInventory(inv);
				return true;
			} else {
				main.multi.put(player, false);
				Inventory inv = main.getDefaultInventory("§5 La sainte jukebox §o§8 page ", false, 1, player);
				if (inv == null) return false;
				player.openInventory(inv);
				return true;
			}
		}
		return false;
	}
	public boolean getMusicGive(Player player) {
		if (player.hasPermission("music.give")) {
			player.getInventory().addItem(main.getItem(Material.NETHER_STAR, "§5§LClef de Jukebox"));
			return true; 
		} else {
			player.sendMessage("§eYou don't have the permmission to perform that command");
			
		}
		return false;
	}
}
		


 