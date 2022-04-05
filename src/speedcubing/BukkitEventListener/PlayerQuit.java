package speedcubing.BukkitEventListener;


import cubing.lib.bukkit.PlayerUtils;
import cubing.lib.utils.Console;
import cubingserver.speedcubingServer;
import cubingserver.things.events.PlayerJoin;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import speedcubing.Core.Reduce;

import java.util.UUID;

public class PlayerQuit implements Listener {
    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (PlayerJoin.JoinPackets.containsKey(uuid) && Reduce.PlayerMap.containsKey(uuid)) {
            Console.sendColoredConsole("§8[§bReduce§8] §a " + player.getName() + "§b disconnected.");
            boolean spec = player.getGameMode() == GameMode.ADVENTURE;
            if (spec) {
                Reduce.quitSpec(player, ((CraftPlayer) player).getHandle().playerConnection, false);
            } else {
                Reduce.Spectators.get(uuid).forEach(a -> Reduce.quitSpec(null, a, true));
                Reduce.Spectators.get(uuid).clear();
                if (Reduce.DamageTask.containsKey(uuid)) {
                    Bukkit.getScheduler().cancelTask(Reduce.DamageTask.get(uuid));
                    Reduce.DamageTask.remove(uuid);
                }
                Reduce.clear(uuid);
                Reduce.blocks.remove(uuid);
            }
            int room = Reduce.coordToRoom(Reduce.PlayerCoord.get(uuid));
            int map = Reduce.PlayerMap.get(uuid);
            Reduce.Spectators.remove(uuid);
            Reduce.Holo.remove(uuid);
            Reduce.PlacedBlocks.remove(uuid);
            Reduce.setSkin.remove(uuid);
            Reduce.setName.remove(uuid);
            Reduce.ReachSettings.remove(uuid);
            Reduce.horSettings.remove(uuid);
            Reduce.verSettings.remove(uuid);
            Reduce.BlockPlaced.remove(uuid);
            Reduce.SideBar.get(uuid).cancel();
            Reduce.SideBar.remove(uuid);
            Reduce.oldCoord.remove(uuid);
            Reduce.oldMap.remove(uuid);
            Integer[] ints = Reduce.integerCache.get(uuid);
            Boolean[] bs = Reduce.booleanCache.get(uuid);
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
            if (!spec)
                Reduce.removeFromGUI(player.getName(), uuid, map, null);
            Reduce.booleanCache.remove(uuid);
            Reduce.integerCache.remove(uuid);
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, Reduce.EntityPlayers.get(uuid)));
            Reduce.EntityPlayers.remove(uuid);
            Reduce.PlayerMap.remove(uuid);
            Reduce.used[map] -= 1;
            Reduce.Rooms[map][room] = true;
            Reduce.PlayerCoord.remove(uuid);
            PlayerUtils.teleport(connection, 0.5, 1000, 0.5, 0, 0);
        }
    }
}
