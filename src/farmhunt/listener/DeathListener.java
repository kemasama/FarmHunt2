package farmhunt.listener;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.Game;
import farmhunt.util.ArmorHelper;
import farmhunt.util.DisguiseHelper;

public class DeathListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (DisguiseHelper.isDisguise(p)) {
				p.getWorld().playEffect(p.getLocation(), Effect.FOOTSTEP, Material.REDSTONE_BLOCK);
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
		if (DisguiseHelper.isDisguise(p)) {
			DisguiseHelper.unDisguise(p);
		}

		p.getWorld().playSound(p.getLocation(), Sound.VILLAGER_YES, 1f, 1f);

		(new BukkitRunnable() {
			@Override
			public void run() {
				p.spigot().respawn();
				p.teleport(Game.spawnLocation);
				p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 1f);
				ArmorHelper.equipToHunter(p);
			}
		}).runTaskLater(Game.getInstance(), 5L);
	}
}
