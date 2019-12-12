package farmhunt.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SrvUtil {
	public static String getServerVersion ( )
	{
	    return Bukkit.getServer ( ).getClass ( ).getPackage ( ).getName ( ).substring ( 23 );
	}

	public static void setPing(Player p, int newPing) {
		try{
			Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object CraftPlayer = CraftPlayerClass.cast(p);
			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);
			Field ping = EntityPlayer.getClass().getDeclaredField("ping");
			//ping.setAccessible(true);
			ping.setInt(EntityPlayer, newPing);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static int getPing(Player p){
		if (!p.getClass().getName().equals("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer")) {
			p = Bukkit.getPlayer(p.getUniqueId());
		}
		try{
			Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object CraftPlayer = CraftPlayerClass.cast(p);
			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);
			Field ping = EntityPlayer.getClass().getDeclaredField("ping");
			return ping.getInt(EntityPlayer);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public static Object getCraftPlayer(Player p){
		if (!p.getClass().getName().equals("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer")) {
			return null;
		}

		try{
			Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
			Object CraftPlayer = CraftPlayerClass.cast(p);
			//Class<?> mcEPClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");

			return CraftPlayer;

			//EntityPlayer ep = ((CraftPlayer) p).getHandle();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String pingFormat(int ping) {
		return ( (ping < 50) ? ChatColor.GREEN : (ping < 120) ? ChatColor.YELLOW : ChatColor.RED).toString()
				+ ( (ping < 50) ? "*" : "") + ping;
	}

	public static String getHeartIcon(int health, int maxHealth) {
		String str = "";
		if (maxHealth == health) {
		for (int i = 0; i < 10; i++) {
		str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		}
		return str;
		}
		if (health >= 0.9D * maxHealth && health <= maxHealth) {
		  for (int i = 0; i < 9; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  return String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		}

		if (health >= 0.8D * maxHealth && health <= 0.9D * maxHealth) {
		  for (int i = 0; i < 8; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 2; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.7D * maxHealth && health <= 0.8D * maxHealth) {
		  for (int i = 0; i < 7; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 3; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.6D * maxHealth && health <= 0.7D * maxHealth) {
		  for (int i = 0; i < 6; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 4; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.5D * maxHealth && health <= 0.6D * maxHealth) {
		  for (int i = 0; i < 5; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 5; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.4D * maxHealth && health <= 0.5D * maxHealth) {
		  for (int i = 0; i < 4; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 6; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.3D * maxHealth && health <= 0.4D * maxHealth) {
		  for (int i = 0; i < 3; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 7; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.2D * maxHealth && health <= 0.3D * maxHealth) {
		  for (int i = 0; i < 2; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  }
		  for (int i = 0; i < 8; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health >= 0.1D * maxHealth && health <= 0.2D * maxHealth) {
		  str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  for (int i = 0; i < 9; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health > 0 && health <= 0.1D * maxHealth) {
		  str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		  for (int i = 0; i < 9; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  return str;
		}
		if (health <= 0) {
		  for (int i = 0; i < 10; i++) {
		    str = String.valueOf(str) + ChatColor.GRAY.toString() + "♥";
		  }
		  for (int i = 0; i < 10; i++) {
		    str = String.valueOf(str) + ChatColor.DARK_RED.toString() + "♥";
		      }
		      return str;
		    }
		    if (health > maxHealth) {
		      health = maxHealth;
		    }
		    return str;
	}
}