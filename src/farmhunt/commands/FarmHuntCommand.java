package farmhunt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import farmhunt.Game;

public class FarmHuntCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("I'm sorry, you can not performe this command that's because this command can execute only players!");
			return true;
		}

		Player p = (Player) sender;

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("start")) {
				if (Game.getInstance().info.isInGame()) {
					p.sendMessage("§e既に始まっています！");
				}else {
					Game.getInstance().task.updateLobbyTime(5);
					p.sendMessage("§e残り5秒で始まります");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("spawn")) {
				Game.spawnLocation = p.getLocation();
				Game.getInstance().getConfig().set("game.spawn", Game.spawnLocation);
				Game.getInstance().saveConfig();

				p.sendMessage("§a設定を更新しました！");

				return true;
			}
		}

		p.sendMessage("§e---:-- [HELP] --:---");
		p.sendMessage("§e");
		p.sendMessage("§eAliases: [fh, farmhunt]");
		p.sendMessage("§e/farmhunt spawn - スポーン地点を変更します");
		p.sendMessage("§e  デフォルトはワールドⅠのスポーン地点");


		return true;
	}

}
