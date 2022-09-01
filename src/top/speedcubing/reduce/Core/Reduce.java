package top.speedcubing.reduce.Core;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.reduce.List.StringList;
import top.speedcubing.reduce.Member;
import top.speedcubing.reduce.List.ItemStackList;
import top.speedcubing.reduce.Main;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.lib.bukkit.inventory.ItemUtils;
import top.speedcubing.server.speedcubingServer;

import java.util.*;

public class Reduce {
    public static final int maptypes = 4;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}};

    public static final ScoreboardTeam NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");

    //    public static final Set<Integer>[] Rooms = new HashSet[maptypes];
    public static final int[] Rooms = new int[maptypes];
    public static final Map<Integer, Integer> mapCache = new HashMap<>();
    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final Inventory spec = Bukkit.createInventory(ItemStackList.SpecHolder, 54, "");
    public static final List<Integer>[] specStorage = new ArrayList[maptypes];
    public static final Map<Integer, List<String>>[] specName = new HashMap[maptypes];
    public static final List<String> totalName = new ArrayList<>();

    public void Load() {
        NoNameTag.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        for (int i = 0; i < 4; i++) {
            Invalid[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).name(new String[]{"§4Sie können die Elemente hier nicht einstellen!", "§4You can't set the items here!", "§4你不能將物品設定於此處!", "§4你不能将物品设定于此处!"}[i]).build();
        }
        for (int i = 0; i < maptypes; i++) {
            specStorage[i] = new ArrayList<>();
            specName[i] = new HashMap<>();
//            Rooms[i] = new HashSet<>();
            Rooms[i] = 0;
        }
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 250) - (Math.abs(z / 500) + z / 500) / (z / 250);
    }

    //quit spec
    public static void quitSpec(Player player, PlayerConnection connection, boolean backtoarena) {
        boolean n = player == null;
        if (n)
            player = connection.getPlayer().getPlayer();
        Member member = Member.getMember(player);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        member.target.user.player.showPlayer(player);
        if (!n) {
            member.target.Spectators.remove(connection);
            member.target.npc.listener.remove(connection);
            member.target.Holo.listener.remove(connection);
        }
        connection.getPlayer().getHandle().removeAllEffects();
        if (backtoarena) {
            member.coord = member.oldCoord;
            member.map = member.oldMap;
            player.teleport(new Location(player.getWorld(), member.map * 500 - MapLocations[member.map][0], 100 + MapLocations[member.map][1], member.coord + 0.5, 270, 0));
            Integer[] ints = member.integerCache;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][member.user.lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]][member.user.lang]);
            inventory.setItem(ints[8], ItemStackList.Spec[member.user.lang]);
            inventory.setItem(ints[2], ItemStackList.settings[member.user.lang]);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                member.npc.spawn();
                member.Holo.spawn();
            }, 1);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, member.npc.entityPlayer));
                }
            }, 4000);
            b(member.map, member);
        }
    }

    public static void b(int map, Member member) {
        int rank = speedcubingServer.getCode(member.user.rank);
        int diffmap = 0;
        int diffrank = 0;
        int total = totalName.size();
        for (int i = 0; i < map; i++) {
            diffmap += specStorage[i].size();
        }
        Map<Integer, List<String>> names = specName[map];
        List<Integer> ranks = new ArrayList<>(names.keySet());
        Collections.sort(ranks);
        for (Integer i : ranks) {
            if (i < rank)
                diffrank += names.get(i).size();
            else break;
        }
        if (!names.containsKey(rank))
            names.put(rank, new ArrayList<>());
        List<String> same = names.get(rank);
        String name = member.user.player.getName();
        same.add(name);
        Collections.sort(same);
        int index = same.indexOf(name);
        int realindex = diffmap + diffrank + index;
        totalName.add(realindex, name);
        specStorage[map].add(diffrank + index, member.user.id);
        for (int i = total; i > realindex; i--) {
            spec.setItem(i, spec.getItem(i - 1));
        }
        spec.setItem(realindex, new ItemBuilder(ItemStackList.Maps[0][map][0].getType()).name(speedcubingServer.getFormat(member.user.rank)[1] + name).build());
        updateSpecPage(0);
    }

    public static void sendPackets(PlayerConnection connection, Set<PlayerConnection> connections, Packet<?>... packet) {
        for (Packet<?> p : packet) {
            connection.sendPacket(p);
        }
        connections.forEach(a -> {
            for (Packet<?> p : packet) {
                a.sendPacket(p);
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
                Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> p.openInventory(inv1));
            }
        }
    }

    public static void removeFromGUI(Member member, int map, int ignored) {
        String name = member.user.player.getName();
        int code = speedcubingServer.getCode(member.user.rank);
        List<String> x = specName[map].get(code);
        if (x.size() == 1)
            specName[map].remove(code);
        else x.remove(name);
        int total = totalName.size();
        specStorage[map].remove(Integer.valueOf(member.user.id));
        for (int i = totalName.indexOf(name); i < total; i++) {
            spec.setItem(i, spec.getItem(i + 1));
        }
        spec.setItem(total, new ItemStack(Material.AIR));
        totalName.remove(name);
        updateSpecPage(ignored);
    }
}
