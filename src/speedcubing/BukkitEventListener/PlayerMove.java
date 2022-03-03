package speedcubing.BukkitEventListener;

import cubing.bukkit.PlayerUtils;
import cubingserver.libs.PlayerData;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PlayerMove implements Listener {
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location to = e.getTo();
        UUID uuid = player.getUniqueId();
        if (player.getGameMode() == GameMode.ADVENTURE) {
            if (Math.abs(to.getZ() - Reduce.PlayerCoord.get(uuid) - 0.5) > 149.5 || Math.abs(to.getX() - Reduce.PlayerMap.get(uuid) * 500 - 0.5) > 149.5)
                player.teleport(e.getFrom());
            else if (to.getY() < 85)
                PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, Reduce.PlayerMap.get(uuid) * 500 + 0.5, 100, Reduce.PlayerCoord.get(uuid) + 0.5, 0, 0);
        } else if (player.getGameMode() == GameMode.SURVIVAL)
            Reduce.Aim(player, Reduce.Spectators.get(uuid));
        if (to.getY() < 85 || Math.abs(to.getZ() - Reduce.PlayerCoord.get(uuid) - 0.5) > 149.5 || Math.abs(to.getX() - Reduce.PlayerMap.get(uuid) * 500 - 0.5) > 149.5) {
            int lang = PlayerData.getLang(uuid);
            int map = Reduce.PlayerMap.get(uuid);
            double NPCx = map * 500 + 0.5;
            double NPCz = Reduce.PlayerCoord.get(uuid) + 0.5;
            World world = player.getWorld();
            Integer[] ints = Reduce.integerCache.get(uuid);
            player.getScoreboard().getTeam(StringList.ReduceSidebar[lang][1]).setSuffix("0");
            Reduce.StopDamage(uuid);
            int[] m = Reduce.MapLocations[map];
            PlayerUtils.teleport(((CraftPlayer) player).getHandle().playerConnection, NPCx - m[0], 100 + m[1], NPCz, 270, 0);
            player.getInventory().setItem(ints[1], ItemStackList.Blocks[ints[4]][lang]);
            Reduce.BlockPlaced.put(uuid, 0);
            for (BlockPosition b : Reduce.blocks.get(uuid)) {
                world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
            }
            Reduce.blocks.get(uuid).clear();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    EntityPlayer entityPlayer = Reduce.EntityPlayers.get(uuid);
                    if (entityPlayer != null)
                        Reduce.teleport(NPCx, 100, NPCz, 90, 0, ((CraftPlayer) player).getHandle().playerConnection, uuid, Reduce.Spectators.get(uuid));
                }
            }, 50);
        }
    }
}

