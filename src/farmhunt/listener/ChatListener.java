package farmhunt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import farmhunt.Game;
import farmhunt.LevelPoint;
import farmhunt.util.DisguiseHelper;
import trans.jp.IMEConverter;
import trans.jp.KanaConverter;

public class ChatListener implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();

		String msg = event.getMessage();
		String kana = KanaConverter.conv(msg);
		String ime = IMEConverter.convByGoogleIME(kana);

		String str = ime + " §8(§f" + msg + "§8)";

		event.setMessage(str);

		if (ime.isEmpty()) {
			event.setCancelled(true);
			return;
		}

		if (!Game.getInstance().info.isInGame()) {
			event.setFormat("§e[" + LevelPoint.getStore(p.getUniqueId()).getLevel() + "] §7%s§8: §f%s");
			return;
		}

		if (DisguiseHelper.isDisguise(p)) {
			event.setFormat("§6[A] §f%s§7: %s");
		}else {
			event.setFormat("§6[H] §f%s§7: %s");
		}

	}
}
