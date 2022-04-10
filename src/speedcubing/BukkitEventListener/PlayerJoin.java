package speedcubing.BukkitEventListener;

import cubingserver.PluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import speedcubing.Core.Reduce;
import speedcubing.Member;

import java.util.*;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOnline()) {
            UUID uuid = player.getUniqueId();
            Integer map = Reduce.mapCache.get(uuid);
            boolean found = false;
            if (map == null)
                player.sendMessage("Error in the connection.");
            else if (map == -1) {
                for (int i = 0; i < Reduce.maptypes; i++) {
                    if (Reduce.used[i] != Reduce.rooms) {
                        map = i;
                        found = true;
                        break;
                    }
                }
            } else if (Reduce.used[map] == Reduce.rooms)
                player.sendMessage("There is no enough servers.");
            else found = true;
            if (!found) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PluginMessage.switchServer(player, "lobby");
                    }
                }, 500);
            } else {
                int room = -1;
                for (int h = 0; h < Reduce.rooms; h++) {
                    if (Reduce.Rooms[map][h]) {
                        room = h;
                        break;
                    }
                }
                Reduce.mapCache.remove(uuid);
                player.sendMessage("§4Keep in mind that the Spectator System is still under development.");
                new Member(player, map, room, null, null, new HashSet<>());
            }
        }
    }
}






