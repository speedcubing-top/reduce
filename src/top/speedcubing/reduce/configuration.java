package top.speedcubing.reduce;

import org.bukkit.command.*;
import top.speedcubing.server.libs.GlobalString;

import java.util.ArrayList;
import java.util.List;

public class configuration implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(GlobalString.UnknownCommand[commandSender instanceof ConsoleCommandSender ? 1 : Member.getMember(commandSender).user.lang]);
        reload();
        return true;
    }

    public void reload() {
        Main.getPlugin(Main.class).reloadConfig();
        configuration.ReduceKB = new double[]{
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.hor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.ver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.airhor"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.airver"),
                Main.getPlugin(Main.class).getConfig().getDouble("kb.Reduce.reach")};
    }

    public static double[] ReduceKB;
}
