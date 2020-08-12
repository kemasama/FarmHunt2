package farmhunt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.nums.GameState;
import farmhunt.util.DisguiseHelper;
import farmhunt.util.ScoreHelper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;

public class GameTask extends BukkitRunnable {

	private GameState state = GameState.STOP;
	private int lobbyTime = 60;

	private BossBar<?> bar;

	public GameTask() {
		try {
			if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
				bar = Via.getAPI().createBossBar("§eFarm Hunt", BossColor.YELLOW, BossStyle.SOLID);
				bar.show();
			}else {
				System.out.println("Viaversion is not Enabled!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLobbyTime(int time) {
		this.lobbyTime = time;
	}
	public void updateBossBar() {
		if (bar == null) {
			if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
				bar = Via.getAPI().createBossBar("§eFarm Hunt", BossColor.YELLOW, BossStyle.SOLID);
				bar.show();
			}
			return;
		}

		try {
			if (state.equals(GameState.PLAYING)) {

				int time = GameInfo.GAME_TIME - Game.getInstance().info.getTime();
				int min = time / 60;
				int sec = time - min * 60;
				String timeFormat = String.format("%02d:%02d", min, sec);

				float per = ( (float) time ) / ( (float) GameInfo.GAME_TIME );
				bar.setHealth(per);

				bar.setTitle("§eTime: §c" + timeFormat);
			}else if (state.equals(GameState.ENDING)) {
				bar.setTitle("§cGameOver");
			}

			bar.show();

			for (Player p : Bukkit.getOnlinePlayers()) {
				if (!bar.getPlayers().contains(p.getUniqueId())) {
					bar.addPlayer(p.getUniqueId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {

			if (Game.getInstance().info.isInGame()) {
				if (state.equals(GameState.STOP)) {
					state = GameState.PLAYING;
				}

				Game.getInstance().info.tick();

				if (Game.getInstance().info.End()) {
					state = GameState.ENDING;

					(new BukkitRunnable() {
						@Override
						public void run() {
							for (Player p : Bukkit.getOnlinePlayers()) {
								Game.sendToLobby(p);
							}
							(new BukkitRunnable() {
								@Override
								public void run() {
									state = GameState.STOP;
								}
							}).runTaskLater(Game.getInstance(), 20L * 5);
						}
					}).runTaskLater(Game.getInstance(), 20L * 5); //5s later

				}
			}else {
				lobbyTime--;
				if (state.equals(GameState.STOP) && lobbyTime == 0) {
					Game.getInstance().info.Start();
					lobbyTime = 60;
				}
			}

			updateBossBar();

			int time = GameInfo.GAME_TIME - Game.getInstance().info.getTime();
			int min = time / 60;
			int sec = time - min * 60;
			String timeFormat = String.format("%02d:%02d", min, sec);

			int seeker = 0, hider = 0;
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (DisguiseHelper.isDisguise(pl)) {
					hider++;
				}else {
					seeker++;
				}
			}


			for (Player p : Bukkit.getOnlinePlayers()) {
				ScoreHelper helper;
				if (ScoreHelper.hasScore(p)) {
					helper = ScoreHelper.getByPlayer(p);
				}else {
					helper = ScoreHelper.createScore(p);
				}

				helper.setTitle("§eFarm Hunt");

				if (state.equals(GameState.PLAYING)) {
					helper.setSlot(10, "§fTime: §a" + timeFormat);
					helper.setSlot(9, "§cYou");
					if (DisguiseHelper.isDisguise(p)) {
						helper.setSlot(8, "§e" + DisguiseHelper.getSlot(p));
					}else {
						helper.setSlot(8, "§ePlayer");
					}
					helper.setSlot(7, "");
					helper.setSlot(6, "§eAnimals");
					helper.setSlot(5, " §e" + hider);
					helper.setSlot(4, "§eSeeker");
					helper.setSlot(3, " §e" + seeker);
				}else {
					helper.setSlot(7, "Waiting");
					helper.setSlot(6, "Time: §e" + lobbyTime);
					helper.setSlot(5, "");
					helper.setSlot(4, "Level: §e" + LevelPoint.getStore(p.getUniqueId()).getLevel());
					helper.setSlot(3, "Exp: §e" + LevelPoint.getStore(p.getUniqueId()).getEXP() + "/" + LevelPoint.getStore(p.getUniqueId()).getNeedEXP());
				}

				helper.setSlot(2, "");
				helper.setSlot(1, "§emc.devras.info");

				for (Player pl : Bukkit.getOnlinePlayers()) {
					helper.setHealth(pl, (int) pl.getHealth());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
