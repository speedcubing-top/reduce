package speedcubing.All;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cubing.api.MojangAPI;
import cubing.api.SessionServer;
import cubing.api.exception.APIErrorException;
import cubing.utils.Console;
import cubing.utils.Reflections;
import cubingserver.StringList.GlobalString;
import cubingserver.libs.PlayerData;
import cubingserver.libs.Rank;
import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;

import java.util.*;

public class Chat {

    public void PacketInject(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        if (channel.pipeline().get("PacketPlayInChat") != null)
            return;
        channel.pipeline().addAfter("decoder", "PacketPlayInChat", new MessageToMessageDecoder<PacketPlayInChat>() {
            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInChat packet, List<Object> arg) {
                String message = packet.a();
                UUID uuid = player.getUniqueId();
                if (Reduce.setSkin.contains(uuid)) {
                    if (message.contains(" ") || message.length() > 16)
                        player.sendMessage(GlobalString.invalidName[PlayerData.getLang(uuid)]);
                    else {
                        String id;
                        try {
                            id = MojangAPI.getUUID(message);
                        } catch (APIErrorException e) {
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
                    } catch (Exception e) {
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
                    } catch (Exception e) {
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
                    } catch (Exception e) {
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
                } else arg.add(packet);
                speedcubingServer.spam.put(uuid, time);
            }
        });
    }
}