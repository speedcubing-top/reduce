package speedcubing.BukkitEventListener;


import speedcubing.lib.bukkit.InventoryUtils;
import speedcubing.server.libs.GlobalString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import speedcubing.List.ItemStackList;
import speedcubing.Member;

public class InventoryClose implements Listener {
    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Member member = Member.getMember(player.getUniqueId());
        if (e.getInventory().getHolder() == ItemStackList.KitEditorHolder && member.kitedit) {
            member.kitedit = false;
            player.sendMessage(GlobalString.UnSavedSettings[member.lang]);
            InventoryUtils.fillItem(player.getInventory(), 9, 36, new ItemStack(Material.AIR));
            player.updateInventory();
        }
    }
}
