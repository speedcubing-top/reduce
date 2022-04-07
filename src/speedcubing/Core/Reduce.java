package speedcubing.Core;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cubing.lib.bukkit.*;
import cubingserver.libs.User;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;

import java.util.*;

public class Reduce {
    public static final int maptypes = 4;
    public static final int rooms = 11;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}};

    public static final ScoreboardTeam NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");

    public static final boolean[][] Rooms = new boolean[maptypes][rooms];
    public static final int[] used = new int[maptypes];
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
            used[i] = 0;
            specStorage[i] = new ArrayList<>();
            specName[i] = new HashMap<>();
            for (int j = 0; j < rooms; j++) {
                Rooms[i][j] = true;
            }
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
        UUID uuid = player.getUniqueId();
        Member member = Member.users.get(uuid);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.getPlayer(member.target).showPlayer(player);
        if (!n)
            Member.users.get(member.target).Spectators.remove(connection);
        connection.getPlayer().getHandle().removeAllEffects();
        if (backtoarena) {
            member.Coord = member.oldCoord;
            member.Map = member.oldMap;
            PlayerUtils.teleport(connection, member.Map * 500 - MapLocations[member.Map][0], 100 + MapLocations[member.Map][1], member.Coord + 0.5, 270, 0);
            int lang = User.getLang(uuid);
            Integer[] ints = member.integerCache;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]][lang]);
            inventory.setItem(ints[8], ItemStackList.Spec[lang]);
            inventory.setItem(ints[2], ItemStackList.settings[lang]);
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
            int rank = User.getCode(User.getRank(uuid));
            int diffmap = 0;
            int diffrank = 0;
            int total = totalName.size();
            for (int i = 0; i < member.Map; i++) {
                diffmap += specStorage[i].size();
            }
            Map<Integer, List<String>> names = specName[member.Map];
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
            same.add(player.getName());
            Collections.sort(same);
            int index = same.indexOf(player.getName());
            int realindex = diffmap + diffrank + index;
            totalName.add(realindex, player.getName());
            specStorage[member.Map].add(diffrank + index, uuid);
            for (int i = total; i > realindex; i--) {
                spec.setItem(i, spec.getItem(i - 1));
            }
            spec.setItem(realindex, new ItemBuilder(ItemStackList.Maps[0][member.Map][0].getType()).name(User.getFormat(User.getRank(uuid))[1] + player.getName()).build());
            updateSpecPage(null);
        }
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
        if (uuid == null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder) {
                    Member m = Member.users.get(p.getUniqueId());
                    Inventory orig = spec;
                    Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[m.lang]);
                    inv1.setContents(orig.getContents());
                    if (p.getGameMode() == GameMode.ADVENTURE)
                        ItemUtils.glow(inv1.getItem(totalName.indexOf(Bukkit.getPlayer(m.target).getName())));
                    Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> p.openInventory(inv1));
                }
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                UUID id = p.getUniqueId();
                if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder && !uuid.equals(id)) {
                    Inventory orig = spec;
                    Member m = Member.users.get(id);
                    Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[m.lang]);
                    inv1.setContents(orig.getContents());
                    if (p.getGameMode() == GameMode.ADVENTURE)
                        ItemUtils.glow(inv1.getItem(totalName.indexOf(Bukkit.getPlayer(m.target).getName())));
                    Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> p.openInventory(inv1));
                }
            }
        }
    }

    public static void removeFromGUI(String name, UUID uuid, int map, UUID ignored) {
        List<String> x = specName[map].get(User.getCode(User.getRank(uuid)));
        if (x.size() == 1) {
            specName[map].remove(User.getCode(User.getRank(uuid)));
        } else x.remove(name);
        int total = totalName.size();
        specStorage[map].remove(uuid);
        Inventory inv = spec;
        for (int i = totalName.indexOf(name); i < total; i++) {
            inv.setItem(i, inv.getItem(i + 1));
        }
        inv.setItem(total, new ItemStack(Material.AIR));
        totalName.remove(name);
        updateSpecPage(ignored);
    }
}
