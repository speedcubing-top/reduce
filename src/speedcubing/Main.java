package speedcubing;

import speedcubing.spigot.Event.ServerEventManager;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.java.JavaPlugin;
import speedcubing.All.configcache;
import speedcubing.BukkitEventListener.*;
import speedcubing.Core.BlockEditor;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;

public class Main extends JavaPlugin {

    public void onEnable() {
        new configcache().reload();
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
        Bukkit.getPluginCommand("reloadsubplugin").setExecutor(new configcache());
        Bukkit.getPluginCommand("reloadsubplugin").setTabCompleter(new configcache());
        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getWorld("world").setTime(6000);
        new ItemStackList().Load();
        new Reduce().Load();
        new BlockEditor().Load();
        ServerEventManager.registerListeners(new ServerEvent());
    }
}
