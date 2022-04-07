package speedcubing.BukkitEventListener;

import cubing.lib.bukkit.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Member;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerMove implements Listener {
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        Member member = Member.users.get(player.getUniqueId());
        if (player.getGameMode() == GameMode.ADVENTURE) {
            if (Math.abs(to.getZ() - member.Coord - 0.5) > 149.5 || Math.abs(to.getX() - member.Map * 500 - 0.5) > 149.5)
                player.teleport(e.getFrom());
            else if (to.getY() < 85)
                PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, member.Map * 500 + 0.5, 100, member.Coord + 0.5, 0, 0);
        } else if (player.getGameMode() == GameMode.SURVIVAL)
            member.Aim(player);
        if (to.getY() < 85 || Math.abs(to.getZ() - member.Coord - 0.5) > 149.5 || Math.abs(to.getX() - member.Map * 500 - 0.5) > 149.5) {
            int map = member.Map;
            double NPCx = map * 500 + 0.5;
            double NPCz = member.Coord + 0.5;
            player.getScoreboard().getTeam(StringList.ReduceSidebar[member.lang][1]).setSuffix("0");
            member.stopDamage();
            int[] m = Reduce.MapLocations[map];
            PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, NPCx - m[0], 100 + m[1], NPCz, 270, 0);
            player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[member.integerCache[4]][member.lang]);
            member.BlockPlaced = 0;
            member.clearBlocks();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    EntityPlayer entityPlayer = member.EntityPlayers;
                    if (entityPlayer != null)
                        member.teleport(NPCx, 100, NPCz, 90, 0, ((CraftPlayer) player).getHandle().playerConnection);
                }
            }, 50);
        }
    }
}

