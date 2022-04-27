package speedcubing.BukkitEventListener;


import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.utils.Console;
import speedcubing.server.speedcubingServer;
import speedcubing.server.things.events.PlayerJoin;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import speedcubing.Core.Reduce;
import speedcubing.Member;

import java.util.Arrays;
import java.util.UUID;

public class PlayerQuit implements Listener {
    @EventHandler
    public void dww(PlayerKickEvent e){
        Console.sendColoredConsole("§4KICK");
    }
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Console.sendColoredConsole("§4QUIT");
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (PlayerJoin.JoinPackets.containsKey(uuid) && Member.users.containsKey(uuid)) {
            boolean spec = player.getGameMode() == GameMode.ADVENTURE;
            Member member = Member.getMember(uuid);
            if (spec) {
                Reduce.quitSpec(player, ((CraftPlayer) player).getHandle().playerConnection, false);
                Reduce.used[member.oldMap] -= 1;
                Reduce.Rooms[member.oldMap][Reduce.coordToRoom(member.oldCoord)] = true;
            } else {
                member.Spectators.forEach(a -> Reduce.quitSpec(null, a, true));
                member.Spectators.clear();
                if (member.task != -1)
                    Bukkit.getScheduler().cancelTask(member.task);
                member.clearBlocks();
            }
            Integer[] ints = member.integerCache;
            Boolean[] bs = member.booleanCache;
            new Thread(() -> speedcubingServer.connection.update("playersdata",
                    "reduce_stick=" + ints[0] +
                            ",reduce_block=" + ints[1] +
                            ",reduce_setting=" + ints[2] +
                            ",reduce_stickskin=" + ints[3] +
                            ",reduce_blockskin=" + ints[4] +
                            ",reduce_reach=" + ints[5] +
                            ",reduce_hor=" + ints[6] +
                            ",reduce_ver=" + ints[7] +
                            ",reduce_spec=" + ints[8] +
                            ",reduce_npccolor=" + (bs[0] ? 1 : 0) +
                            ",reduce_npcsound=" + (bs[1] ? 1 : 0) +
                            ",reduce_npcswing=" + (bs[2] ? 1 : 0)
                    , "uuid='" + uuid + "'")).start();
            if (!spec) {
                Reduce.removeFromGUI(player, member.Map, null);
                Console.sendColoredConsole("§bc §6"+player.getName()+" "+member.Map+" "+null);
                Console.sendColoredConsole("§c"+ Arrays.toString(Reduce.specName));
                Console.sendColoredConsole("§c"+Arrays.toString(Reduce.specStorage));
                Console.sendColoredConsole("§c"+Reduce.totalName);
                Reduce.used[member.Map] -= 1;
                Reduce.Rooms[member.Map][Reduce.coordToRoom(member.Coord)] = true;
            }
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, member.EntityPlayers));
            PlayerUtils.teleport(connection, 0.5, 1000, 0.5, 0, 0);
            Member.users.remove(uuid);
        } else Reduce.mapCache.remove(uuid);
    }
}
