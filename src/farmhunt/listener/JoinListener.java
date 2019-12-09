package farmhunt.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import farmhunt.Game;
import farmhunt.util.ScoreHelper;

public class JoinListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		p.setExp(0);
		p.setLevel(0);
		p.setHealth(20.0);
		p.setFoodLevel(20);

		p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getEnderChest().clear();

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		p.spigot().respawn();

		event.setQuitMessage("");
		if (Game.getInstance().info.isInGame()) {
			event.setQuitMessage("§e" + p.getName() + " disconnect");
		}

		if (ScoreHelper.hasScore(p)) {
			ScoreHelper.removeScore(p);
		}

	}
}
