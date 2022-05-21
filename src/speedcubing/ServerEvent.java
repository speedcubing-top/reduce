package speedcubing;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import speedcubing.All.configcache;
import speedcubing.Core.Reduce;
import speedcubing.lib.api.MojangAPI;
import speedcubing.lib.api.SessionServer;
import speedcubing.lib.api.exception.APIErrorException;
import speedcubing.lib.bukkit.packetwrapper.OutPlayerListHeaderFooter;
import speedcubing.server.events.SocketEvent;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.speedcubingServer;
import speedcubing.spigot.Event.ServerEventHandler;
import speedcubing.spigot.Event.events.PlayInChatEvent;
import speedcubing.spigot.Event.events.PlayInUseEntityEvent;

import java.util.Set;
import java.util.UUID;

public class ServerEvent {
    @ServerEventHandler
    public void socketEvent(SocketEvent e) {
        String[] rs = e.rs;
        switch (rs[0]) {
            case "d":
                Reduce.mapCache.put(UUID.fromString(rs[1]), Integer.parseInt(rs[2]));
                speedcubingServer.tcp.send(Integer.parseInt(rs[3]), "d|" + rs[1]);
                break;
            case "o":
                if (rs[1].equals("1")) {
                    speedcubingServer.AllPlayers = Integer.parseInt(rs[2]);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Member m = Member.getMember(p.getUniqueId());
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(OutPlayerListHeaderFooter.a(GlobalString.LobbyTabList[0][m.lang], GlobalString.LobbyTabList[1][m.lang].replace("%int%", rs[2])));
                    }
                }
                break;
        }
    }

    @ServerEventHandler
    public void PlayInChatEvent(PlayInChatEvent e) {
        Player player = e.player;
        String message = e.packet.a();
        UUID uuid = player.getUniqueId();
        Member member = Member.getMember(uuid);
        if (member.setSkin) {
            e.setCancelled(true);
            if (message.contains(" ") || message.length() > 16)
                player.sendMessage(GlobalString.invalidName[member.lang]);
            else {
                String id;
                try {
                    id = MojangAPI.getUUID(message);
                } catch (APIErrorException exception) {
                    id = "invalidName";
                }
                if (id.equals("invalidName"))
                    player.sendMessage("invalid name \"" + message + "\"");
                else {
                    member.setSkin = false;
                    String finalId = id;
                    new Thread(() -> {
                        EntityPlayer entityPlayer = member.EntityPlayers;
                        if (entityPlayer != null)
                            member.setSkin(player, entityPlayer, SessionServer.getSkin(finalId));
                    }).start();
                    speedcubingServer.node(false, uuid, member.tcpPort);
                }
            }
        } else if (member.setName) {
            e.setCancelled(true);
            EntityArmorStand stand = member.Holo;
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
            Reduce.sendPackets(((CraftPlayer) player).getHandle().playerConnection, member.Spectators, new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
            member.setName = false;
            speedcubingServer.node(false, uuid, member.tcpPort);
        } else if (member.ReachSettings) {
            e.setCancelled(true);
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 25000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.ReachSettings = false;
                    member.integerCache[5] = x;
                    speedcubingServer.node(false, uuid, member.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 25.000)");
            }
        } else if (member.horSettings) {
            e.setCancelled(true);
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.horSettings = false;
                    member.integerCache[6] = x;
                    speedcubingServer.node(false, uuid, member.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
        } else if (member.verSettings) {
            e.setCancelled(true);
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.verSettings = false;
                    member.integerCache[7] = x;
                    speedcubingServer.node(false, uuid, member.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
        } else if (message.charAt(0) != '/') {
            e.setCancelled(true);
            String[] formats = speedcubingServer.getFormat(Member.getMember(uuid).rank);
            String text = formats[0] + player.getName();
            String[] str = GlobalString.MessageFormat[0];
            String[] out = new String[]{
                    str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
            };
            Bukkit.getOnlinePlayers().forEach(a -> a.sendMessage(out[Member.getMember(a.getUniqueId()).lang]));
        }
    }

    @ServerEventHandler
    public void PlayInUseEntityEvent(PlayInUseEntityEvent e) {
        Player player = e.player;
        PacketPlayInUseEntity packet = e.packet;
        if (packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) && player.getNoDamageTicks() < player.getMaximumNoDamageTicks() / 2 && player.getGameMode().equals(GameMode.SURVIVAL)) {
            UUID uuid = player.getUniqueId();
            Member member = Member.getMember(uuid);
            ItemStack hand = player.getItemInHand();
            Boolean[] bs = member.booleanCache;
            EntityPlayer entityplayer = ((CraftPlayer) player).getHandle();
            PlayerConnection connection = entityplayer.playerConnection;
            Set<PlayerConnection> spec = member.Spectators;
            int map = member.Map;
            if ((hand == null || hand.getType() != Material.INK_SACK) && member.task == -1) {
                member.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                    Location location = player.getLocation();
                    double xDiff = location.getX() - map * 500 - 0.5;
                    double zDiff = location.getZ() - member.Coord - 0.5;
                    double yDiff = player.getEyeLocation().getY() - 101.62;
                    Integer[] settings = member.integerCache;
                    if (Math.sqrt(xDiff * xDiff + zDiff * zDiff + yDiff * yDiff) <= configcache.ReduceKB[4] * settings[5] / 1000) {
                        double[] value = configcache.ReduceKB;
                        double hor = player.isOnGround() ? value[0] : value[2];
                        double horizontalDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                        PacketPlayOutAnimation animation = new PacketPlayOutAnimation(member.EntityPlayers, (byte) 0);
                        double x = xDiff * hor / horizontalDistance * settings[6] / 1000;
                        double y = (player.isOnGround() ? value[1] : value[3]) * settings[7] / 1000;
                        double z = zDiff * hor / horizontalDistance * settings[6] / 1000;
                        if (member.velocities != null) {
                            x = x * member.velocities[0];
                            y = y * member.velocities[1];
                            z = z * member.velocities[0];
                        }
                        connection.sendPacket(new PacketPlayOutEntityVelocity(player.getEntityId(), x, y, z));
                        Reduce.sendPackets(connection, spec,
                                new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), location.getX(), location.getY(), location.getZ(), 1, 1),
                                animation,
                                new PacketPlayOutAnimation(entityplayer, 1));
                        if (member.booleanCache[2] && member.Spectators != null)
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () ->
                                    Reduce.sendPackets(connection, spec, animation), 5);
                    }
                }, 0, 10);
            }
            if (bs[1])
                Reduce.sendPackets(connection, spec, new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), map * 250 + 0.5, 101.62, member.Coord + 0.5, 1, 1));
            if (bs[0])
                Reduce.sendPackets(connection, spec, new PacketPlayOutAnimation(member.EntityPlayers, (byte) 1));
            player.setNoDamageTicks(20);
        }
    }
}
