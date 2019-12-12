package farmhunt.util;

import java.util.UUID;

import org.bukkit.entity.Player;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;

public class BossBarHelper {
	private static BossBar<?> bar = null;
	public static void createBossBar(String title) {
		bar = Via.getAPI().createBossBar(title, BossColor.YELLOW, BossStyle.SOLID);
		bar.hide();
	}

	public static BossBar<?> getBossBar() {
		return bar;
	}

	public static void removeBossBar() {
		bar.hide();
		for (Object pl : bar.getPlayers()) {
			try {
				if (pl instanceof UUID) {
					bar.removePlayer((UUID) pl);
				}else {
					if (pl instanceof Player) {
						bar.removePlayer(((Player) pl).getUniqueId());
					}
				}
			} catch (Exception e) {}
		}

		bar = null;
	}
}
