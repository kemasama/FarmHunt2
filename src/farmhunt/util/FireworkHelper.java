package farmhunt.util;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import farmhunt.Game;

public class FireworkHelper {
	public static void SummonSimple(Location loc) {
		(new BukkitRunnable() {
			private int c = 0;
			@Override
			public void run() {
				c++;
				if (c > 10) {
					cancel();
				}

				Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
				FireworkMeta fwm = fw.getFireworkMeta();
				Random r = new Random(System.nanoTime());
				FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(Color.GREEN).withFade(Color.RED).with(Type.BALL).trail(r.nextBoolean()).build();
				fwm.addEffect(effect);
				fwm.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.AQUA).with(Type.STAR).trail(true).build());
				fwm.setPower(2);
				fw.setFireworkMeta(fwm);
			}
		}).runTaskTimer(Game.getInstance(), 0L, 5L);
	}
}
