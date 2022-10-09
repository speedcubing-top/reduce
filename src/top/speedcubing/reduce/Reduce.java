package top.speedcubing.reduce;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.bukkit.inventory.ItemUtils;
import top.speedcubing.lib.eventbus.LibEventManager;
import top.speedcubing.reduce.BukkitEventListener.*;
import top.speedcubing.reduce.Core.BlockEditor;
import top.speedcubing.reduce.List.ItemStackList;
import top.speedcubing.reduce.List.StringList;
import top.speedcubing.server.config;

import java.util.*;

public class Reduce extends JavaPlugin {
    public static World world;


    public static final int maptypes = 4;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}};

    public static ScoreboardTeam NoNameTag;

    public static final Set<Integer>[] Rooms = new HashSet[maptypes];
    public static final Map<Integer, Integer> mapCache = new HashMap<>();
    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final Inventory spec = Bukkit.createInventory(ItemStackList.SpecHolder, 54, "");
    public static final List<Integer>[] specStorage = new ArrayList[maptypes];
    public static final Map<Integer, List<String>>[] specName = new HashMap[maptypes];
    public static final List<String> totalName = new ArrayList<>();

    public static double[] ReduceKB;

    public void onEnable() {
        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "reduce");
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
        world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setTime(6000);
        new ItemStackList().Load();
        new BlockEditor().Load();
        NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");
        NoNameTag.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        for (int i = 0; i < 4; i++) {
            Invalid[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).name(new String[]{"§4Sie können die Elemente hier nicht einstellen!", "§4You can't set the items here!", "§4你不能將物品設定於此處!", "§4你不能将物品设定于此处!"}[i]).build();
        }
        for (int i = 0; i < maptypes; i++) {
            specStorage[i] = new ArrayList<>();
            specName[i] = new HashMap<>();
            Rooms[i] = new HashSet<>();
        }
        LibEventManager.registerListeners(new ServerEvent());
        config.event.call();
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 250) - (Math.abs(z / 500) + z / 500) / (z / 250);
    }

    //quit spec
    public static void quitSpec(Player player, Player connection, boolean backtoarena) {
        boolean n = player == null;
        if (n)
            player = connection.getPlayer().getPlayer();
        Member member = Member.getMember(player);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        member.target.user.player.showPlayer(player);
        if (!n) {
            member.target.Spectators.remove(connection);
            member.target.npc.removeListener(connection);
            member.target.Holo.removeListener(connection);
        }
        ((CraftPlayer) connection).getHandle().removeAllEffects();
        if (backtoarena) {
            member.coord = member.oldCoord;
            member.map = member.oldMap;
            player.teleport(new Location(top.speedcubing.reduce.Reduce.world, member.map * 500 - MapLocations[member.map][0], 100 + MapLocations[member.map][1], member.coord + 0.5, 270, 0));
            Integer[] ints = member.integerCache;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][member.user.lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]][member.user.lang]);
            inventory.setItem(ints[8], ItemStackList.Spec[member.user.lang]);
            inventory.setItem(ints[2], ItemStackList.settings[member.user.lang]);
            Bukkit.getScheduler().scheduleSyncDelayedTask(top.speedcubing.reduce.Reduce.getPlugin(top.speedcubing.reduce.Reduce.class), () -> {
                member.npc.spawn().hideFromTab(4000);
                member.Holo.spawn();
            }, 1);
            member.b();
        }
    }

    public static void sendPackets(PlayerConnection connection, Set<Player> connections, Packet<?>... packet) {
        for (Packet<?> p : packet) {
            connection.sendPacket(p);
        }
        connections.forEach(a -> {
            for (Packet<?> p : packet) {
                ((CraftPlayer) a).getHandle().playerConnection.sendPacket(p);
            }
        });
    }


    public static void updateSpecPage(int id) {
        boolean notnull = id != 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Member member = Member.getMember(p);
            if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder && !(notnull && member.user.id == id)) {
                Inventory orig = spec;
                Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[member.user.lang]);
                inv1.setContents(orig.getContents());
                if (p.getGameMode() == GameMode.ADVENTURE)
                    ItemUtils.glow(inv1.getItem(totalName.indexOf(member.target.user.player.getName())));
                Bukkit.getScheduler().runTask(top.speedcubing.reduce.Reduce.getPlugin(top.speedcubing.reduce.Reduce.class), () -> p.openInventory(inv1));
            }
        }
    }
}

