package speedcubing.All;

import cubingserver.StringList.GlobalString;
import cubingserver.libs.PlayerData;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import speedcubing.Main;

import java.util.ArrayList;
import java.util.List;

public class configcache implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(GlobalString.UnknownCommand[commandSender instanceof ConsoleCommandSender ? 1 : PlayerData.getLang(((Player) commandSender).getUniqueId())]);
        reload();
        return true;
    }

    public void reload() {
        Main.getPlugin(Main.class).reloadConfig();
        configcache.ReduceKB = new double[]{
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.airhor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.airver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.reach")};
    }

    public static double[] ReduceKB;

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
