package speedcubing.BukkitEventListener;

import cubing.lib.bukkit.ItemUtils;
import cubing.lib.utils.Console;
import cubingserver.libs.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Member;

import java.util.UUID;

public class PlayerInteract implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.ALLOW);
        Player player = e.getPlayer();
        switch (player.getItemInHand().getType()) {
            case INK_SACK:
                if (player.getLocation().getY() >= 90) {
                    Member member = Member.users.get(player.getUniqueId());
                    player.openInventory(ItemStackList.Settings[member.lang]);
                    if (member.setSkin) {
                        player.sendMessage(StringList.InputStringCancel[member.lang]);
                        member.setSkin = false;
                    }
                    if (member.setName) {
                        player.sendMessage(StringList.InputStringCancel2[member.lang]);
                        member.setName = false;
                    }
                    if (member.ReachSettings) {
                        player.sendMessage(StringList.InputStringCancel3[member.lang]);
                        member.ReachSettings = false;
                    }
                    if (member.horSettings) {
                        player.sendMessage(StringList.InputStringCancel4[member.lang]);
                        member.horSettings = false;
                    }
                    if (member.verSettings) {
                        player.sendMessage(StringList.InputStringCancel5[member.lang]);
                        member.verSettings = false;
                    }
                }
                break;
            case COMPASS:
                Member member = Member.users.get(player.getUniqueId());
                Inventory orig = Reduce.spec;
                Inventory inventory = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[member.lang]);
                inventory.setContents(orig.getContents());
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    int a = Reduce.totalName.indexOf(Bukkit.getPlayer(member.target).getName());
                    if (a != -1)
                        ItemUtils.glow(inventory.getItem(a));
                }
                player.openInventory(inventory);
                break;
            case SLIME_BALL:
                //click slimeball
                Reduce.quitSpec(player, ((CraftPlayer) player).getHandle().playerConnection, true);
                break;
        }
        Block block = e.getClickedBlock();
        if (block != null && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && block.getType().equals(Material.BED_BLOCK) && !player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK)
            e.setCancelled(true);
    }
}