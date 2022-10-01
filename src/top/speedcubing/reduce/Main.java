package top.speedcubing.reduce;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.lib.bukkit.TabCompleteUtils;
import top.speedcubing.lib.eventbus.LibEventManager;
import top.speedcubing.reduce.BukkitEventListener.*;
import top.speedcubing.reduce.Core.BlockEditor;
import top.speedcubing.reduce.Core.Reduce;
import top.speedcubing.reduce.List.ItemStackList;

public class Main extends JavaPlugin {
    public static World world;

    public void onEnable() {
        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "reduce");
        new configuration().reload();
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerItemHeld(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClose(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginCommand("reloadconfig").setExecutor(new configuration());
        TabCompleteUtils.registerEmptyTabComplete("reloadconfig");
        world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setTime(6000);
        new ItemStackList().Load();
        new Reduce().Load();
        new BlockEditor().Load();
        LibEventManager.registerListeners(new ServerEvent());
    }
}

