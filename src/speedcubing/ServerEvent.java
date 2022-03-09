package speedcubing;

import cubing.api.MojangAPI;
import cubing.api.SessionServer;
import cubing.api.exception.APIErrorException;
import cubing.bukkit.Event.ServerEventHandler;
import cubing.bukkit.Event.events.PlayInChatEvent;
import cubing.bukkit.Event.events.PlayInUseEntityEvent;
import cubing.bukkit.PlayerUtils;
import cubing.utils.Console;
import cubingserver.StringList.GlobalString;
import cubingserver.connection.SocketUtils;
import cubingserver.customEvents.SocketEvent;
import cubingserver.customEvents.UDPEvent;
import cubingserver.libs.PlayerData;
import cubingserver.libs.Rank;
import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import speedcubing.All.configcache;
import speedcubing.Core.Reduce;

import java.util.Set;
import java.util.UUID;

public class ServerEvent {
    @ServerEventHandler
    public void socketEvent(SocketEvent e) {
        String[] rs = e.rs;
        if (rs != null && rs[0].equals("d")) {
            Reduce.PlayerMap.put(UUID.fromString(rs[1]), Integer.parseInt(rs[2]));
            SocketUtils.sendData(25576 - Bukkit.getPort() % 2, "d|" + rs[1], 100);
        }
    }

    @ServerEventHandler
    public void UDPEvent(UDPEvent e) {
        String[] rs = e.rs;
        if (rs[0].equals("o") && rs[1].equals("1")) {
            speedcubingServer.AllPlayers = Integer.parseInt(rs[2]);
            for (Player p : Bukkit.getOnlinePlayers()) {
                int lang = PlayerData.getLang(p.getUniqueId());
                PlayerUtils.sendTabHeaderFooter(((CraftPlayer) p).getHandle().playerConnection, GlobalString.LobbyTabList[0][lang], GlobalString.LobbyTabList[1][lang].replace("%int%", rs[2]));
            }
        }
    }

