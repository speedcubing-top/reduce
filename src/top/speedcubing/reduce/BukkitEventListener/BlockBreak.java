package top.speedcubing.reduce.BukkitEventListener;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import top.speedcubing.reduce.Member;

public class BlockBreak implements Listener {
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            Block block = e.getBlock();
            Member member = Member.getMember(player);
            BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
            if (member.blocks.contains(position)) {
                member.blocks.remove(position);
                block.setType(Material.AIR);
            } else e.setCancelled(true);
        }
    }
}
