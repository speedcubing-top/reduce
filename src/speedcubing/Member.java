package speedcubing;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cubing.lib.bukkit.EntityPlayerUtils;
import cubing.lib.bukkit.ItemBuilder;
import cubing.lib.bukkit.PlayerUtils;
import cubing.lib.bukkit.SideBarAPI;
import cubingserver.libs.User;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;

import java.util.*;

public class Member {
    public static Map<UUID, Member> users = new HashMap<>();
    public EntityArmorStand Holo;
    public EntityPlayer EntityPlayers;
    public int task = -1;
    public Set<BlockPosition> blocks = new HashSet<>();
    public TimerTask SideBar;
    public int Coord;
    public int Map;
    public int oldMap;
    public int oldCoord;
    public UUID target;
    public Set<PlayerConnection> Spectators;
    public int PlacedBlocks = 0;
    public int BlockPlaced = 0;
    public boolean horSettings = false;
    public boolean verSettings = false;
    public boolean ReachSettings = false;
    public boolean setSkin = false;
    public boolean setName = false;
    public boolean kitedit = false;
    public Boolean[] booleanCache;
    public Integer[] integerCache;
    public int lang;

    public Member(Player player, int map, int room, Boolean[] a, Integer[] b, Set<PlayerConnection> Spectators) {
        UUID uuid = player.getUniqueId();
        boolean first = a == null;
        if (first)
            this.lang = User.getLang(uuid);
        int coord = 250 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        EntityPlayer entityP = ((CraftPlayer) player).getHandle();
        PlayerConnection connection = entityP.playerConnection;
        double NPCx = 500 * map + 0.5;
        Property property = entityP.getProfile().getProperties().get("textures").iterator().next();
        String[] skin = new String[]{property.getValue(), property.getSignature()};
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
        EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), world, new GameProfile(UUID.randomUUID(), " "), new PlayerInteractManager(world));
        int EntityID = entityPlayer.getId();
        Reduce.Rooms[map][room] = false;
        Reduce.used[map] += 1;
        this.Coord = coord;
        this.Map = map;
        player.setGameMode(GameMode.SURVIVAL);
        ((CraftPlayer) player).getHandle().invulnerableTicks = 0;
        entityPlayer.getProfile().getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        entityPlayer.setLocation(NPCx, 100, coord + 0.5, 90, 0);
        EntityPlayerUtils.enableOuterLayer(entityPlayer.getDataWatcher());
        player.getInventory().setHeldItemSlot(0);
        double locx = NPCx - Reduce.MapLocations[map][0];
        double locy = 100 + Reduce.MapLocations[map][1];
        double locz = coord + 0.5;
        PlayerUtils.teleport(connection, locx, locy, locz, 270, 0);
        EntityArmorStand holo = new EntityArmorStand(world);
        holo.setCustomNameVisible(true);
        holo.setGravity(true);
        holo.setInvisible(true);
        holo.n(true);
        holo.setCustomName("§6" + cubingserver.config.SERVERIP);
        holo.setLocation(NPCx, 101.85, coord + 0.5, 0, 0);
        this.Holo = holo;
        this.Spectators = Spectators;
        this.EntityPlayers = entityPlayer;
        for (PlayerConnection c : Spectators) {
            Member m = Member.users.get(c.getPlayer().getPlayer().getUniqueId());
            m.Map = map;
            m.Coord = coord;
            PlayerUtils.teleport(c, locx, locy, locz, 270, 0);
        }
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutNamedEntitySpawn(entityPlayer));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) 64));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutEntityEquipment(EntityID, 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, 1));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, 0));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, Collections.singletonList(" "), 3));
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutSpawnEntityLiving(holo));
        new Thread(() -> {
            booleanCache = first ? ArrayUtils.toObject(speedcubingServer.connection.selectBooleans("playersdata", "reduce_npccolor,reduce_npcsound,reduce_npcswing", "uuid='" + uuid + "'")) : a;
            integerCache = first ? ArrayUtils.toObject(speedcubingServer.connection.selectInts("playersdata", "reduce_stick,reduce_block,reduce_setting,reduce_stickskin,reduce_blockskin,reduce_reach,reduce_hor,reduce_ver,reduce_spec", "uuid='" + uuid + "'")) : b;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(integerCache[0], ItemStackList.Sticks[integerCache[3]][lang]);
            inventory.setItem(integerCache[1], ItemStackList.Blocks[integerCache[4]][lang]);
            inventory.setItem(integerCache[8], ItemStackList.Spec[lang]);
            inventory.setItem(integerCache[2], ItemStackList.settings[lang]);
            Reduce.b(map, player);
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

        SideBar = new TimerTask() {
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
        };
        Timer timer = new Timer();
        timer.schedule(SideBar, 0, 1000);
        Set<PlayerConnection> e = new HashSet<>(Spectators);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Reduce.sendPackets(connection, e, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
            }
        }, 4000);
        users.put(uuid, this);
    }

    public void clearBlocks() {
        org.bukkit.World world = Bukkit.getWorld("world");
        blocks.forEach(a -> world.getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
        blocks.clear();
    }

    public void stopDamage() {
        if (task != -1) {
            Bukkit.getScheduler().cancelTask(task);
            task = -1;
        }
    }

    public void teleport(double x, double y, double z, float yaw, float pitch, PlayerConnection connection) {
        int id = EntityPlayers.getId();
        Reduce.sendPackets(connection, Spectators,
                new PacketPlayOutEntityTeleport(id, MathHelper.floor(x * 32), MathHelper.floor(y * 32), MathHelper.floor(z * 32), (byte) ((int) (yaw * 256 / 360)), (byte) ((int) (pitch * 256 / 360)), true),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(id, (byte) ((int) (yaw * 256F / 360F)), (byte) ((int) (pitch * 256F / 360F)), true),
                new PacketPlayOutEntityHeadRotation(EntityPlayers, (byte) ((int) (yaw * 256F / 360F))));
    }


    public void Aim(Player player) {
        Location location = player.getLocation();
        double x = location.getX();
        double z = location.getZ();
        double NPCx = Map * 500 + 0.5;
        double NPCz = Coord + 0.5;
        double xDiff = x - NPCx;
        double zDiff = z - NPCz;
        float yaw;
        if (z == NPCz)
            yaw = xDiff > 0 ? 270 : 90;
        else if (x == NPCx)
            yaw = zDiff > 0 ? 0 : 180;
        else {
            float s = (float) Math.toDegrees(Math.atan(Math.abs(xDiff) / Math.abs(zDiff)));
            yaw = zDiff > 0 ? (xDiff > 0 ? 360 - s : s) : 180 + (xDiff > 0 ? s : -s);
        }
        double y = player.getEyeLocation().getY() - 101.62;
        double l = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        teleport(
                NPCx,
                100,
                NPCz,
                yaw,
                (l == 0) ? 270 : (y == 0 ? 0 : 360 - (float) Math.toDegrees(Math.atan(y / l))),
                ((CraftPlayer) player).getHandle().playerConnection);
    }

    public void setSkin(Player player, EntityPlayer entityPlayer, String[] skin) {
        int id = entityPlayer.getId();
        GameProfile gameProfile = entityPlayer.getProfile();
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        Reduce.sendPackets(connection, Spectators,
                new PacketPlayOutEntityDestroy(id),
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer),
                new PacketPlayOutNamedEntitySpawn(entityPlayer));
        Aim(player);
        Reduce.sendPackets(connection, Spectators, new PacketPlayOutEntityEquipment(id, 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Reduce.sendPackets(connection, Spectators, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                    }
                }, 4000);
    }
}
