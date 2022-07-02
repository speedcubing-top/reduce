package speedcubing.BukkitEventListener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import speedcubing.Core.Reduce;
import speedcubing.Member;

import java.util.UUID;

public class PlayerItemHeld implements Listener {
    @EventHandler
    public void PlayerItemHeldEvent(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (player.getLocation().getY() >= 90 && player.getGameMode() == GameMode.SURVIVAL) {
            ItemStack itemStack = player.getInventory().getItem(e.getNewSlot());
            if (itemStack != null && itemStack.getType() == Material.INK_SACK)
                Member.getMember(player).stopDamage();
        }
    }
}