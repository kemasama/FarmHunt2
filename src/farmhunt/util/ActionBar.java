package farmhunt.util;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

public class ActionBar {
	Object packet;
	public ActionBar(String text) {
		makePacket(text);
	}
	public void setText(String text) {
		makePacket(text);
	}

	private void makePacket(String text) {
		try {
			Class<?> ChatSerializerClass = Class.forName("net.minecraft.server." + SrvUtil.getServerVersion() + ".IChatBaseComponent$ChatSerializer");
			Class<?> PacketPlayOutChatClass = Class.forName("net.minecraft.server." + SrvUtil.getServerVersion() + ".PacketPlayOutChat");
			Class<?> ComponentClass = Class.forName("net.minecraft.server." + SrvUtil.getServerVersion() + ".IChatBaseComponent"); //net.minecraft.server.v1_8_R3.IChatBaseComponent

			Method aHandle = ChatSerializerClass.getDeclaredMethod("a", new Class[] {
					String.class
			});

			Object ChatSerializer = aHandle.invoke(null, new Object[] {
					"{\"text\":\"" + text + "\"}"
			});

			Constructor<?> newInstanceHandle = PacketPlayOutChatClass.getDeclaredConstructor(new Class[] {
					ComponentClass,
					byte.class
			});

			packet = newInstanceHandle.newInstance(new Object[] {
				ChatSerializer,
				(byte) 2
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToPlayer(Player player) {
		Object CraftPlayer = SrvUtil.getCraftPlayer(player);
		try {
			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);
			Field connectionField = EntityPlayer.getClass().getField("playerConnection");

			Object connectionObj = connectionField.get(EntityPlayer);

			Method sendPacket = connectionObj.getClass().getMethod("sendPacket", new Class[] {
					packet.getClass().getInterfaces()[0]
			});
			sendPacket.invoke(connectionObj, new Object[] {
					packet
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
