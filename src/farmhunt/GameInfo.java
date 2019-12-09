package farmhunt;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.util.ArmorHelper;
import farmhunt.util.DisguiseHelper;

public class GameInfo {
	public static final int GAME_TIME = 60 * 5;
	private boolean inGame;
	public boolean isInGame() {
		return inGame;
	}

	// making a check point
	private int time;
	public int getTime() {
		return time;
	}

	public void tick() {
		time++;
	}


	public void Start() {
		if (inGame) {
			return;
		}

		if (!(Bukkit.getOnlinePlayers().size() > 2)) {
			//Game.getInstance().getLogger().info("Failed to start Game: Not enough players!");
			return;
		}

		Player hunter = choosePlayer();
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!pl.getUniqueId().equals(hunter.getUniqueId())) {
				DisguiseHelper.setDisguise(pl, EntityType.COW);
				ArmorHelper.equipToHider(pl);
				pl.teleport(Game.spawnLocation);
			}else {
				ArmorHelper.equipToHunter(pl);

				pl.sendMessage("§eあなたはハンターです！");
				final Player player = pl.getPlayer();
				(new BukkitRunnable() {
					@Override
					public void run() {
						player.teleport(Game.spawnLocation);
					}
				}).runTaskLater(Game.getInstance(), 20L * 10);
			}
		}

		inGame = true;

	}

	public boolean End() {
		if (!inGame) {
			return false;
		}

		boolean alive = false;
		boolean setup = false;

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (DisguiseHelper.isDisguise(pl)) {
				alive = true;
			}
		}

		if (!alive) {
			// GameOver...

			StringBuilder players = new StringBuilder();

			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (!DisguiseHelper.isDisguise(pl)) {
					players.append(pl.getName());
					players.append(", §e");
				}
			}

			Bukkit.broadcastMessage("§7----------------------------------");
			Bukkit.broadcastMessage("§e    §e§lWIN §c§lPlayer");
			Bukkit.broadcastMessage("§7");
			Bukkit.broadcastMessage("§e  " + players.toString() + " WIN!");
			Bukkit.broadcastMessage("§7----------------------------------");

			setup = true;
		}else {
			if (time > GAME_TIME) {
				StringBuilder players = new StringBuilder();

				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (DisguiseHelper.isDisguise(pl)) {
						players.append(pl.getName());
						players.append(", §e");
					}
				}

				Bukkit.broadcastMessage("§7----------------------------------");
				Bukkit.broadcastMessage("§e    §e§lWIN §c§lHider");
				Bukkit.broadcastMessage("§7");
				Bukkit.broadcastMessage("§e  " + players.toString() + " WIN!");
				Bukkit.broadcastMessage("§7----------------------------------");

				setup = true;
			}
		}

		if (setup) {
			time = 0;
			inGame = false;
		}

		return setup;
	}

	public Player choosePlayer() {
		Player p = null;
		Random r = new Random();
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (p == null) {
				p = pl;
			}

			if (r.nextBoolean()) {
				p = pl;
			}

		}

		return p;
	}
}
