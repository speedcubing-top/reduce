package top.speedcubing.reduce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.lib.bukkit.inventory.ItemUtils;
import top.speedcubing.lib.eventbus.CubingEventManager;
import top.speedcubing.reduce.bukkitlistener.BlockBreak;
import top.speedcubing.reduce.bukkitlistener.BlockPlace;
import top.speedcubing.reduce.bukkitlistener.EntityDamage;
import top.speedcubing.reduce.bukkitlistener.InventoryClick;
import top.speedcubing.reduce.bukkitlistener.InventoryClose;
import top.speedcubing.reduce.bukkitlistener.PlayerDropItem;
import top.speedcubing.reduce.bukkitlistener.PlayerInteract;
import top.speedcubing.reduce.bukkitlistener.PlayerItemHeld;
import top.speedcubing.reduce.bukkitlistener.PlayerJoin;
import top.speedcubing.reduce.bukkitlistener.PlayerMove;
import top.speedcubing.reduce.bukkitlistener.PlayerQuit;
import top.speedcubing.reduce.bukkitlistener.PlayerToggleSneak;
import top.speedcubing.reduce.core.BlockEditor;
import top.speedcubing.reduce.list.ItemStackList;
import top.speedcubing.reduce.list.StringList;
import top.speedcubing.server.lang.LangItem;
import top.speedcubing.server.speedcubingServer;
import top.speedcubing.server.utils.MapManager;

public class Reduce extends JavaPlugin {
    public static World world;

    public static final int maptypes = 5;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}, {1, 0}};

    public static ScoreboardTeam NoNameTag;

    public static final Set<Integer>[] Rooms = new HashSet[maptypes];
    public static final Map<Integer, Integer> mapCache = new HashMap<>();
    public static LangItem Invalid;
    public static final Inventory spec = Bukkit.createInventory(ItemStackList.SpecHolder, 54, "");
    public static final List<Integer>[] specStorage = new ArrayList[maptypes];
    public static final Map<Integer, List<String>>[] specName = new HashMap[maptypes];
    public static final List<String> totalName = new ArrayList<>();
    public static double[] ReduceKB;
    public static String mapName = "map_reduce";

    @Override
    public void onEnable() {
        MinecraftServer.getServer().getPropertyManager().setProperty("server-name", "reduce");
        CubingEventManager.registerListeners(new ServerEvent());
        new ConfigReloadEvent().call();
        registerListeners();
        registerWorlds();

        new ItemStackList().Load();
        new BlockEditor().Load();
        NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");
        NoNameTag.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        Invalid = new LangItem(Material.STAINED_GLASS_PANE).durability(8).name("%lang_inv_cant_set_item%");
        for (int i = 0; i < maptypes; i++) {
            specStorage[i] = new ArrayList<>();
            specName[i] = new HashMap<>();
            Rooms[i] = new HashSet<>();
        }
    }

    private void registerListeners() {
        speedcubingServer.registerListeners(
                new BlockBreak(),
                new BlockPlace(),
                new EntityDamage(),
                new InventoryClick(),
                new InventoryClose(),
                new PlayerDropItem(),
                new PlayerInteract(),
                new PlayerItemHeld(),
                new PlayerJoin(),
                new PlayerMove(),
                new PlayerQuit(),
                new PlayerToggleSneak());
    }

    private void registerWorlds() {
        try {
            world = MapManager.install(mapName, mapName, Difficulty.PEACEFUL, 6000, false);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 250) - (Math.abs(z / 500) + z / 500) / (z / 250);
    }

    //quit spec
    public static void quitSpec(boolean isNull, Member member, boolean backtoarena) {
        member.user.bSetAllowFlight(false);
        member.user.bSetGameMode(GameMode.SURVIVAL);
        member.target.user.bShowPlayer(member.user.player);
        if (!isNull) {
            member.target.Spectators.remove(member);
            member.target.npc.removeListener(member.user.player);
            member.target.Holo.removeListener(member.user.player);
        }
        member.user.toNMS().removeAllEffects();
        if (backtoarena) {
            if (member.isLockedOnSomeone) {
                member.user.sendPacket(new PacketPlayOutCamera(member.user.toNMS()), new PacketPlayOutGameStateChange(3, 2));
                member.isLockedOnSomeone = false;
            }
            member.coord = member.oldCoord;
            member.map = member.oldMap;
            member.user.bTeleport(new Location(top.speedcubing.reduce.Reduce.world, member.map * 500 - MapLocations[member.map][0], 100 + MapLocations[member.map][1], member.coord + 0.5, 270, 0));
            Integer[] ints = member.integerCache;
            Inventory inventory = member.user.bGetInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][member.user.lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]]);
            inventory.setItem(ints[8], ItemStackList.Spec.get(member.user));
            inventory.setItem(ints[2], ItemStackList.settings[member.user.lang]);
            Bukkit.getScheduler().scheduleSyncDelayedTask(speedcubingServer.getInstance(), () -> {
                member.npc.spawn().hideFromTab(4000);
                member.Holo.spawn();
            }, 1);
            member.b();
        }
    }

    public static void sendPackets(PlayerConnection connection, Set<Member> connections, Packet<?>... packet) {
        for (Packet<?> p : packet) {
            connection.sendPacket(p);
        }
        connections.forEach(a -> a.user.sendPacket(packet));
    }


    public static void updateSpecPage(int id) {
        boolean notnull = id != 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Member member = Member.getMember(p);
            if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder && !(notnull && member.user.id == id)) {
                Inventory orig = spec;
                Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec.getString(member.user.lang));
                inv1.setContents(orig.getContents());
                if (p.getGameMode() == GameMode.ADVENTURE)
                    ItemUtils.glow(inv1.getItem(totalName.indexOf(member.target.user.bGetName())));
                Bukkit.getScheduler().runTask(speedcubingServer.getInstance(), () -> p.openInventory(inv1));
            }
        }
    }
}

