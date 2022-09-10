package top.speedcubing.reduce;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import top.speedcubing.lib.bukkit.SideBar;
import top.speedcubing.lib.bukkit.WorldUtils;
import top.speedcubing.lib.bukkit.entity.Hologram;
import top.speedcubing.lib.bukkit.entity.NPC;
import top.speedcubing.lib.utils.SQL.SQLUtils;
import top.speedcubing.lib.utils.TimeFormatter;
import top.speedcubing.reduce.All.configcache;
import top.speedcubing.reduce.Core.Reduce;
import top.speedcubing.reduce.List.ItemStackList;
import top.speedcubing.reduce.List.StringList;
import top.speedcubing.server.config;
import top.speedcubing.server.libs.User;
import top.speedcubing.server.speedcubingServer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Member {
    public static Map<Integer, Member> users = new HashMap<>();

    public static Member getMember(CommandSender sender) {
        return getMember(User.getUser(sender).id);
    }

    public static Member getMember(int id) {
        return users.get(id);
    }

    public final User user;

    public NPC npc;
    public Hologram Holo;
    public int task = -1;
    public Set<BlockPosition> blocks = new HashSet<>();
    public TimerTask SideBar;
    public int coord;
    public int map;
    public int oldMap;
    public int oldCoord;
    public Member target;
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

    public Member(Player player) {
        this.user = User.getUser(player);
        users.put(user.id, this);
    }

    public void startGame(Player player, int map, int room, Boolean[] a, Integer[] b, Set<PlayerConnection> Spectators, Hologram holo) {
        int mX = 500 * map;
        this.coord = 250 * (room * (room % 2 == 0 ? -1 : 1) + room % 2);
        WorldUtils.cloneArea(-10 + mX, 90, -10, 10 + mX, 110, 10, -10 + mX, 90, -10 + coord, Main.world);
        this.map = map;
        boolean first = a == null;
        EntityPlayer entityplayer = ((CraftPlayer) player).getHandle();
        connection = entityplayer.playerConnection;
        Property property = first ? entityplayer.getProfile().getProperties().get("textures").iterator().next() : npc.entityPlayer.getProfile().getProperties().get("textures").iterator().next();
        this.npc = new NPC(" ", null, true)
                .setSkin(property.getValue(), property.getSignature())
                .setSpawnBodyYaw(90)
                .setLocation(mX + 0.5, 100, coord + 0.5, 90, 0)
                .hideNametag()
                .setItemInHand(ItemStackList.Sticks[0][0])
                .setAutoListen(false)
                .setClickEvent((p, action) -> {
                    if (action.equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) && p.getNoDamageTicks() < p.getMaximumNoDamageTicks() / 2 && p.getGameMode().equals(GameMode.SURVIVAL)) {
                        ItemStack hand = p.getItemInHand();
                        Boolean[] bs = booleanCache;
                        if ((hand == null || hand.getType() != Material.INK_SACK) && task == -1) {
                            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                                Location location = p.getLocation();
                                double xDiff = location.getX() - map * 500 - 0.5;
                                double zDiff = location.getZ() - coord - 0.5;
                                double yDiff = p.getEyeLocation().getY() - 101.62;
                                if (Math.sqrt(xDiff * xDiff + zDiff * zDiff + yDiff * yDiff) <= configcache.ReduceKB[4] * integerCache[booleanCache[3] ? 9 : 5] / 1000) {
                                    double[] value = configcache.ReduceKB;
                                    double hor = p.isOnGround() ? value[0] : value[2];
                                    double horizontalDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                                    double x = xDiff * hor / horizontalDistance * integerCache[booleanCache[3] ? 10 : 6] / 1000;
                                    double y = (p.isOnGround() ? value[1] : value[3]) * integerCache[7] / 1000;
                                    double z = zDiff * hor / horizontalDistance * integerCache[booleanCache[3] ? 10 : 6] / 1000;
                                    if (booleanCache[3])
                                        y = gettheKb(p);
                                    if (user.velocities != null) {
                                        x = x * user.velocities[0];
                                        y = y * user.velocities[1];
                                        z = z * user.velocities[0];
                                    }
                                    connection.sendPacket(new PacketPlayOutEntityVelocity(p.getEntityId(), x, y, z));
                                    npc.animation(0);
                                    Reduce.sendPackets(connection, Spectators, new PacketPlayOutAnimation(entityplayer, 1));
                                    Reduce.sendPackets(connection, Spectators, new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), location.getX(), location.getY(), location.getZ(), 1, 1));
                                    if (booleanCache[2] && Spectators != null)
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () ->
                                                npc.animation(0), 5);
                                }
                            }, 0, 10);
                        }
                        if (bs[1])
                            Reduce.sendPackets(connection, Spectators, new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), map * 250 + 0.5, 101.62, coord + 0.5, 1, 1));
                        if (bs[0])
                            npc.animation(1);
                        player.setNoDamageTicks(20);
                    }
                });
        npc.listener.add(connection);
        npc.listener.addAll(Spectators);
        npc.spawn();
        player.setGameMode(GameMode.SURVIVAL);
        ((CraftPlayer) player).getHandle().invulnerableTicks = 0;
        player.getInventory().setHeldItemSlot(0);
        double locx = mX + 0.5 - Reduce.MapLocations[map][0];
        double locy = 100 + Reduce.MapLocations[map][1];
        double locz = coord + 0.5;
        player.teleport(new Location(Main.world, locx, locy, locz, 270, 0));
        this.Holo = new Hologram(first ? "§6" + config.SERVERIP : holo.armorStand.getCustomName(), mX + 0.5, 101.85, coord + 0.5, 0, 0).setAutoListen(false);
        Holo.listener.add(connection);
        Holo.listener.addAll(Spectators);
        Holo.spawn();
        this.Spectators = Spectators;
        for (PlayerConnection c : Spectators) {
            Member m = getMember(c.getPlayer());
            m.map = map;
            m.coord = coord;
            c.getPlayer().teleport(new Location(Main.world, locx, locy, locz, 270, 0));
        }
        new Thread(() -> {
            booleanCache = first ? SQLUtils.getBooleanArray(speedcubingServer.connection.select("playersdata", "reduce_npccolor,reduce_npcsound,reduce_npcswing,reduce_vanilla", "id=" + user.id)) : a;
            integerCache = first ? SQLUtils.getIntArray(speedcubingServer.connection.select("playersdata", "reduce_stick,reduce_block,reduce_setting,reduce_stickskin,reduce_blockskin,reduce_reach,reduce_hor,reduce_ver,reduce_spec,reduce_vanilla_reach,reduce_vanilla_hor", "id=" + user.id)) : b;
            Inventory inventory = player.getInventory();
            inventory.clear();
            inventory.setItem(integerCache[0], ItemStackList.Sticks[integerCache[3]][user.lang]);
            inventory.setItem(integerCache[1], ItemStackList.Blocks[integerCache[4]][user.lang]);
            inventory.setItem(integerCache[8], ItemStackList.Spec[user.lang]);
            inventory.setItem(integerCache[2], ItemStackList.settings[user.lang]);
            Reduce.b(map, this);
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
                    objective.setDisplayName(StringList.ReduceSidebar[user.lang][0].replace("%t%",new TimeFormatter(x, TimeUnit.SECONDS).format("%H%:%m%:%s%")));
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
    }

    public void clearBlocks() {
        blocks.forEach(a -> Main.world.getBlockAt(a.getX(), a.getY(), a.getZ()).setType(Material.AIR));
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
        double NPCx = map * 500 + 0.5;
        double NPCz = coord + 0.5;
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
        npc.setSkin(value, signature).setItemInHand(ItemStackList.Sticks[0][0]);
        Aim(player.getLocation());
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Reduce.sendPackets(connection, Spectators, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.entityPlayer));
                    }
                }, 4000);
    }


    public double gettheKb(Player player) {
        kb++;
        if (kb == 3 || player.isOnGround())
            kb = 0;
        if (kb == 0)
            return player.getLocation().getY() < npc.entityPlayer.locY - 1 ? 0.01 : 0.46;
        if (kb == 1)
            return player.getLocation().getY() < npc.entityPlayer.locY - 1 ? 0.01 : 0.38;
        if (kb == 2)
            return 0.01;
        return -1;
    }
}
