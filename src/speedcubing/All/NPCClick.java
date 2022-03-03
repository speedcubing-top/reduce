package speedcubing.All;

import cubingserver.speedcubingServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import speedcubing.Core.Reduce;
import speedcubing.Main;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NPCClick {

    public void PacketInject(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;

        if (channel.pipeline().get("PacketPlayOutInUseEntity") != null)
            return;
        channel.pipeline().addAfter("decoder", "PacketPlayInUseEntity", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInUseEntity packet, List<Object> arg) {
                arg.add(packet);
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
        });
    }
}
