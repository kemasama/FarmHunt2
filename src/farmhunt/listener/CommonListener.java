package farmhunt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import farmhunt.util.DisguiseHelper;

public class CommonListener implements Listener {

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onTake(PlayerArmorStandManipulateEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		if (!event.getInventory().getTitle().startsWith("§")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!event.getInventory().getTitle().startsWith("§")) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onChangeFood(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}

	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		if (event.getRegainReason().equals(RegainReason.SATIATED)) {
			event.setCancelled(true);
			// disable natural regain
		}
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player d = (Player) event.getDamager();
			Player e = (Player) event.getEntity();
			if (DisguiseHelper.isDisguise(d) && DisguiseHelper.isDisguise(e)) {
				event.setCancelled(true);
			} else if (!DisguiseHelper.isDisguise(d) && !DisguiseHelper.isDisguise(e)) {
					event.setCancelled(true);
			}
		}
	}
}
