package top.speedcubing.reduce.bukkitlistener;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.reduce.Member;
import top.speedcubing.reduce.list.ItemStackList;

public class InventoryClose implements Listener {
    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Member member = Member.getMember(player);
        if (e.getInventory().getHolder() == ItemStackList.KitEditorHolder && member.kitedit) {
            member.kitedit = false;
            member.user.sendMessage("%lang_inventory_discard%");
            InventoryUtils.fillItem(player.getInventory(), 9, 36, new ItemStack(Material.AIR));
            player.updateInventory();
        }
    }
}
