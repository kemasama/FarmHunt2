package farmhunt.util;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

public class DisguiseHelper {
	public static boolean isDisguise(Player p) {
		return DisguiseAPI.isDisguised(p);
	}

	public static void setDisguise(Player p, EntityType type) {
		MobDisguise dis = new MobDisguise(DisguiseType.getType(type));
		DisguiseAPI.disguiseToAll(p, dis);
	}

	public static void unDisguise(Player p) {
		DisguiseAPI.undisguiseToAll(p);
	}

	public static String getSlot(Player p) {

		if (!isDisguise(p)) {
			return "Player";
		}

		DisguiseType type = DisguiseAPI.getDisguise(p).getType();
		String str = null;

		switch(type) {
		case PIG:
			str = "Pig";
			break;
		case OCELOT:
			str = "Cat";
			break;
		case COW:
			str = "Cow";
			break;
		case SHEEP:
			str = "Sheep";
			break;
		case CHICKEN:
			str = "Chicken";
			break;
		case WOLF:
			str = "Wolf";
			break;
		default:
			str = "NONE";
			break;
		}

		return str;
	}

}
