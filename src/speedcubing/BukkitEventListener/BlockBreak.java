package speedcubing.BukkitEventListener;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import speedcubing.Core.Reduce;

import java.util.UUID;

public class BlockBreak implements Listener {
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL){
            Block block = e.getBlock();
            UUID uuid = player.getUniqueId();
            BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
            if (Reduce.blocks.get(uuid).contains(position)) {
                Reduce.blocks.get(uuid).remove(position);
                block.setType(Material.AIR);
            }
        }
    }
}
