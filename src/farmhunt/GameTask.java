package farmhunt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.nums.GameState;
import farmhunt.util.BossBarHelper;
import farmhunt.util.DisguiseHelper;
import farmhunt.util.ScoreHelper;

public class GameTask extends BukkitRunnable {

	private GameState state = GameState.STOP;

	@Override
	public void run() {
		try {

			if (Game.getInstance().info.isInGame()) {
				if (state.equals(GameState.STOP)) {
					state = GameState.PLAYING;
				}

				Game.getInstance().info.tick();

				if (Game.getInstance().info.End()) {
					BossBarHelper.removeBossBar();
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
				if (state.equals(GameState.STOP)) {
					Game.getInstance().info.Start();
				}
			}

			if (BossBarHelper.getBossBar() == null) {
				BossBarHelper.createBossBar("§eFarm Hunt");
			}

			int time = GameInfo.GAME_TIME - Game.getInstance().info.getTime();
			int min = time / 60;
			int sec = time - min * 60;
			String timeFormat = String.format("%02d:%02d", min, sec);

			try {
				float per = ( (float) time ) / ( (float) GameInfo.GAME_TIME );
				BossBarHelper.getBossBar().setHealth(per);
				if (Game.getInstance().info.isInGame()) {
					BossBarHelper.getBossBar().setTitle("§eTime: §c" + timeFormat);
				}
			} catch (Exception e) {}


			for (Player p : Bukkit.getOnlinePlayers()) {
				ScoreHelper helper;
				if (ScoreHelper.hasScore(p)) {
					helper = ScoreHelper.getByPlayer(p);
				}else {
					helper = ScoreHelper.createScore(p);
				}

				helper.setTitle("§eFarm Hunt");

				if (state.equals(GameState.PLAYING)) {
					helper.setSlot(6, "§fTime: §a" + timeFormat);
					helper.setSlot(5, "");
					helper.setSlot(4, "§cYou");
					if (DisguiseHelper.isDisguise(p)) {
						helper.setSlot(3, "§e" + DisguiseHelper.getSlot(p));
					}else {
						helper.setSlot(3, "§ePlayer");
					}
				}else {
					helper.setSlot(3, "waiting");
				}

				helper.setSlot(2, "");
				helper.setSlot(1, "§emc.devras.info");


				try {
					if (state.equals(GameState.PLAYING)) {
						BossBarHelper.getBossBar().addPlayer(p.getUniqueId());
					}
				} catch (Exception e) {}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
