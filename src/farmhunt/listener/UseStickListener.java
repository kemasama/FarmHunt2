package farmhunt.listener;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import farmhunt.Game;
import farmhunt.util.ArmorHelper;
import farmhunt.util.DisguiseHelper;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;;

public class UseStickListener implements Listener {

	@EventHandler
	public void onUse(PlayerInteractEntityEvent event) {
		if (DisguiseHelper.isDisguise(event.getPlayer())) {
			Player p = event.getPlayer();
			if (!DisguiseHelper.isDisguise(p)) {
				return;
			}
			if (event.getRightClicked() instanceof LivingEntity) {
				if (!Game.AllowType.contains(DisguiseType.getType(event.getRightClicked().getType()))) {
					// cancel...
					return;
				}

				// get held item if it is equals Disguise stick...!
				ItemStack item = p.getInventory().getItemInHand();

				if (item != null
						&& item.hasItemMeta()
						&& item.getItemMeta().hasDisplayName()
						&& item.getItemMeta().getDisplayName().equals(ArmorHelper.STICK)) {
					DisguiseHelper.setDisguise(p, event.getRightClicked().getType());
					p.sendMessage("§e変身しました！");
					p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
					p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.FLAME, 1);
				}

			}
		}
	}
}
