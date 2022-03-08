package speedcubing.BukkitEventListener;

import cubing.bukkit.api.BungeePluginMessage;
import cubing.utils.Console;
import cubing.utils.Reflections;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import speedcubing.Core.Reduce;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOnline()) {
            UUID uuid = player.getUniqueId();
            Integer map = Reduce.PlayerMap.get(uuid);
            if (map == null || Reduce.used[map] == Reduce.rooms) {
                if (map == null)
                    player.sendMessage("Error in the connection.");
                else
                    player.sendMessage("There is no enough servers.");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Reduce.PlayerMap.remove(uuid);
                        BungeePluginMessage.switchServer(player, "lobby");
                    }
                }, 500);
                return;
            }
            Reflections.setField(((CraftPlayer) player).getHandle(), "invulnerableTicks", 0);
            int room = -1;
            for (int h = 0; h < Reduce.rooms; h++) {
                if (Reduce.Rooms[map][h]) {
                    room = h;
                    break;
                }
            }
            player.sendMessage("§4Keep in mind that the Spectator System is still under development.");
            Console.sendColoredConsole("§8[§bReduce§8] §a " + player.getName() + "§b joined in §aroom=" + room + ",map=" + map);
            Reduce.GameStart(map, player, uuid, room, null);
        }
    }
}






