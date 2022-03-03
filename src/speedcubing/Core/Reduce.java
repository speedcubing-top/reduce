package speedcubing.Core;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cubing.bukkit.EntityPlayerUtils;
import cubing.bukkit.ItemUtils;
import cubing.bukkit.PlayerUtils;
import cubing.bukkit.SkinUtils;
import cubing.bukkit.api.SideBarAPI;
import cubingserver.libs.PlayerData;
import cubingserver.libs.Rank;
import cubingserver.speedcubingServer;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang.ArrayUtils;
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

import java.util.*;

public class Reduce {
    public static final int maptypes = 4;
    public static final int rooms = 11;
    public static final int[][] MapLocations = {{2, 0}, {1, 0}, {0, 4}, {1, 0}};

    public static final ScoreboardTeam NoNameTag = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), "");

    public static final boolean[][] Rooms = new boolean[maptypes][rooms];
    public static final int[] used = new int[maptypes];

    public static final Map<UUID, EntityArmorStand> Holo = new HashMap<>();

    public static final Map<UUID, EntityPlayer> EntityPlayers = new HashMap<>();
    public static final Map<UUID, Integer> DamageTask = new HashMap<>();

    public static final Map<UUID, Set<BlockPosition>> blocks = new HashMap<>();
    public static final Map<UUID, TimerTask> SideBar = new HashMap<>();


    public static final Map<UUID, Integer> PlayerCoord = new HashMap<>();
    public static final Map<UUID, Integer> PlayerMap = new HashMap<>();

    public static final Map<UUID, UUID> target = new HashMap<>();
    public static final Map<UUID, Set<PlayerConnection>> Spectators = new HashMap<>();

    public static final Map<UUID, Integer> PlacedBlocks = new HashMap<>();
    public static final Map<UUID, Integer> BlockPlaced = new HashMap<>();

    public static final Set<UUID> horSettings = new HashSet<>();
    public static final Set<UUID> verSettings = new HashSet<>();
    public static final Set<UUID> ReachSettings = new HashSet<>();
    public static final Set<UUID> setSkin = new HashSet<>();
    public static final Set<UUID> setName = new HashSet<>();
    public static final Set<UUID> kitedit = new HashSet<>();

    public static final Map<UUID, Boolean[]> booleanCache = new HashMap<>();
    public static final Map<UUID, Integer[]> integerCache = new HashMap<>();

    public static final ItemStack[] Invalid = new ItemStack[4];

    public static final Inventory spec = Bukkit.createInventory(ItemStackList.SpecHolder, 54, "");
    public static final List<UUID>[] specStorage = new ArrayList[maptypes];
    public static final Map<Integer, List<String>>[] specName = new HashMap[maptypes];
    public static final List<String> totalName = new ArrayList<>();

    public void Load() {
        NoNameTag.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        for (int i = 0; i < 4; i++) {
            Invalid[i] = ItemUtils.item(Material.STAINED_GLASS_PANE, new String[]{"§4Sie können die Elemente hier nicht einstellen!", "§4You can't set the items here!", "§4你不能將物品設定於此處!", "§4你不能将物品设定于此处!"}[i], 1, 8, false, false, null, null, null, null);
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


    public static void GameStart(int map, Player player, UUID uuid, int room, Set<PlayerConnection> spectators) {
        int lang = PlayerData.getLang(uuid);
        int coord = 250 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        EntityPlayer entityP = ((CraftPlayer) player).getHandle();
        PlayerConnection connection = entityP.playerConnection;
        double NPCx = 500 * map + 0.5;
        String[] skin = SkinUtils.getProfileSkin(entityP.getProfile());
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
        EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), world, new GameProfile(UUID.randomUUID(), " "), new PlayerInteractManager(world));
        int EntityID = entityPlayer.getId();
        Rooms[map][room] = false;
        used[map] += 1;
        PlayerCoord.put(uuid, coord);
        PlayerMap.put(uuid, map);
        player.setGameMode(GameMode.SURVIVAL);
        entityPlayer.getProfile().getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        entityPlayer.setLocation(NPCx, 100, coord + 0.5, 90, 0);
        EntityPlayerUtils.enableOuterLayer(entityPlayer.getDataWatcher());
        player.getInventory().setHeldItemSlot(0);
        double locx = NPCx - MapLocations[map][0];
        double locy = 100 + MapLocations[map][1];
        double locz = coord + 0.5;
        PlayerUtils.teleport(connection, locx, locy, locz, 270, 0);
        EntityArmorStand holo = new EntityArmorStand(world);
        holo.setCustomNameVisible(true);
        holo.setGravity(true);
        holo.setInvisible(true);
        holo.n(true);
        holo.setCustomName("§6" + cubingserver.configcache.SERVERIP);
        holo.setLocation(NPCx, 101.85, coord + 0.5, 0, 0);
        Holo.put(uuid, holo);
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        PacketPlayOutEntityHeadRotation rotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) 64);
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(EntityID, 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0]));
        PacketPlayOutScoreboardTeam team1 = new PacketPlayOutScoreboardTeam(NoNameTag, 1);
        PacketPlayOutScoreboardTeam team2 = new PacketPlayOutScoreboardTeam(NoNameTag, 0);
        PacketPlayOutScoreboardTeam team3 = new PacketPlayOutScoreboardTeam(NoNameTag, Collections.singletonList(" "), 3);
        PacketPlayOutSpawnEntityLiving living = new PacketPlayOutSpawnEntityLiving(holo);
        if (spectators != null) {
            for (PlayerConnection c : spectators) {
                UUID id = c.getPlayer().getPlayer().getUniqueId();
                PlayerMap.put(id, map);
                PlayerCoord.put(id, coord);
                PlayerUtils.teleport(c, locx, locy, locz, 270, 0);
            }
            for (PlayerConnection c : spectators) {
                c.sendPacket(info);
                c.sendPacket(spawn);
                c.sendPacket(rotation);
                c.sendPacket(equipment);
                c.sendPacket(team1);
                c.sendPacket(team2);
                c.sendPacket(team3);
                c.sendPacket(living);
            }
        } else
            Spectators.put(uuid, new HashSet<>());
        blocks.put(uuid, new HashSet<>());
        EntityPlayers.put(uuid, entityPlayer);
        BlockPlaced.put(uuid, 0);
        PlacedBlocks.put(uuid, 0);
        connection.sendPacket(info);
        connection.sendPacket(spawn);
        connection.sendPacket(rotation);
        connection.sendPacket(equipment);
        connection.sendPacket(team1);
        connection.sendPacket(team2);
        connection.sendPacket(team3);
        connection.sendPacket(living);
        new Thread(() -> {
            if (!booleanCache.containsKey(uuid)) {
                booleanCache.put(uuid, ArrayUtils.toObject(speedcubingServer.connection.selectBooleans("playersdata", "reduce_npccolor,reduce_npcsound,reduce_npcswing,reduce_allowspec", "uuid='" + uuid + "'")));
                integerCache.put(uuid, ArrayUtils.toObject(speedcubingServer.connection.selectInts("playersdata", "reduce_stick,reduce_block,reduce_setting,reduce_stickskin,reduce_blockskin,reduce_reach,reduce_hor,reduce_ver,reduce_spec", "uuid='" + uuid + "'")));
            }
            Integer[] ints = integerCache.get(uuid);
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]][lang]);
            inventory.setItem(ints[8], ItemStackList.Spec[lang]);
            inventory.setItem(ints[2], ItemStackList.settings[lang]);
            if (booleanCache.get(uuid)[3]) {
                int rank = PlayerData.getRank(uuid);
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
                same.add(player.getName());
                Collections.sort(same);
                int index = same.indexOf(player.getName());
                int realindex = diffmap + diffrank + index;
                totalName.add(realindex, player.getName());
                specStorage[map].add(diffrank + index, uuid);
                for (int i = total; i > realindex; i--) {
                    spec.setItem(i, spec.getItem(i - 1));
                }
                spec.setItem(realindex, ItemUtils.item(ItemStackList.Maps[0][map][0].getType(), Rank.format(uuid)[0] + player.getName(), 1, -1, false, false, null, null, null, null));
                updateSpecPage(null);
            }
        }).start();

        player.updateInventory();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        String[] sidebar = StringList.ReduceSidebar[lang];
        objective.setDisplayName(sidebar[8].replace("%h%", "").replace("%m%", "00:").replace("%s%", "00"));
        SideBarAPI.sidebarTeams(scoreboard, new String[][]{
                {sidebar[7]},
                {sidebar[6]},
                {sidebar[5]},
                {sidebar[4].replace("%str%", StringList.mapname[map][lang])},
                {sidebar[3]},
                {sidebar[2]},
                {"", sidebar[1], "0"},
                {sidebar[0]},
        }, new int[]{7, 6, 5, 4, 3, 2, 1, 0});
        player.setScoreboard(scoreboard);

        SideBar.put(uuid, new TimerTask() {
            int x = 0;

            @Override
            public void run() {
                Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                if (objective != null)
                    objective.setDisplayName(StringList.ReduceSidebar[lang][8]
                            .replace("%h%", x / 3600 == 0 ? "" : x / 3600 + ":")
                            .replace("%m%", String.format("%02d", (x % 3600) / 60) + ":")
                            .replace("%s%", String.format("%02d", x % 60)));
                x++;
            }
        });
        Timer timer = new Timer();
        timer.schedule(SideBar.get(uuid), 0, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
            }
        }, 4000);
    }

    public static void Aim(Player player, Set<PlayerConnection> spec) {
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        double x = location.getX();
        double z = location.getZ();
        double NPCx = PlayerMap.get(uuid) * 500 + 0.5;
        double NPCz = PlayerCoord.get(uuid) + 0.5;
        double xDiff = x - NPCx;
        double zDiff = z - NPCz;
        float yaw;
        if (z == NPCz)
            yaw = xDiff > 0 ? 270 : 90;
        else if (x == NPCx)
            yaw = zDiff > 0 ? 0 : 180;
        else {
            float s = (float) (Math.atan(Math.abs(xDiff) / Math.abs(zDiff)) * 180 / Math.PI);
            yaw = zDiff > 0 ? (xDiff > 0 ? 360 - s : s) : (xDiff > 0 ? 180 + s : 180 - s);
        }
        double y = player.getEyeLocation().getY() - 101.62;
        double l = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        teleport(
                NPCx,
                100,
                NPCz,
                yaw,
                (l == 0) ? 270 : (y == 0 ? 0 : 360 - (float) (Math.atan(y / l) * 180 / Math.PI)),
                ((CraftPlayer) player).getHandle().playerConnection, uuid, spec);
    }

    public static void StopDamage(UUID uuid) {
        if (DamageTask.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(DamageTask.get(uuid));
            DamageTask.remove(uuid);
        }
    }

    public static void teleport(double x, double y, double z, float yaw, float pitch, PlayerConnection connection, UUID uuid, Set<PlayerConnection> spec) {
        EntityPlayer entityPlayer = EntityPlayers.get(uuid);
        int id = entityPlayer.getId();
        sendPackets(connection, spec,
                new PacketPlayOutEntityTeleport(id, MathHelper.floor(x * 32), MathHelper.floor(y * 32), MathHelper.floor(z * 32), (byte) ((int) (yaw * 256 / 360)), (byte) ((int) (pitch * 256 / 360)), true),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(id, (byte) ((int) (yaw * 256F / 360F)), (byte) ((int) (pitch * 256F / 360F)), true),
                new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) ((int) (yaw * 256F / 360F))));
    }

    public static int coordToRoom(int z) {
        return z == 0 ? 0 : Math.abs(z / 250) - (Math.abs(z / 500) + z / 500) / (z / 250);
    }

    //quit spec
    public static void quitSpec(Player player, PlayerConnection connection, boolean backtoarena) {
        if (player == null)
            player = connection.getPlayer().getPlayer();
        UUID uuid = player.getUniqueId();
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        UUID targetUUID = target.get(uuid);
        Bukkit.getPlayer(targetUUID).showPlayer(player);
        Spectators.get(targetUUID).remove(connection);
        target.remove(uuid);
        if (backtoarena) {
            int map = PlayerMap.get(uuid);
            PlayerUtils.teleport(connection, map * 500 - MapLocations[map][0], 100 + MapLocations[map][1], PlayerCoord.get(uuid) + 0.5, 270, 0);
            int lang = PlayerData.getLang(uuid);
            Integer[] ints = integerCache.get(uuid);
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(ints[0], ItemStackList.Sticks[ints[3]][lang]);
            inventory.setItem(ints[1], ItemStackList.Blocks[ints[4]][lang]);
            inventory.setItem(ints[8], ItemStackList.Spec[lang]);
            inventory.setItem(ints[2], ItemStackList.settings[lang]);
            EntityPlayer entityPlayer = EntityPlayers.get(uuid);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
                connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                connection.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) 64));
                connection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(NoNameTag, 1));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(NoNameTag, 0));
                connection.sendPacket(new PacketPlayOutScoreboardTeam(NoNameTag, Collections.singletonList(" "), 3));
                connection.sendPacket(new PacketPlayOutSpawnEntityLiving(Holo.get(uuid)));
            }, 1);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                }
            }, 4000);
            if (booleanCache.get(uuid)[3]) {
                int rank = PlayerData.getRank(uuid);
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
                    if (i < rank) {
                        diffrank += names.get(i).size();
                    } else break;
                }
                if (!names.containsKey(rank))
                    names.put(rank, new ArrayList<>());
                List<String> same = names.get(rank);
                same.add(player.getName());
                Collections.sort(same);
                int index = same.indexOf(player.getName());
                int realindex = diffmap + diffrank + index;
                totalName.add(realindex, player.getName());
                specStorage[map].add(diffrank + index, uuid);
                for (int i = total; i > realindex; i--) {
                    spec.setItem(i, spec.getItem(i - 1));
                }
                spec.setItem(realindex, ItemUtils.item(ItemStackList.Maps[0][map][0].getType(), Rank.format(uuid)[0] + player.getName(), 1, -1, false, false, null, null, null, null));
                updateSpecPage(null);
            }
        }
    }

    public static void sendPackets(PlayerConnection connection, Set<PlayerConnection> connections, Packet... packet) {
        for (Packet p : packet) {
            connection.sendPacket(p);
        }
        for (PlayerConnection c : connections) {
            for (Packet p : packet) {
                c.sendPacket(p);
            }
        }
    }

    public static void setSkin(Player player, EntityPlayer entityPlayer, UUID uuid, String[] skin) {
        int id = entityPlayer.getId();
        GameProfile gameProfile = entityPlayer.getProfile();
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        Set<PlayerConnection> spec = Spectators.get(uuid);
        sendPackets(connection, spec,
                new PacketPlayOutEntityDestroy(id),
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer),
                new PacketPlayOutNamedEntitySpawn(entityPlayer));
        Aim(player, spec);
        sendPackets(connection, spec, new PacketPlayOutEntityEquipment(id, 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        sendPackets(connection, spec, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                    }
                }, 4000);
    }

    public static void updateSpecPage(UUID uuid) {
        if (uuid == null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder) {
                    UUID id = p.getUniqueId();
                    Inventory orig = spec;
                    Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[PlayerData.getLang(id)]);
                    inv1.setContents(orig.getContents());
                    if (p.getGameMode() == GameMode.ADVENTURE)
                        ItemUtils.glow(inv1.getItem(totalName.indexOf(Bukkit.getPlayer(target.get(id)).getName())));
                    Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> p.openInventory(inv1));
                }
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                UUID id = p.getUniqueId();
                if (p.getOpenInventory().getTopInventory().getHolder() == ItemStackList.SpecHolder && !uuid.equals(id)) {
                    Inventory orig = spec;
                    Inventory inv1 = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[PlayerData.getLang(id)]);
                    inv1.setContents(orig.getContents());
                    if (p.getGameMode() == GameMode.ADVENTURE)
                        ItemUtils.glow(inv1.getItem(totalName.indexOf(Bukkit.getPlayer(target.get(id)).getName())));
                    Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> p.openInventory(inv1));
                }
            }
        }
    }

    public static void removeFromGUI(String name, UUID uuid, int map, UUID ignored) {
        List<String> x = specName[map].get(PlayerData.getRank(uuid));
        if (x.size() == 1) {
            specName[map].remove(PlayerData.getRank(uuid));
        } else x.remove(name);
        int total = totalName.size();
        int index = totalName.indexOf(name);
        specStorage[map].remove(uuid);
        Inventory inv = spec;
        for (int i = index; i < total; i++) {
            inv.setItem(i, inv.getItem(i + 1));
        }
        inv.setItem(total, new ItemStack(Material.AIR));
        totalName.remove(name);
        updateSpecPage(ignored);
    }
}
