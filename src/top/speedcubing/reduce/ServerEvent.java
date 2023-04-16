package top.speedcubing.reduce;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import top.speedcubing.lib.api.MojangAPI;
import top.speedcubing.lib.api.mojang.ProfileSkin;
import top.speedcubing.lib.bukkit.event.PlayInEvent;
import top.speedcubing.lib.eventbus.LibEventHandler;
import top.speedcubing.lib.utils.Reflections;
import top.speedcubing.server.*;
import top.speedcubing.server.events.*;
import top.speedcubing.server.libs.GlobalString;
import top.speedcubing.server.mulitproxy.BungeeProxy;
import top.speedcubing.server.share.Chat;

import java.io.IOException;

public class ServerEvent {
    @LibEventHandler
    public void socketEvent(SocketEvent e) {
        switch (e.header) {
            case "reduce":
                try {
                    int id = e.receive.readInt();
                    Reduce.mapCache.put(id, e.receive.readInt());
                    BungeeProxy.switchServer(id, "reduce");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;
        }
    }

    @LibEventHandler
    public void ConfigReloadEvent(ConfigReloadEvent e) {
        Reduce.ReduceKB = new double[]{
                config.config.getAsJsonObject("reducekb").get("hor").getAsDouble(),
                config.config.getAsJsonObject("reducekb").get("ver").getAsDouble(),
                config.config.getAsJsonObject("reducekb").get("airhor").getAsDouble(),
                config.config.getAsJsonObject("reducekb").get("airver").getAsDouble(),
                config.config.getAsJsonObject("reducekb").get("reach").getAsDouble()
        };
    }

    @LibEventHandler
    public void PlayInEvent(PlayInEvent e) {
        if (e.packet instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.packet).a();
            Player player = e.player;
            Member member = Member.getMember(player);
            if (member.setSkin) {
                e.isCancelled = true;
                if (!speedcubingServer.nameRegex.matcher(message).matches())
                    member.user.sendLangMessage(GlobalString.invalidName);
                else {
                    member.setSkin = false;
                    try {
                        ProfileSkin skin = MojangAPI.getDetailByName(message);
                        new Thread(() -> {
                            if (member.npc != null)
                                member.setSkin(player, skin.getValue(), skin.getSignature());
                        }).start();
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    } catch (Exception exception) {
                        member.user.sendLangMessage(GlobalString.invalidName);
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
                        member.user.langMessageSender(GlobalString.integerOutOfRange).replace("%min%", "0.000").replace("%max%", "25.000").send();
                    else {
                        member.ReachSettings2 = false;
                        member.integerCache[9] = x;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (member.horSettings2) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.langMessageSender(GlobalString.integerOutOfRange).replace("%min%", "0.000").replace("%max%", "5.000").send();
                    else {
                        member.horSettings2 = false;
                        member.integerCache[10] = x;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (member.ReachSettings) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 25000)
                        member.user.langMessageSender(GlobalString.integerOutOfRange).replace("%min%", "0.000").replace("%max%", "25.000").send();
                    else {
                        member.ReachSettings = false;
                        member.integerCache[5] = x;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (member.horSettings) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.langMessageSender(GlobalString.integerOutOfRange).replace("%min%", "0.000").replace("%max%", "5.000").send();
                    else {
                        member.horSettings = false;
                        member.integerCache[6] = x;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (member.verSettings) {
                e.isCancelled = true;
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.langMessageSender(GlobalString.integerOutOfRange).replace("%min%", "0.000").replace("%max%", "5.000").send();
                    else {
                        member.verSettings = false;
                        member.integerCache[7] = x;
                        speedcubingServer.node(false, member.user.id, member.user.tcpPort);
                    }
                } catch (Exception exception) {
                    member.user.sendLangMessage(GlobalString.invalidNumber);
                }
            } else if (message.charAt(0) != '/') {
                e.isCancelled = true;
                String[] formats = member.user.getFormat();
                Chat.globalChat(Bukkit.getOnlinePlayers(), player, GlobalString.MessageFormat, message, formats[0] + player.getName(), formats[2]);
            }
        } else if (e.packet instanceof PacketPlayInUseEntity) {
            if (e.player.getGameMode() == GameMode.ADVENTURE && ((PacketPlayInUseEntity) e.packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                Member member = Member.getMember(e.player);
                if (!member.isLockedOnSomeone) {
                    if (member.target.user.player.getEntityId() == (int) Reflections.getField(e.packet, "a")) {
                        PlayerConnection connection = ((CraftPlayer) e.player).getHandle().playerConnection;
                        connection.sendPacket(new PacketPlayOutGameStateChange(3, 3));
                        connection.sendPacket(new PacketPlayOutCamera(member.target.user.toNMS()));
                        PacketPlayOutAbilities abilities = new PacketPlayOutAbilities();
                        abilities.b(true);
                        abilities.c(true);
                        connection.sendPacket(abilities);
//                        e.player.teleport(new Location(e.player.getWorld(), member.coord, 97, 0, 0, 0));
                        member.isLockedOnSomeone = true;
                    }
                }
            }
        }
    }
}
