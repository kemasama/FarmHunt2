package farmhunt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

public class LevelPoint {

	private static HashMap<UUID, LevelPoint> point = new HashMap<>();
	public static LevelPoint getStore(UUID key) {
		return point.containsKey(key) ? point.get(key) : new LevelPoint(key);
	}
	public static void clean() {
		for (UUID key : point.keySet()) {
			clean(key);
		}
	}

	public static void clean(UUID key) {
		if (point.containsKey(key)) {
			point.get(key).save();
			point.remove(key);
		}
	}

	private UUID own;

	private LevelPoint(UUID key) {
		this.own = key;

		this.level = 1;
		this.exp = 0;

		try {
			if (Game.MySQL.isConnected()) {
				ResultSet res = Game.MySQL.query("select * from levels where uuid='" + own.toString() + "' limit 1;", true);
				while (res != null && res.next()) {
					level = res.getInt("level");
					exp = res.getInt("exp");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.need = getNeedEXP(level);

		point.put(key, this);
	}

	public void save() {
		try {
			if (Game.MySQL.isConnected()) {
				Connection con = Game.MySQL.getConnection();
				PreparedStatement stm = con.prepareStatement("insert into levels(uuid, level, exp) values(?, ?, ?) on duplicate key update level=values(level), exp=values(exp);");
				stm.setString(1, own.toString());
				stm.setInt(2, level);
				stm.setInt(3, exp);
				stm.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int level = 1;
	private int exp = 0;
	private int need = 0;

	public int getLevel() {
		return level;
	}

	public int getEXP() {
		return exp;
	}
	public int getNeedEXP() {
		return need;
	}

	public void addEXP(int amount) {
		exp += amount;
		if (exp >= need) {
			exp = exp - need;
			level++;
		}
	}

	private int getNeedEXP(int level) {
		return (level * 17);
	}

	public UUID getOwnKey() {
		return own;
	}
}
