package farmhunt.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArmorHelper {
	public static final String STICK = "§eDisguise";
	public static void equipToHunter(Player p) {
		p.getInventory().clear();
		p.getInventory().setHeldItemSlot(0);

		p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
		{
			ItemStack bow = new ItemStack(Material.BOW);
			bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			p.getInventory().setItem(1, bow);
		}

		p.getInventory().setItem(8, new ItemStack(Material.ARROW));

		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 0));
	}

	public static void equipToHider(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getInventory().setHeldItemSlot(0);
		{
			ItemStack stick = new ItemStack(Material.BLAZE_ROD);
			ItemMeta meta = stick.getItemMeta();
			meta.setDisplayName(STICK);
			stick.setItemMeta(meta);
			p.getInventory().setItem(0, stick);
		}

		p.getInventory().setItem(1, new ItemStack(Material.BOW));
		p.getInventory().setItem(8, new ItemStack(Material.ARROW, 5));
	}
}
