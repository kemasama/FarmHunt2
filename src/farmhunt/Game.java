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
import farmhunt.listener.BoosterListener;
import farmhunt.listener.BuildListener;
import farmhunt.listener.ChatListener;
import farmhunt.listener.CommonListener;
import farmhunt.listener.DeathListener;
import farmhunt.listener.JoinListener;
import farmhunt.listener.UseStickListener;
import farmhunt.util.CMySQL;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;

public class Game extends JavaPlugin {

	public static Game getInstance() {
		return Game.getPlugin(Game.class);
	}

	public GameTask task;
	public GameInfo info;
	public static ArrayList<DisguiseType> AllowType = new ArrayList<>();
	public static int boostPer = 1;

	public static Location spawnLocation;
	public static CMySQL MySQL;

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

		try {
			AllowType.clear();
			info = null;
			task = null;
			spawnLocation = null;

			if (MySQL != null) {
				MySQL.disConnect();
			}

			MySQL = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

		Bukkit.getPluginManager().registerEvents(new CommonListener(), this);

		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);

		// Disguise
		Bukkit.getPluginManager().registerEvents(new UseStickListener(), this);
		Bukkit.getPluginManager().registerEvents(new EmeraldItem(), this);

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "NL");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "NL", new BoosterListener());

		setupDatabase();

		super.onEnable();
	}

	public void setupDatabase() {
		FileConfiguration config = getConfig();
		String user = config.getString("mysql.user", "root");
		String pass = config.getString("mysql.pass", "");
		String name = config.getString("mysql.name", "minecraft");
		String host = config.getString("mysql.host", "localhost");
		String port = config.getString("mysql.port", "3306");

		MySQL = new CMySQL(host, port, name, user, pass);

		MySQL.Connect();

		if (MySQL.isConnected()) {
			if (!MySQL.tableExists("levels")) {
				MySQL.createTable("levels", "uuid varchar(36) not null primary key, level int not null default 1, exp int not null default 0");
			}
		}else {
			getLogger().warning("You need setup mysql because probably use that in near the future!");
			Bukkit.getPluginManager().disablePlugin(this);
		}

	}

	public static void addExp(Player p, int amount) {

		LevelPoint.getStore(p.getUniqueId()).addEXP(amount);

		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(p.getName());
			out.writeUTF(p.getUniqueId().toString());
			out.writeUTF(String.valueOf(amount));
			p.sendPluginMessage(Game.getInstance(), "NL", out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
