package top.speedcubing.reduce.BukkitEventListener;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import top.speedcubing.reduce.Core.Reduce;
import top.speedcubing.reduce.Member;
import top.speedcubing.lib.bukkit.WorldUtils;
import top.speedcubing.server.speedcubingServer;

public class PlayerQuit implements Listener {

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        boolean spec = player.getGameMode() == GameMode.ADVENTURE;
        Member member = Member.getMember(player);
        if (spec)
            Reduce.quitSpec(player, ((CraftPlayer) player).getHandle().playerConnection, false);
        else {
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
                        ",reduce_vanilla_reach=" + ints[9] +
                        ",reduce_vanilla_hor=" + ints[10] +
                        ",reduce_npccolor=" + (bs[0] ? 1 : 0) +
                        ",reduce_npcsound=" + (bs[1] ? 1 : 0) +
                        ",reduce_npcswing=" + (bs[2] ? 1 : 0) +
                        ",reduce_vanilla=" + (bs[3] ? 1 : 0)
                , "id=" + member.user.id)).start();
        if (!spec)
            Reduce.removeFromGUI(member, member.map, 0);
        member.npc.despawn();
        member.npc.delete();
        player.teleport(new Location(player.getWorld(), 0.5, 1000, 0.5, 0, 0));
        Member.users.remove(member.user.id);
    }
}
