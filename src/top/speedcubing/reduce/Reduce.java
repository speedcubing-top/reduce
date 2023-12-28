package top.speedcubing.reduce;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import top.speedcubing.lib.bukkit.inventory.*;
import top.speedcubing.lib.eventbus.CubingEventManager;
import top.speedcubing.reduce.BukkitEventListener.*;
import top.speedcubing.reduce.Core.BlockEditor;
import top.speedcubing.reduce.List.*;
import top.speedcubing.server.lang.GlobalString;
import top.speedcubing.server.utils.config;

import java.util.*;

public class Reduce extends JavaPlugin {
    public static World world;


    public static final int maptypes = 5;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}, {1, 0}};

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
        Bukkit.getPluginManager().registerEvents(new PlayerToggleSneak(), this);

        world = Bukkit.getWorld("world");
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setTime(6000);
        world.setAutoSave(false);

        new ItemStackList().Load();
        new BlockEditor().Load();
        NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");
        NoNameTag.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        for (int i = 0; i < 4; i++) {
            Invalid[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).name(GlobalString.CantSetItemhere.get(i)).build();
        }
        for (int i = 0; i < maptypes; i++) {
            specStorage[i] = new ArrayList<>();
            specName[i] = new HashMap<>();
            Rooms[i] = new HashSet<>();
        }
        CubingEventManager.registerListeners(new ServerEvent());
        config.event.call();
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
                Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec.get(member.user.lang));
                inv1.setContents(orig.getContents());
                if (p.getGameMode() == GameMode.ADVENTURE)
                    ItemUtils.glow(inv1.getItem(totalName.indexOf(member.target.user.bGetName())));
                Bukkit.getScheduler().runTask(top.speedcubing.reduce.Reduce.getPlugin(top.speedcubing.reduce.Reduce.class), () -> p.openInventory(inv1));
            }
        }
    }
}

