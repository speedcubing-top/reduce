package speedcubing.BukkitEventListener;

import speedcubing.server.libs.User;
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
import speedcubing.Member;

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
            Member member = Member.getMember(player.getUniqueId());
            if (y < 85 || Math.abs(z - member.Coord) > 150 || Math.abs(x - member.Map * 500) > 150)
                e.setCancelled(true);
            else {
                int a = member.PlacedBlocks + 1;
                int b = member.BlockPlaced;
                member.PlacedBlocks = a;
                if (a > 32) {
                    member.PlacedBlocks = 0;
                    Integer[] ints = member.integerCache;
                    player.getInventory().setItem(ints[1], ItemStackList.Blocks[ints[4]][member.lang]);
                }
                member.blocks.add(new BlockPosition(x, y, z));
                member.BlockPlaced = b + 1;
                player.getScoreboard().getTeam(StringList.ReduceSidebar[member.lang][1]).setSuffix(Integer.toString(b + 1));
            }
        }
    }
}
