package speedcubing.BukkitEventListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import speedcubing.Core.Reduce;
import speedcubing.Member;
import speedcubing.server.PluginMessage;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOnline()) {
            UUID uuid = player.getUniqueId();
            Integer map = Reduce.mapCache.get(uuid);
            if (map == null) {
                player.sendMessage("Error in the connection.");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PluginMessage.switchServer(player, "lobby");
                    }
                }, 500);
                return;
            }
            int room = -1;
            for (int h = 0; h < Integer.MAX_VALUE; h++) {
                if (Reduce.Rooms[map].size() == h)
                    Reduce.Rooms[map].add(true);
                if (Reduce.Rooms[map].get(h)) {
                    room = h + 1;
                    break;
                }
            }
            Reduce.mapCache.remove(uuid);
            new Member(player, map, room, null, null, new HashSet<>());
        }
    }
}






