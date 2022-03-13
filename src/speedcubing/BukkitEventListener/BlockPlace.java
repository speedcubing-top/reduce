package speedcubing.BukkitEventListener;

import cubingserver.libs.User;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;

import java.util.UUID;

public class BlockPlace implements Listener {
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            Block block = e.getBlock();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            UUID uuid = player.getUniqueId();
            if (y < 85 || Math.abs(z - Reduce.PlayerCoord.get(uuid)) > 150 || Math.abs(x - Reduce.PlayerMap.get(uuid) * 500) > 150)
                e.setCancelled(true);
            else {
                int lang = User.getLang(uuid);
                int a = Reduce.PlacedBlocks.get(uuid) + 1;
                int b = Reduce.BlockPlaced.get(uuid);
                Reduce.PlacedBlocks.put(uuid, a);
                if (a > 32) {
                    Reduce.PlacedBlocks.put(uuid, 0);
                    Integer[] ints = Reduce.integerCache.get(uuid);
                    player.getInventory().setItem(ints[1], ItemStackList.Blocks[ints[4]][lang]);
                }
                Reduce.blocks.get(uuid).add(new BlockPosition(x, y, z));
                Reduce.BlockPlaced.put(uuid, b + 1);
                player.getScoreboard().getTeam(StringList.ReduceSidebar[lang][1]).setSuffix(Integer.toString(b + 1));
            }
        }
    }
}
