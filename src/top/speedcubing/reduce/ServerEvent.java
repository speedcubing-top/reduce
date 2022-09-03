package top.speedcubing.reduce;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import top.speedcubing.reduce.Core.Reduce;
import top.speedcubing.lib.api.SessionServer;
import top.speedcubing.lib.api.exception.APIErrorException;
import top.speedcubing.lib.bukkit.event.PlayInEvent;
import top.speedcubing.lib.eventbus.LibEventHandler;
import top.speedcubing.server.events.SocketEvent;
import top.speedcubing.server.libs.GlobalString;
import top.speedcubing.server.speedcubingServer;
import top.speedcubing.system.LocalMojang;

import java.util.UUID;

public class ServerEvent {
    @LibEventHandler
    public void socketEvent(SocketEvent e) {
        String[] rs = e.receive.split("\\|");
        switch (rs[0]) {
            case "reduce":
                Reduce.mapCache.put(Integer.parseInt(rs[1]), Integer.parseInt(rs[2]));
                speedcubingServer.tcp.send(Integer.parseInt(rs[3]), "reduce|" + rs[1]);
                break;
        }
    }

    @LibEventHandler
    public void PlayInEvent(PlayInEvent e) {
        if (e.packet instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.packet).a();
            Player player = e.player;
            Member member = Member.getMember(player);
            if (member.setSkin) {
                e.isCancelled = true;
                if (message.contains(" ") || message.length() > 16)
                    player.sendMessage(GlobalString.invalidName[member.user.lang]);
                else {
                    UUID id = null;
                    try {
                        id = LocalMojang.getUUID(message);
                    } catch (APIErrorException exception) {
                    }
                    if (id == null)
                        player.sendMessage("invalid name \"" + message + "\"");
                    else {
                        member.setSkin = false;
                        UUID finalId = id;
                        new Thread(() -> {
                            String[] a = SessionServer.getSkin(finalId);
                            if (member.npc != null)
                                member.setSkin(player, a[0], a[1]);
                        }).start();
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                }
            } else if (member.setName) {
                e.isCancelled = true;
                member.Holo.setName(ChatColor.translateAlternateColorCodes('&', message));
                member.setName = false;
                speedcubingServer.node(false, member.user.id, member.user.tcpPort);
            } else if (member.ReachSettings2) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 25000)
                        player.sendMessage("the number is too large / small !");
                    else {
                        member.ReachSettings2 = false;
                        member.integerCache[9] = x;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
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
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
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
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
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
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
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
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    player.sendMessage("Invalid format! (0.000 ~ 5.000)");
                }
            } else if (message.charAt(0) != '/') {
                e.isCancelled = true;
                String[] formats = speedcubingServer.getFormat(member.user.rank);
                String text = formats[0] + player.getName();
                String[] str = GlobalString.MessageFormat[0];
                String[] out = new String[]{
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                Bukkit.getOnlinePlayers().forEach(a -> a.sendMessage(out[Member.getMember(a).user.lang]));
            }
        }
    }
}
