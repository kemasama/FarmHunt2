package farmhunt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import farmhunt.util.DisguiseHelper;

public class CommonListener implements Listener {
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
				event.setDamage(0.0);
			}
		}
	}
}
