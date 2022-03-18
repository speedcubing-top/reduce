package speedcubing.BukkitEventListener;


import cubing.spigot.lib.bukkit.InventoryUtils;
import cubingserver.StringList.GlobalString;
import cubingserver.libs.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;

import java.util.UUID;

public class InventoryClose implements Listener {
    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (e.getInventory().getHolder() == ItemStackList.KitEditorHolder && Reduce.kitedit.contains(uuid)) {
            Reduce.kitedit.remove(uuid);
            player.sendMessage(GlobalString.UnSavedSettings[User.getLang(uuid)]);
            InventoryUtils.fillItem(player.getInventory(), 9, 36, new ItemStack(Material.AIR));
            player.updateInventory();
        }
    }
}
