package fr.exelo.musicdisc;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MusicListerner implements Listener {
	
	private Main main;
	
	public MusicListerner(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) throws InterruptedException {
		
		
		ItemStack item = event.getItem();
		if(item == null) return;
		if(item.getType() != Material.NETHER_STAR) return;
		
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		
		
		if(action == Action.RIGHT_CLICK_BLOCK && block.getType().equals(Material.JUKEBOX) && item.getType().equals(Material.NETHER_STAR) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals("§5§LClef de Jukebox")) {
			main.cmdMusic.getMusic(event.getPlayer());
		}
		return;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		ItemStack current = event.getCurrentItem();
		if(current == null) return;
		
		Player player = (Player) event.getWhoClicked();
		Inventory currentinv = event.getClickedInventory();
		ItemStack barrier = main.getItem(Material.BARRIER, "§4Stop");
		
		if (currentinv.contains(barrier)) {
			event.setCancelled(true);
			boolean multi = main.multi.get(player);
			boolean permMulti = false;
			if (player.hasPermission("music.music.others"))
				permMulti = true;
			if (current.equals(barrier)) {
				main.playOrDisableMusic(main.playerInZone(player), player, false, " ", multi);
				return; 
				}
			
			else if (current.getItemMeta().getDisplayName().equals("§4Next page")) {
				player.openInventory(main.getDefaultInventory("§5 La sainte jukebox §o§8 page ", permMulti, current.getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL), player));
			}
			
			else if (current.getItemMeta().getDisplayName().equals("§4Previous page")) {
				player.openInventory(main.getDefaultInventory("§5 La sainte jukebox §o§8 page ", permMulti, current.getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL), player));
			}
			
			else if (current.getType().equals(Material.CHEST)){
				player.openInventory(main.getMenuInventory(current.getItemMeta().getDisplayName(), permMulti, current.getItemMeta().getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL), player));
			}
			else if (current.getType().equals(Material.GREEN_CONCRETE)) {
				main.multi.put(player, false);
				currentinv.setItem(50, main.getItem(Material.RED_CONCRETE, "§4Multi off"));
			}
			else if (current.getType().equals(Material.RED_CONCRETE)) {
				main.multi.put(player, true);
				currentinv.setItem(50, main.getItem(Material.GREEN_CONCRETE, "§2Multi on"));
			}
			else if (currentinv.contains(Material.CHEST)){
				
				if ( current.getType().equals(Material.ARROW) && current.getItemMeta().getDisplayName() != "§4 Next page" && current.getItemMeta().getDisplayName() != "§4 Previous page") player.closeInventory();
				
				for(String name : main.musicConfig.getMusic().getStringList("music.others_music.music_names")) {
					if (current.getItemMeta().getDisplayName().equals("§5" + name)) {
						main.playOrDisableMusic(main.playerInZone(player), player, true, main.musicConfig.getMusic().getStringList("music.others_music.music_Json").get(main.musicConfig.getMusic().getStringList("music.others_music.music_names").indexOf(name)), multi);
					}
				}
				return;
			}
			else {
				if ( current.getType().equals(Material.ARROW)) {
						player.openInventory(main.getDefaultInventory("§5 La sainte jukebox §o§8 page ", permMulti, 1, player)); 
				}
				
				for(String nameMenu : main.musicConfig.getMusic().getConfigurationSection("music.menus").getKeys(false)) {
					for(String name : main.musicConfig.getMusic().getConfigurationSection("music.menus." + nameMenu).getStringList(".music_names")) {
						if (current.getItemMeta().getDisplayName().equals("§5" + name)) {
							main.playOrDisableMusic(main.playerInZone(player), player, true, main.musicConfig.getMusic().getConfigurationSection("music.menus." + nameMenu).getStringList(".music_Json").get(main.musicConfig.getMusic().getConfigurationSection("music.menus." + nameMenu).getStringList(".music_names").indexOf(name)), multi);
							return;
						}
					}
					
				}
			}
			return;
		}
		return;
	}
	
}