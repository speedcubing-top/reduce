package speedcubing.BukkitEventListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import speedcubing.Core.Reduce;
import speedcubing.Member;

import java.util.HashSet;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Member member = new Member(player);
        Integer map = Reduce.mapCache.get(member.id);
        if (map == null) {
            player.sendMessage("§4Error while transporting map data. please contact speedcubing");
            map = 0;
        }
        int room = 1;
        while (Reduce.Rooms[map].contains(room)) {
            room++;
        }
        Reduce.Rooms[map].add(room);
        Reduce.mapCache.remove(member.id);
        member.startGame(player, map, room, null, null, new HashSet<>(), null);
    }
}






