package farmhunt.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.Game;
import farmhunt.util.ArmorHelper;
import farmhunt.util.DisguiseHelper;
import me.libraryaddict.disguise.DisguiseAPI;

public class DeathListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (!Game.getInstance().info.isInGame()) {
				event.setCancelled(true);
			}
		}else {
			if (!DisguiseAPI.isDisguised(event.getEntity())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!Game.getInstance().info.isInGame()) {
			event.setCancelled(true);
			return;
		}

		if (event.getDamager() instanceof Projectile) {
			Projectile pro = (Projectile) event.getDamager();
			if (pro.getShooter() instanceof Player && event.getEntity() instanceof Player) {
				Player e = (Player) event.getEntity();
				Player p = (Player) pro.getShooter();
				if (DisguiseAPI.isDisguised(e)) {
					// damaged entity is animals?
					// if it so, check damager is hunter!
					if (DisguiseHelper.isDisguise(p)) {
						event.setCancelled(true);
					}
				}else {
					if (!DisguiseHelper.isDisguise(p)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		event.setDroppedExp(0);
		event.getDrops().clear();

		if ((event.getEntity() instanceof Player)) {
			return;
		}

		final LivingEntity e = event.getEntity();

		e.getWorld().playSound(e.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

		(new BukkitRunnable() {
			@Override
			public void run() {
				e.getWorld().spawnEntity(e.getLocation(), e.getType());
			}
		}).runTaskLater(Game.getInstance(), 20L * 60);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		final Player p = event.getEntity();

		p.getWorld().playSound(p.getLocation(), Sound.VILLAGER_YES, 1f, 1f);

		if (p.getKiller() != null) {
			Game.addExp(p.getKiller(), 10 * Game.boostPer);
		}

		if (DisguiseHelper.isDisguise(p)) {
			DisguiseHelper.unDisguise(p);
			(new BukkitRunnable() {
				@Override
				public void run() {
					p.spigot().respawn();
					p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
					p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 1f);
					ArmorHelper.equipToHunter(p);
					p.setPlayerListName("§6[H] §f" + p.getName());
				}
			}).runTaskLater(Game.getInstance(), 5L);

			(new BukkitRunnable() {
				@Override
				public void run() {
					p.spigot().respawn();
					p.teleport(Game.spawnLocation);
					p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 1f);
				}
			}).runTaskLater(Game.getInstance(), 20L * 10);
		}else {


			(new BukkitRunnable() {
				@Override
				public void run() {
					p.spigot().respawn();
					p.teleport(Game.spawnLocation);
					p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 1f);
					ArmorHelper.equipToHunter(p);
				}
			}).runTaskLater(Game.getInstance(), 10L);
		}
	}
}
