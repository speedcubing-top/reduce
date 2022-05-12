package speedcubing.Core;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.lib.bukkit.ItemBuilder;
import speedcubing.lib.bukkit.ItemUtils;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class Reduce {
    public static final int maptypes = 4;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}};

    public static final ScoreboardTeam NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");

    public static final List<Boolean>[] Rooms = new List[maptypes];
    public static final Map<UUID, Integer> mapCache = new HashMap<>();
    public static final ItemStack[] Invalid = new ItemStack[4];
    public static final Inventory spec = Bukkit.createInventory(ItemStackList.SpecHolder, 54, "");
    public static final List<UUID>[] specStorage = new ArrayList[maptypes];
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
            Rooms[i] = new ArrayList<>();
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
        Member member = Member.getMember(player.getUniqueId());
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.getPlayer(member.target).showPlayer(player);
        if (!n)
            Member.getMember(member.target).Spectators.remove(connection);
        connection.getPlayer().getHandle().removeAllEffects();
        if (backtoarena) {
            member.Coord = member.oldCoord;
            member.Map = member.oldMap;
            PlayerUtils.teleport(connection, member.Map * 500 - MapLocations[member.Map][0], 100 + MapLocations[member.Map][1], member.Coord + 0.5, 270, 0);
            Integer[] ints = member.integerCache;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][member.lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]][member.lang]);
            inventory.setItem(ints[8], ItemStackList.Spec[member.lang]);
            inventory.setItem(ints[2], ItemStackList.settings[member.lang]);
            EntityPlayer entityPlayer = member.EntityPlayers;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
                connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                connection.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) 64));
                connection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(NoNameTag, 1));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(NoNameTag, 0));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(NoNameTag, Collections.singletonList(" "), 3));
                connection.sendPacket(new PacketPlayOutSpawnEntityLiving(member.Holo));
            }, 1);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                }
            }, 4000);
            b(member.Map, player, member);
        }
    }

    public static void b(int map, Player player, Member member) {
        UUID uuid = player.getUniqueId();
        int rank = speedcubingServer.getCode(member.rank);
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
        String name = player.getName();
        same.add(name);
        Collections.sort(same);
        int index = same.indexOf(name);
        int realindex = diffmap + diffrank + index;
        totalName.add(realindex, name);
        specStorage[map].add(diffrank + index, uuid);
        for (int i = total; i > realindex; i--) {
            spec.setItem(i, spec.getItem(i - 1));
        }
        spec.setItem(realindex, new ItemBuilder(ItemStackList.Maps[0][map][0].getType()).name(speedcubingServer.getFormat(member.rank)[1] + name).build());
        updateSpecPage(null);
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


    public static void updateSpecPage(UUID uuid) {
        boolean notnull = uuid != null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID id = p.getUniqueId();
            if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder && !(notnull && uuid.equals(id))) {
                Member m = Member.getMember(id);
                Inventory orig = spec;
                Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[m.lang]);
                inv1.setContents(orig.getContents());
                if (p.getGameMode() == GameMode.ADVENTURE)
                    ItemUtils.glow(inv1.getItem(totalName.indexOf(Bukkit.getPlayer(m.target).getName())));
                Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> p.openInventory(inv1));
            }
        }
    }

    public static void removeFromGUI(Player player, int map, UUID ignored) {
        String name = player.getName();
        UUID uuid = player.getUniqueId();
        int code = speedcubingServer.getCode(Member.getMember(uuid).rank);
        List<String> x = specName[map].get(code);
        if (x.size() == 1)
            specName[map].remove(code);
        else x.remove(name);
        int total = totalName.size();
        specStorage[map].remove(uuid);
        for (int i = totalName.indexOf(name); i < total; i++) {
            spec.setItem(i, spec.getItem(i + 1));
        }
        spec.setItem(total, new ItemStack(Material.AIR));
        totalName.remove(name);
        updateSpecPage(ignored);
    }

    public static void setRoom(int map, int room, boolean value) {
        Rooms[map].set(room - 1, value);
        if (value && room == Rooms[map].size())
            Rooms[map].remove(room - 1);
    }
}
