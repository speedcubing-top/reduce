package top.speedcubing.reduce.All;

import org.bukkit.command.*;
import top.speedcubing.reduce.Main;
import top.speedcubing.reduce.Member;
import top.speedcubing.server.libs.GlobalString;

import java.util.ArrayList;
import java.util.List;

public class configcache implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(GlobalString.UnknownCommand[commandSender instanceof ConsoleCommandSender ? 1 : Member.getMember(commandSender).user.lang]);
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
