package top.speedcubing.reduce.BukkitEventListener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import top.speedcubing.lib.bukkit.inventory.ItemUtils;
import top.speedcubing.reduce.List.*;
import top.speedcubing.reduce.*;

public class PlayerInteract implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        switch (player.getItemInHand().getType()) {
            case INK_SACK:
                if (player.getLocation().getY() >= 90) {
                    Member member = Member.getMember(player);
                    player.openInventory(ItemStackList.Settings[member.user.lang]);
                    if (member.setSkin) {
                        member.user.sendLangMessage(StringList.InputStringCancel);
                        member.setSkin = false;
                    }
                    if (member.setName) {
                        member.user.sendLangMessage(StringList.InputStringCancel2);
                        member.setName = false;
                    }
                    if (member.ReachSettings) {
                        member.user.sendLangMessage(StringList.InputStringCancel3);
                        member.ReachSettings = false;
                    }
                    if (member.horSettings) {
                        member.user.sendLangMessage(StringList.InputStringCancel4);
                        member.horSettings = false;
                    }
                    if (member.verSettings) {
                        member.user.sendLangMessage(StringList.InputStringCancel5);
                        member.verSettings = false;
                    }
                    if (member.ReachSettings2) {
                        member.user.sendLangMessage(StringList.InputStringCancel3);
                        member.ReachSettings2 = false;
                    }
                    if (member.horSettings2) {
                        member.user.sendLangMessage(StringList.InputStringCancel4);
                        member.horSettings2 = false;
                    }
                }
                break;
            case COMPASS:
                Member member = Member.getMember(player);
                Inventory orig = Reduce.spec;
                Inventory inventory = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec.get(member.user.lang));
                inventory.setContents(orig.getContents());
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    int a = Reduce.totalName.indexOf(member.target.user.bGetName());
                    if (a != -1)
                        ItemUtils.glow(inventory.getItem(a));
                }
                player.openInventory(inventory);
                break;
            case SLIME_BALL:
                //click slimeball
                Reduce.quitSpec(false, Member.getMember(player), true);
                break;
        }
        Block block = e.getClickedBlock();
        if (block != null && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && block.getType().equals(Material.BED_BLOCK) && !player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK)
            e.setCancelled(true);
    }
}