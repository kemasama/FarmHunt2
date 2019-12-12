package farmhunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import farmhunt.util.IconMenu;
import farmhunt.util.IconMenu.Row;
import farmhunt.util.IconMenu.onClick;
import farmhunt.util.ParticleAPI;
import farmhunt.util.ParticleAPI.EnumParticle;
import farmhunt.util.ParticleAPI.Particle;

public class EmeraldItem implements Listener {

	public static ItemStack emerald;
	public static IconMenu menu;
	public static HashMap<UUID, Boolean> CoolDown = new HashMap<>();

	public EmeraldItem() {
		emerald = new ItemStack(Material.EMERALD);
		ItemMeta meta = emerald.getItemMeta();
		meta.setDisplayName("§aTaunt");
		meta.setLore(new ArrayList<>(Arrays.asList(new String[] {
				"Taunt available!",
		})));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		emerald.setItemMeta(meta);

		if (menu == null) {
			setupMenu();
		}
	}

	@EventHandler
	public void onAttack(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			if (item.getType().equals(emerald.getType()) && item.getItemMeta().getDisplayName().equals(emerald.getItemMeta().getDisplayName())) {
				menu.open(event.getPlayer());
			}
		}
	}

	public void setupMenu() {
		menu = new IconMenu("§aTaunt", 4, new onClick() {
			@Override
			public boolean click(Player p, IconMenu menu, Row row, int slot, ItemStack item) {

				if (row.getRow() == 1) {
					if (slot == 1) {
						if (checkCoolDown(p.getUniqueId())) {
							p.sendMessage("§cTaunt Cooldown");
							return true;
						}

						setupCoolDown(p.getUniqueId());

						try {
							Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
							FireworkMeta fwm = fw.getFireworkMeta();

							Random r = new Random();

							FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.GREEN).withFade(Color.RED).with(Type.BALL).trail(r.nextBoolean()).build();

							fwm.addEffect(effect);

							fwm.setPower(1);

							fw.setFireworkMeta(fwm);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					if (slot == 2) {
						if (checkCoolDown(p.getUniqueId())) {
							p.sendMessage("§cTaunt Cooldown");
							return true;
						}

						setupCoolDown(p.getUniqueId(), 30);

						try {
							Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
							FireworkMeta fwm = fw.getFireworkMeta();

							Random r = new Random();

							FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.GREEN).with(Type.STAR).trail(r.nextBoolean()).build();

							fwm.addEffect(effect);
							fwm.setPower(1);

							fw.setFireworkMeta(fwm);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					if (slot == 4) {
						if (checkCoolDown(p.getUniqueId())) {
							p.sendMessage("§cTaunt Cooldown");
							return true;
						}

						setupCoolDown(p.getUniqueId(), 5);

						Particle part = new ParticleAPI.Particle(EnumParticle.FLAME, p.getLocation(), 0, 0, 0, 1, 90);
						part.sendParticle();
						p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1, 10);

					}
					if (slot == 6) {

						if (checkCoolDown(p.getUniqueId())) {
							p.sendMessage("§cTaunt Cooldown");
							return true;
						}

						setupCoolDown(p.getUniqueId(), 10);

						Vector dir = p.getLocation().getDirection();

						final FallingBlock tnt = tntTos(p);

						tnt.setVelocity(dir);
						//tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 1, false, false);

						Bukkit.getScheduler().scheduleSyncDelayedTask(Game.getInstance(), new Runnable() {
							@Override
							public void run() {
								tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5, false, false);
								tnt.remove();
								if (tnt.getLocation().getBlock().getType().equals(Material.TNT)) {
									tnt.getLocation().getBlock().setType(Material.AIR);
								}
							}
						}, 20 * 3);

					}
					if (slot == 7) {
						if (checkCoolDown(p.getUniqueId())) {
							p.sendMessage("§cTaunt Cooldown");
							return true;
						}

						setupCoolDown(p.getUniqueId());

						try {
							Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
							FireworkMeta fwm = fw.getFireworkMeta();

							Random r = new Random();

							FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.GREEN).with(Type.CREEPER).trail(r.nextBoolean()).build();

							fwm.addEffect(effect);

							fwm.setPower(1);

							fw.setFireworkMeta(fwm);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
				if (row.getRow() == 2) {
					if (slot == 0 || slot == 8) {
						return false;
					}

					if (checkCoolDown(p.getUniqueId())) {
						p.sendMessage("§cTaunt Cooldown");
						return true;
					}

					setupCoolDown(p.getUniqueId(), 3);

					Sound sound = Sound.CAT_MEOW;

					switch (slot) {
					case 1:
						sound = Sound.CAT_MEOW;
						break;
					case 2:
						sound = Sound.WOLF_BARK;
						break;
					case 3:
						sound = Sound.ENDERDRAGON_GROWL;
						break;
					case 4:
						sound = Sound.PIG_DEATH;
						break;
					case 5:
						sound = Sound.ZOMBIE_HURT;
						break;
					case 6:
						sound = Sound.VILLAGER_YES;
						break;
					case 7:
						sound = Sound.CHEST_OPEN;
						break;
					}

					p.getWorld().playSound(p.getLocation(), sound, 1, 1);

				}

				return false;
			}
		});

		for (int row = 0; row < 4; row++) {
			for (int slot = 0; slot < 9; slot++) {
				menu.addButton(menu.getRow(row), slot, new ItemStack(Material.STAINED_GLASS_PANE), "", "");
			}
		}

		menu.addButton(menu.getRow(1), 1, new ItemStack(Material.FIREWORK), "Fire Work", "+10 point");
		menu.addButton(menu.getRow(1), 2, new ItemStack(Material.SKULL_ITEM), "Appearance (Disabled)", "+30 point");
		menu.addButton(menu.getRow(1), 4, new ItemStack(Material.BLAZE_ROD), "Flame", "+3 point");
		menu.addButton(menu.getRow(1), 6, new ItemStack(Material.TNT), "TNT Tos", "+10 point");
		menu.addButton(menu.getRow(1), 7, new ItemStack(Material.DRAGON_EGG), "Creeper", "+10 point");


		menu.addButton(menu.getRow(2), 1, new ItemStack(Material.RECORD_10), "Cat", "+2 point");
		menu.addButton(menu.getRow(2), 2, new ItemStack(Material.RECORD_10), "Wolf", "+2 point");
		menu.addButton(menu.getRow(2), 3, new ItemStack(Material.RECORD_10), "EnderDragon", "+2 point");
		menu.addButton(menu.getRow(2), 4, new ItemStack(Material.RECORD_10), "Pig", "+2 point");
		menu.addButton(menu.getRow(2), 5, new ItemStack(Material.RECORD_10), "Zombie", "+2 point");
		menu.addButton(menu.getRow(2), 6, new ItemStack(Material.RECORD_10), "Villager", "+2 point");
		menu.addButton(menu.getRow(2), 7, new ItemStack(Material.RECORD_10), "Chest", "+2 point");
	}

	public boolean checkCoolDown(UUID key) {
		return CoolDown.containsKey(key);
	}

	public void setupCoolDown(final UUID key) {
		setupCoolDown(key, 10);
	}

	public void setupCoolDown(final UUID key, int time) {
		if (!checkCoolDown(key)) {
			CoolDown.put(key, true);
			if (Bukkit.getPlayer(key) != null) {
				Bukkit.getPlayer(key).sendMessage("§aSuccess Taunt");
			}

			Bukkit.getScheduler().runTaskLater(Game.getInstance(), new Runnable() {
				@Override
				public void run() {
					CoolDown.remove(key);
					if (Bukkit.getPlayer(key) != null) {
						Bukkit.getPlayer(key).sendMessage("§aTaunts Available!");
					}
				}
			}, 20L * time);
		}
	}

	@SuppressWarnings("deprecation")
	public FallingBlock tntTos(Player p) {
		return (FallingBlock) p.getWorld().spawnFallingBlock(p.getLocation(), Material.TNT, (byte) 0);
	}
}
