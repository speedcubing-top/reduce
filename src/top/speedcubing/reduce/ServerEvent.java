package top.speedcubing.reduce;

import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import top.speedcubing.lib.api.MojangAPI;
import top.speedcubing.lib.api.mojang.ProfileSkin;
import top.speedcubing.lib.bukkit.event.PlayInEvent;
import top.speedcubing.lib.eventbus.LibEventHandler;
import top.speedcubing.lib.utils.ByteArrayDataBuilder;
import top.speedcubing.lib.utils.Console;
import top.speedcubing.server.config;
import top.speedcubing.server.events.ConfigReloadEvent;
import top.speedcubing.server.events.SocketEvent;
import top.speedcubing.server.libs.GlobalString;
import top.speedcubing.server.share.Chat;
import top.speedcubing.server.speedcubingServer;

import java.io.IOException;

public class ServerEvent {
    @LibEventHandler
    public void socketEvent(SocketEvent e) {
        switch (e.header) {
            case "reduce":
                try {
                    int id = e.receive.readInt();
                    Reduce.mapCache.put(id, e.receive.readInt());
                    speedcubingServer.tcpClient.send(e.receive.readInt(), new ByteArrayDataBuilder().writeUTF("reduce").writeInt(id).toByteArray());
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
                        ProfileSkin skin = MojangAPI.getSkinByName(message);
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
                if (Chat.checkBlockedText(message))
                    return;
                String filtered = Chat.filter(message);
                String[] formats = speedcubingServer.getFormat(member.user.rank);
                String text = formats[0] + player.getName();
                String[] str = GlobalString.MessageFormat;
                String[] out = {
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + message),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + message)
                };
                String[] out2 = {
                        str[0].replace("%text%", text).replace("%text2%", formats[2] + filtered),
                        str[1].replace("%text%", text).replace("%text2%", formats[2] + filtered),
                        str[2].replace("%text%", text).replace("%text2%", formats[2] + filtered),
                        str[3].replace("%text%", text).replace("%text2%", formats[2] + filtered)
                };
                Chat.globalChat(Bukkit.getOnlinePlayers(), player, out, out2);
                Console.sendColoredConsole("§7[§aChatLog§7] [§b" + player.getWorld().getName() + "§7] " + out[1]);
            }
        }
    }
}
