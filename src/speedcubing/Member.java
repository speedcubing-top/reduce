package speedcubing;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.lib.bukkit.SideBar;
import speedcubing.lib.bukkit.WorldUtils;
import speedcubing.lib.bukkit.entity.Hologram;
import speedcubing.lib.bukkit.entity.NPC;
import speedcubing.server.config;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;

import java.util.*;

public class Member {
    public static final Map<UUID, Member> users = new HashMap<>();

    public static Member getMember(UUID uuid) {
        return users.get(uuid);
    }

    public NPC npc;
    public Hologram Holo;
    public User user;
    public int task = -1;
    public Set<BlockPosition> blocks = new HashSet<>();
    public TimerTask SideBar;
    public int Coord;
    public int Map;
    public int oldMap;
    public int oldCoord;
    public UUID target;
    public Set<PlayerConnection> Spectators;
    public int PlacedBlocks;
    public int BlockPlaced;
    public boolean horSettings;
    public boolean horSettings2;
    public boolean verSettings;
    public boolean ReachSettings;
    public boolean ReachSettings2;
    public boolean setSkin;
    public boolean setName;
    public boolean kitedit;
    public Boolean[] booleanCache;
    public Integer[] integerCache;
    public PlayerConnection connection;
    public int kb;
    public int npckb;

    public Member(Player player, int map, int room, Boolean[] a, Integer[] b, Set<PlayerConnection> Spectators, Hologram holo) {
        UUID uuid = player.getUniqueId();
        this.user = User.getUser(uuid);
        int mX = 500 * map;
        int coord = 250 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        WorldUtils.cloneArea(-10 + mX, 90, -10, 10 + mX, 110, 10, -10 + mX, 90, -10 + coord, player.getWorld());
        boolean first = a == null;
        EntityPlayer entityP = ((CraftPlayer) player).getHandle();
        connection = entityP.playerConnection;
        Property property = entityP.getProfile().getProperties().get("textures").iterator().next();
        this.npc = new NPC(" ", UUID.randomUUID(), true)
                .setSkin(property.getValue(), property.getSignature(), false)
                .setLocation(mX + 0.5, 100, coord + 0.5, 90, 0);
        npc.listener.add(connection);
        npc.listener.addAll(Spectators);
        npc.spawn().items(ItemStackList.Sticks[0][0]);
        npc.hideNametag();
        this.Coord = coord;
        this.Map = map;
        player.setGameMode(GameMode.SURVIVAL);
        ((CraftPlayer) player).getHandle().invulnerableTicks = 0;
        player.getInventory().setHeldItemSlot(0);
        double locx = mX + 0.5 - Reduce.MapLocations[map][0];
        double locy = 100 + Reduce.MapLocations[map][1];
        double locz = coord + 0.5;
        player.teleport(new Location(player.getWorld(), locx, locy, locz, 270, 0));
        this.Holo = new Hologram(first ? "§6" + config.SERVERIP : holo.armorStand.getCustomName(), mX + 0.5, 101.85, coord + 0.5, 0, 0);
        Holo.listener.add(connection);
        Holo.listener.addAll(Spectators);
        Holo.spawn();
        this.Spectators = Spectators;
        for (PlayerConnection c : Spectators) {
            Member m = getMember(c.getPlayer().getUniqueId());
            m.Map = map;
            m.Coord = coord;
            c.getPlayer().teleport(new Location(player.getWorld(), locx, locy, locz, 270, 0));
        }
        new Thread(() -> {
            booleanCache = first ? ArrayUtils.toObject(speedcubingServer.connection.selectBooleans("playersdata", "reduce_npccolor,reduce_npcsound,reduce_npcswing,reduce_vanilla", "uuid='" + uuid + "'")) : a;
            integerCache = first ? ArrayUtils.toObject(speedcubingServer.connection.selectInts("playersdata", "reduce_stick,reduce_block,reduce_setting,reduce_stickskin,reduce_blockskin,reduce_reach,reduce_hor,reduce_ver,reduce_spec,reduce_vanilla_reach,reduce_vanilla_hor", "uuid='" + uuid + "'")) : b;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(integerCache[0], ItemStackList.Sticks[integerCache[3]][user.lang]);
            inventory.setItem(integerCache[1], ItemStackList.Blocks[integerCache[4]][user.lang]);
            inventory.setItem(integerCache[8], ItemStackList.Spec[user.lang]);
            inventory.setItem(integerCache[2], ItemStackList.settings[user.lang]);
            Reduce.b(map, player, this);
        }).start();

        player.updateInventory();
        String[] sidebar = StringList.ReduceSidebar[user.lang];
        new SideBar(player, sidebar[0].replace("%h%", "").replace("%m%", "00:").replace("%s%", "00"))
                .staticLine(sidebar[1], 7)
                .staticLine(sidebar[2], 6)
                .staticLine(sidebar[3], 5)
                .changeableLine(sidebar[4].replace("%str%", StringList.mapname[map][user.lang]), 4)
                .staticLine(sidebar[5], 3)
                .staticLine(sidebar[6], 2)
                .changeableLine(sidebar[7] + 0, 1)
                .staticLine(sidebar[8], 0).create();
        SideBar = new TimerTask() {
            int x = 0;

            @Override
            public void run() {
                Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                if (objective != null)
                    objective.setDisplayName(StringList.ReduceSidebar[user.lang][0]
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
                Reduce.sendPackets(connection, e, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.entityPlayer));
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

    public void Aim(Location location) {
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
        double y = location.getY() - npc.entityPlayer.locY;
        double l = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        npc.setLocation(
                NPCx,
                npc.entityPlayer.locY,
                NPCz,
                yaw,
                (l == 0) ? 270 : (y == 0 ? 0 : 360 - (float) Math.toDegrees(Math.atan(y / l)))).updateNpcLocation();
    }

    public void setSkin(Player player, String value, String signature) {
        npc.setSkin(value, signature, true);
        Aim(player.getLocation());
        npc.items(ItemStackList.Sticks[0][0]);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Reduce.sendPackets(connection, Spectators, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.entityPlayer));
                    }
                }, 4000);
    }
}
