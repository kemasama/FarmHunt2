package farmhunt.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import farmhunt.Game;
import farmhunt.LevelPoint;
import farmhunt.util.ArmorHelper;
import farmhunt.util.ScoreHelper;

public class JoinListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		p.setExp(0);
		p.setLevel(0);
		p.resetMaxHealth();
		p.setHealth(20.0);
		p.setFoodLevel(20);

		p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getEnderChest().clear();

		if (Game.getInstance().info.isInGame()) {
			ArmorHelper.equipToHunter(p);
			p.teleport(Game.spawnLocation);
			event.setJoinMessage("§e" + p.getName() + " §econnect the game!");
			p.setPlayerListName("§6[H] §f" + p.getName());
			p.sendMessage("§c途中参加のため、ハンターになりました！");
		}else {
			event.setJoinMessage("§e" + p.getName() + " §ejoined the game (" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")");
		}

		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}

		LevelPoint.getStore(p.getUniqueId()).addEXP(0);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		p.spigot().respawn();

		event.setQuitMessage("");
		if (Game.getInstance().info.isInGame()) {
			event.setQuitMessage("§e" + p.getName() + " §edisconnect!");
		}else {
			event.setQuitMessage("§e" + p.getName() + " §eleft the game (" + (Bukkit.getOnlinePlayers().size() - 1) + "/" + Bukkit.getMaxPlayers() + ")");
		}

		if (ScoreHelper.hasScore(p)) {
			ScoreHelper.removeScore(p);
		}

		LevelPoint.clean(p.getUniqueId());

	}
}