    @ServerEventHandler
    public void PlayInChatEvent(PlayInChatEvent e){
        e.setCancelled(true);
        Player player = e.player;
        String message = e.packet.a();
        UUID uuid = player.getUniqueId();
        if (Reduce.setSkin.contains(uuid)) {
            if (message.contains(" ") || message.length() > 16)
                player.sendMessage(GlobalString.invalidName[PlayerData.getLang(uuid)]);
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
                    Reduce.setSkin.remove(uuid);
                    String finalId = id;
                    new Thread(() -> {
                        EntityPlayer entityPlayer = Reduce.EntityPlayers.get(uuid);
                        if (entityPlayer == null)
                            return;
                        Reduce.setSkin(player, entityPlayer, uuid, SessionServer.getSkin(finalId));
                    }).start();
                }
            }
            return;
        } else if (Reduce.setName.contains(uuid)) {
            EntityArmorStand stand = Reduce.Holo.get(uuid);
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
            Reduce.sendPackets(((CraftPlayer) player).getHandle().playerConnection, Reduce.Spectators.get(uuid), new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
            Reduce.setName.remove(uuid);
            return;
        } else if (Reduce.ReachSettings.contains(uuid)) {
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 25000)
                    player.sendMessage("the number is too large / small !");
                else {
                    Reduce.ReachSettings.remove(uuid);
                    Reduce.integerCache.get(uuid)[5] = x;
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 25.000)");
            }
            return;
        } else if (Reduce.horSettings.contains(uuid)) {
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    Reduce.horSettings.remove(uuid);
                    Reduce.integerCache.get(uuid)[6] = x;
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
            return;
        } else if (Reduce.verSettings.contains(uuid)) {
            try {
                int x = (int) (Double.parseDouble(message) * 1000);
                if (x < 0 || x > 5000)
                    player.sendMessage("the number is too large / small !");
                else {
                    Reduce.verSettings.remove(uuid);
                    Reduce.integerCache.get(uuid)[7] = x;
                }
            } catch (Exception exception) {
                player.sendMessage("Invalid format! (0.000 ~ 5.000)");
            }
            return;
        }
        long time = System.currentTimeMillis();
        boolean spam = PlayerJoin.Canspam.contains(uuid);
        if (!spam && time - speedcubingServer.spam.get(uuid) < 500)
            player.sendMessage(GlobalString.spam[PlayerData.getLang(uuid)]);
        else if (message.charAt(0) != '/') {
            if (!spam && speedcubingServer.lastmsg.get(uuid).equals(message))
                player.sendMessage(GlobalString.samemsg[PlayerData.getLang(uuid)]);
            else {
                String[] formats = Rank.format(uuid);
                String text = formats[0] + formats[1] + player.getName();
                String[] str = GlobalString.MessageFormat[0];
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(out[PlayerData.getLang(p.getUniqueId())]);
                }
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
                speedcubingServer.lastmsg.put(uuid, message);
            }
        } else e.setCancelled(false);
        speedcubingServer.spam.put(uuid, time);
    }

    @ServerEventHandler
    public void PlayInUseEntityEvent(PlayInUseEntityEvent e) {
        Player player = e.player;
        PacketPlayInUseEntity packet = e.packet;
        if (packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) && player.getNoDamageTicks() < player.getMaximumNoDamageTicks() / 2 && player.getGameMode().equals(GameMode.SURVIVAL)) {
            UUID uuid = player.getUniqueId();
            ItemStack hand = player.getItemInHand();
            Boolean[] bs = Reduce.booleanCache.get(uuid);
            EntityPlayer entityplayer = ((CraftPlayer) player).getHandle();
            PlayerConnection connection = entityplayer.playerConnection;
            Set<PlayerConnection> spec = Reduce.Spectators.get(uuid);
            int map = Reduce.PlayerMap.get(uuid);
            if ((hand == null || hand.getType() != Material.INK_SACK) && !Reduce.DamageTask.containsKey(uuid)) {
                Reduce.DamageTask.put(uuid, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                    Location location = player.getLocation();
                    double xDiff = location.getX() - map * 500 - 0.5;
                    double zDiff = location.getZ() - Reduce.PlayerCoord.get(uuid) - 0.5;
                    double yDiff = player.getEyeLocation().getY() - 101.62;
                    Integer[] settings = Reduce.integerCache.get(uuid);
                    if (Math.sqrt(xDiff * xDiff + zDiff * zDiff + yDiff * yDiff) <= configcache.ReduceKB[4] * settings[5] / 1000) {
                        double[] value = configcache.ReduceKB;
                        double hor = player.isOnGround() ? value[0] : value[2];
                        double horizontalDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                        PacketPlayOutAnimation animation = new PacketPlayOutAnimation(Reduce.EntityPlayers.get(uuid), (byte) 0);
                        double x = xDiff * hor / horizontalDistance * settings[6] / 1000;
                        double y = (player.isOnGround() ? value[1] : value[3]) * settings[7] / 1000;
                        double z = zDiff * hor / horizontalDistance * settings[6] / 1000;
                        if (speedcubingServer.velocities.containsKey(uuid)) {
                            Double[] d = speedcubingServer.velocities.get(uuid);
                            x = x * d[0];
                            y = y * d[1];
                            z = z * d[0];
                        }
                        connection.sendPacket(new PacketPlayOutEntityVelocity(player.getEntityId(), x, y, z));
                        Reduce.sendPackets(connection, spec,
                                new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), location.getX(), location.getY(), location.getZ(), 1, 1),
                                animation,
                                new PacketPlayOutAnimation(entityplayer, 1));
                        if (Reduce.booleanCache.get(uuid)[2])
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                                if (Reduce.Spectators.containsKey(uuid))
                                    Reduce.sendPackets(connection, spec, animation);
                            }, 5);
                    }
                }, 0, 10));
            }
            if (bs[1])
                Reduce.sendPackets(connection, spec, new PacketPlayOutNamedSoundEffect(CraftSound.getSound(Sound.HURT_FLESH), map * 250 + 0.5, 101.62, Reduce.PlayerCoord.get(uuid) + 0.5, 1, 1));
            if (bs[0])
                Reduce.sendPackets(connection, spec, new PacketPlayOutAnimation(Reduce.EntityPlayers.get(uuid), (byte) 1));
            player.setNoDamageTicks(20);
        }
    }
}
