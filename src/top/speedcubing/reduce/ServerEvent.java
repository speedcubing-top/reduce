package top.speedcubing.reduce;

import java.io.IOException;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import top.speedcubing.common.configuration.ServerConfig;
import top.speedcubing.common.events.ConfigReloadEvent;
import top.speedcubing.common.events.SocketReadEvent;
import top.speedcubing.common.rank.RankFormat;
import top.speedcubing.lib.api.MojangAPI;
import top.speedcubing.lib.api.mojang.ProfileSkin;
import top.speedcubing.lib.bukkit.events.packet.PlayInEvent;
import top.speedcubing.lib.eventbus.CubingEventHandler;
import top.speedcubing.lib.utils.ReflectionUtils;
import top.speedcubing.lib.utils.internet.HostAndPort;
import top.speedcubing.server.events.player.NickEvent;
import top.speedcubing.server.events.player.SkinEvent;
import top.speedcubing.server.events.player.ToggleFlyEvent;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.mulitproxy.BungeeProxy;
import top.speedcubing.server.share.Chat;
import top.speedcubing.server.speedcubingServer;

public class ServerEvent {

    @CubingEventHandler
    public void configReloadEvent(ConfigReloadEvent e) {
        Reduce.ReduceKB = new double[]{
                ServerConfig.config.getAsJsonObject("reducekb").get("hor").getAsDouble(),
                ServerConfig.config.getAsJsonObject("reducekb").get("ver").getAsDouble(),
                ServerConfig.config.getAsJsonObject("reducekb").get("airhor").getAsDouble(),
                ServerConfig.config.getAsJsonObject("reducekb").get("airver").getAsDouble(),
                ServerConfig.config.getAsJsonObject("reducekb").get("reach").getAsDouble()
        };
    }

    @CubingEventHandler
    public void playInEvent(PlayInEvent e) {
        if (e.getPacket() instanceof PacketPlayInChat) {
            String message = ((PacketPlayInChat) e.getPacket()).a();
            Player player = e.getPlayer();
            Member member = Member.getMember(player);
            if (member.setSkin) {
                e.setCancelled(true);
                if (!speedcubingServer.nameRegex.matcher(message).matches())
                    member.user.sendMessage("%lang_general_invaid_name%");
                else {
                    member.setSkin = false;
                    speedcubingServer.scheduledPool.execute(() -> {
                        try {
                            ProfileSkin skin = MojangAPI.getSkinByName(message);
                            if (skin == null)
                                member.user.sendMessage("%lang_general_invaid_name%");
                            else {
                                if (member.npc != null)
                                    member.setSkin(player, skin.getSkin().getValue(), skin.getSkin().getSignature());
                            }
                        } catch (IOException ignored) {
                        }
                    });
                    member.user.setInput(false);
                }
            } else if (member.setName) {
                e.setCancelled(true);
                member.Holo.setName(ChatColor.translateAlternateColorCodes('&', message));
                member.setName = false;
                member.user.setInput(false);
            } else if (member.ReachSettings2) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 25000)
                        member.user.sendMessage("%lang_input_number_out%", "0.000", "25.000");
                    else {
                        member.ReachSettings2 = false;
                        member.integerCache[9] = x;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendMessage("%lang_input_invalid_number%");
                }
            } else if (member.horSettings2) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.sendMessage("%lang_input_number_out%", "0.000", "5.000");
                    else {
                        member.horSettings2 = false;
                        member.integerCache[10] = x;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendMessage("%lang_input_invalid_number%");
                }
            } else if (member.ReachSettings) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 25000)
                        member.user.sendMessage("%lang_input_number_out%", "0.000", "25.000");
                    else {
                        member.ReachSettings = false;
                        member.integerCache[5] = x;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendMessage("%lang_input_invalid_number%");
                }
            } else if (member.horSettings) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.sendMessage("%lang_input_number_out%", "0.000", "5.000");
                    else {
                        member.horSettings = false;
                        member.integerCache[6] = x;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendMessage("%lang_input_invalid_number%");
                }
            } else if (member.verSettings) {
                e.setCancelled(true);
                try {
                    int x = (int) (Double.parseDouble(message) * 1000);
                    if (x < 0 || x > 5000)
                        member.user.sendMessage("%lang_input_number_out%", "0.000", "5.000");
                    else {
                        member.verSettings = false;
                        member.integerCache[7] = x;
                        member.user.setInput(false);
                    }
                } catch (Exception exception) {
                    member.user.sendMessage("%lang_input_invalid_number%");
                }
            } else if (message.charAt(0) != '/') {
                e.setCancelled(true);
                RankFormat format = member.user.getFormat(false);
                Chat.globalChat(player.getWorld().getPlayers(), player, Lang.of("%lang_message_format%"), message, format.getPrefix() + player.getName(), format.getChatColor());
            }
        } else if (e.getPacket() instanceof PacketPlayInUseEntity) {
            if (e.getPlayer().getGameMode() == GameMode.ADVENTURE && ((PacketPlayInUseEntity) e.getPacket()).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                Member member = Member.getMember(e.getPlayer());
                if (!member.isLockedOnSomeone) {
                    if (member.target.user.player.getEntityId() == (int) ReflectionUtils.getField(e.getPacket(), "a")) {
                        PlayerConnection connection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
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

    @CubingEventHandler
    public void socketReadEvent(SocketReadEvent e) {
        switch (e.getPacketID()) {
            case "reduce":
                try {
                    int id = e.getData().readInt();
                    int map = e.getData().readInt();
                    String host = e.getData().readUTF();
                    int port = e.getData().readInt();
                    Reduce.mapCache.put(id, map);
                    System.out.println(id + " " + map + " " + host + " " + port);
                    BungeeProxy.switchServer(id, "reduce", new HostAndPort(host, port));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;
        }
    }

    @CubingEventHandler
    public void nickEvent(NickEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void skinEvent(SkinEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }

    @CubingEventHandler
    public void toggleFlyEvent(ToggleFlyEvent e) {
        e.setCancelled(true);
        Member.getMember(e.getPlayer()).user.sendMessage("%lang_general_lobby_only%");
    }
}
