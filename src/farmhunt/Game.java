package farmhunt;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import farmhunt.commands.FarmHuntCommand;
import farmhunt.listener.BuildListener;
import farmhunt.listener.CommonListener;
import farmhunt.listener.DeathListener;
import farmhunt.listener.JoinListener;
import farmhunt.listener.UseStickListener;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;

public class Game extends JavaPlugin {

	public static Game getInstance() {
		return Game.getPlugin(Game.class);
	}

	public GameTask task;
	public GameInfo info;
	public static ArrayList<DisguiseType> AllowType = new ArrayList<>();

	public static Location spawnLocation;

	static {
		AllowType.add(DisguiseType.CHICKEN);
		AllowType.add(DisguiseType.COW);
		AllowType.add(DisguiseType.OCELOT);
		AllowType.add(DisguiseType.PIG);
		AllowType.add(DisguiseType.SHEEP);
		AllowType.add(DisguiseType.WOLF);
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {

		/**
		 * Initializing
		 */

		saveDefaultConfig();
		FileConfiguration config = getConfig();
		config.options().copyDefaults();

		{
			Object obj = config.get("game.spawn");
			if (obj instanceof Location) {
				spawnLocation = (Location) obj;
			}else {
				spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
			}
		}

		Bukkit.getPluginCommand("farmhunt").setExecutor(new FarmHuntCommand());


		task = new GameTask();
		task.runTaskTimer(Game.getInstance(), 0L, 20L);

		info = new GameInfo();

		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new BuildListener(), this);

		Bukkit.getPluginManager().registerEvents(new CommonListener(), this);

		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);

		// Disguise
		Bukkit.getPluginManager().registerEvents(new UseStickListener(), this);

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		super.onEnable();
	}

	public static void sendToLobby(Player p) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("lobby");
			p.sendPluginMessage(Game.getInstance(), "BungeeCord", out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
