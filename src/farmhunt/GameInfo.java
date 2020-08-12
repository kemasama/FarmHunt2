package farmhunt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.util.ArmorHelper;
import farmhunt.util.DisguiseHelper;
import farmhunt.util.FireworkHelper;

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
	public void setTime(int time) {
		this.time = time;
	}

	public void tick() {
		time++;
	}


	public void Start() {
		if (inGame) {
			return;
		}

		if (!(Bukkit.getOnlinePlayers().size() > 1)) {
			//Game.getInstance().getLogger().info("Failed to start Game: Not enough players!");
			return;
		}

		Player hunter = choosePlayer();
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!pl.getUniqueId().equals(hunter.getUniqueId())) {
				DisguiseHelper.setDisguise(pl, EntityType.COW);
				ArmorHelper.equipToHider(pl);
				pl.teleport(Game.spawnLocation);
				pl.sendMessage("§e10秒後にハンターが出現します！それまでに隠れてください！");

				pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 4));

				pl.getInventory().setItem(4, EmeraldItem.emerald.clone());
				pl.setPlayerListName("§6[A] §f" + pl.getName());
			}else {
				ArmorHelper.equipToHunter(pl);

				pl.sendMessage("§eあなたはハンターです！");
				pl.setPlayerListName("§6[H] §f" + pl.getName());
				final Player player = pl.getPlayer();
				(new BukkitRunnable() {
					@Override
					public void run() {
						player.teleport(Game.spawnLocation);
						Bukkit.broadcastMessage("§eハンターが出現しました！");
						player.getWorld().playSound(player.getLocation(), Sound.ANVIL_USE, 5f, 1f);
						Game.addExp(pl, 3 * Game.boostPer);
					}
				}).runTaskLater(Game.getInstance(), 20L * 10);
			}
		}

		Game.spawnLocation.getWorld().playSound(Game.spawnLocation, Sound.ENDERDRAGON_GROWL, 1f, 1f);

		inGame = true;

	}

	@SuppressWarnings("deprecation")
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

					pl.sendTitle("§6§lVICTORY", "§ePlayers win!");

					Game.addExp(pl, 3 * Game.boostPer);

					FireworkHelper.SummonSimple(pl.getLocation());
				}else {
					pl.sendTitle("§c§lGameOver", "§ePlayers win!");
					DisguiseHelper.unDisguise(pl);
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
						pl.sendTitle("§6§lVICTORY", "§eHiders win!");
						Game.addExp(pl, 3 * Game.boostPer);

						DisguiseHelper.unDisguise(pl);
						FireworkHelper.SummonSimple(pl.getLocation());
					}else {
						pl.sendTitle("§c§lGameOver", "§eHiders win!");
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
		List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());

		Collections.shuffle(players);

		Player p = null;
		for (Player pl : players) {
			if (p == null) {
				p = pl;
			}

			// make a new random resources to use a origin seed
			Random r = new Random(System.nanoTime() + System.currentTimeMillis());

			if (r.nextBoolean()) {
				p = pl;
			}

		}

		return p;
	}
}
