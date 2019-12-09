package farmhunt.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmorHelper {
	public static final String STICK = "§eDisguise";
	public static void equipToHunter(Player p) {
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
		p.getInventory().setHeldItemSlot(0);
	}

	public static void equipToHider(Player p) {
		p.getInventory().clear();
		p.getInventory().setHeldItemSlot(0);
		{
			ItemStack stick = new ItemStack(Material.BLAZE_ROD);
			ItemMeta meta = stick.getItemMeta();
			meta.setDisplayName(STICK);
			stick.setItemMeta(meta);
			p.getInventory().setItem(0, stick);
		}
	}
}
