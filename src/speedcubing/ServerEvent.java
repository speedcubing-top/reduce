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
import speedcubing.lib.bukkit.event.PlayInChatEvent;
import speedcubing.lib.bukkit.event.PlayInUseEntityEvent;
import speedcubing.lib.eventbus.LibEventHandler;
import speedcubing.server.events.SocketEvent;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.speedcubingServer;

import java.util.Set;
import java.util.UUID;

public class ServerEvent {
    @LibEventHandler
    public void socketEvent(SocketEvent e) {
        String[] rs = e.receive.split("\\|");
        switch (rs[0]) {
            case "reduce":
                Reduce.mapCache.put(UUID.fromString(rs[1]), Integer.parseInt(rs[2]));
                speedcubingServer.tcp.send(Integer.parseInt(rs[3]), "reduce|" + rs[1]);
                break;
        }
    }

    @LibEventHandler
    public void PlayInChatEvent(PlayInChatEvent e) {
        Player player = e.player;
        String message = e.packet.a();
        UUID uuid = player.getUniqueId();
        Member member = Member.getMember(uuid);
        if (member.setSkin) {
            e.isCancelled = true;
            if (message.contains(" ") || message.length() > 16)
                player.sendMessage(GlobalString.invalidName[member.user.lang]);
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
                        String[] a = SessionServer.getSkin(finalId);
                        if (member.npc != null)
                            member.setSkin(player, a[0], a[1]);
                    }).start();
                    speedcubingServer.node(false, uuid, member.user.tcpPort);
                }
            }
        } else if (member.setName) {
            e.isCancelled = true;
            member.Holo.setName(ChatColor.translateAlternateColorCodes('&', message));
            member.setName = false;
            speedcubingServer.node(false, uuid, member.user.tcpPort);
        } else if (member.ReachSettings2) {
            e.isCancelled = true;
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 25000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.ReachSettings2 = false;
                    member.integerCache[9] = x;
                    speedcubingServer.node(false, uuid, member.user.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 25.000)");
            }
        } else if (member.horSettings2) {
            e.isCancelled = true;
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.horSettings2 = false;
                    member.integerCache[10] = x;
                    speedcubingServer.node(false, uuid, member.user.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
        } else if (member.ReachSettings) {
            e.isCancelled = true;
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 25000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.ReachSettings = false;
                    member.integerCache[5] = x;
                    speedcubingServer.node(false, uuid, member.user.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 25.000)");
            }
        } else if (member.horSettings) {
            e.isCancelled = true;
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.horSettings = false;
                    member.integerCache[6] = x;
                    speedcubingServer.node(false, uuid, member.user.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
        } else if (member.verSettings) {
            e.isCancelled = true;
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    member.verSettings = false;
                    member.integerCache[7] = x;
                    speedcubingServer.node(false, uuid, member.user.tcpPort);
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
        } else if (message.charAt(0) != '/') {
            e.isCancelled = true;
            String[] formats = speedcubingServer.getFormat(Member.getMember(uuid).user.rank);
            String text = formats[0] + player.getName();
            String[] str = GlobalString.MessageFormat[0];
            String[] out = new String[]{
                    str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                    str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
            };
            Bukkit.getOnlinePlayers().forEach(a -> a.sendMessage(out[Member.getMember(a.getUniqueId()).user.lang]));
        }
    }

    @LibEventHandler
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
                    boolean vanillaKB = member.booleanCache[3];
                    if (Math.sqrt(xDiff * xDiff + zDiff * zDiff + yDiff * yDiff) <= configcache.ReduceKB[4] * settings[vanillaKB ? 9 : 5] / 1000) {
                        double[] value = configcache.ReduceKB;
                        double hor = player.isOnGround() ? value[0] : value[2];
                        double horizontalDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                        double x = xDiff * hor / horizontalDistance * settings[vanillaKB ? 10 : 6] / 1000;
                        double y = (player.isOnGround() ? value[1] : value[3]) * settings[7] / 1000;
                        double z = zDiff * hor / horizontalDistance * settings[vanillaKB ? 10 : 6] / 1000;
                        double[] velocities = member.user.velocities;
                        if (vanillaKB)
                            y = gettheKb(player, member);
                        if (velocities != null) {
                            x = x * velocities[0];
                            y = y * velocities[1];
                            z = z * velocities[0];
                        }
                        connection.sendPacket(new PacketPlayOutEntityVelocity(player.getEntityId(), x, y, z));
                        member.npc.animation(0);
                        Reduce.sendPackets(connection, spec, new PacketPlayOutAnimation(entityplayer, 1));
                        Reduce.sendPackets(connection, spec, new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), location.getX(), location.getY(), location.getZ(), 1, 1));
                        if (member.booleanCache[2] && member.Spectators != null)
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () ->
                                    member.npc.animation(0), 5);
                    }
                }, 0, 10);
            }
            if (bs[1])
                Reduce.sendPackets(connection, spec, new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), map * 250 + 0.5, 101.62, member.Coord + 0.5, 1, 1));
            if (bs[0])
                member.npc.animation(1);
            player.setNoDamageTicks(20);
        }
    }

    public double gettheKb(Player player, Member member) {
        if (player == null) {
            member.npckb++;
            if (member.npckb == 3)
                member.npckb = 0;
            if (member.npckb == 0)
                return 0.46;
            if (member.npckb == 1)
                return 0.38;
            if (member.npckb == 2)
                return 0.01;
        } else {
            member.kb++;
            if (member.kb == 3 || player.isOnGround())
                member.kb = 0;
            if (member.kb == 0)
                return player.getLocation().getY() < member.npc.entityPlayer.locY - 1 ? 0.01 : 0.46;
            if (member.kb == 1)
                return player.getLocation().getY() < member.npc.entityPlayer.locY - 1 ? 0.01 : 0.38;
            if (member.kb == 2)
                return 0.01;
        }
        return -1;
    }
}
