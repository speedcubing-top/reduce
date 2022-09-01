package top.speedcubing.reduce.BukkitEventListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.speedcubing.reduce.Core.Reduce;
import top.speedcubing.reduce.Member;

import java.util.HashSet;

public class PlayerJoin implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Member member = new Member(player);
        Integer map = Reduce.mapCache.get(member.user.id);
        if (map == null) {
            player.sendMessage("§4Error while transporting map data. please contact speedcubing");
            map = 0;
        }
        Reduce.Rooms[map] += 1;
        Reduce.mapCache.remove(member.user.id);
        member.startGame(player, map, Reduce.Rooms[map], null, null, new HashSet<>(), null);
    }
}






