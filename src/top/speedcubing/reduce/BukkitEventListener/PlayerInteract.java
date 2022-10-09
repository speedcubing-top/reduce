package top.speedcubing.reduce.BukkitEventListener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import top.speedcubing.reduce.List.ItemStackList;
import top.speedcubing.reduce.List.StringList;
import top.speedcubing.reduce.Member;
import top.speedcubing.lib.bukkit.inventory.ItemUtils;
import top.speedcubing.reduce.Reduce;

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
                        player.sendMessage(StringList.InputStringCancel[member.user.lang]);
                        member.setSkin = false;
                    }
                    if (member.setName) {
                        player.sendMessage(StringList.InputStringCancel2[member.user.lang]);
                        member.setName = false;
                    }
                    if (member.ReachSettings) {
                        player.sendMessage(StringList.InputStringCancel3[member.user.lang]);
                        member.ReachSettings = false;
                    }
                    if (member.horSettings) {
                        player.sendMessage(StringList.InputStringCancel4[member.user.lang]);
                        member.horSettings = false;
                    }
                    if (member.verSettings) {
                        player.sendMessage(StringList.InputStringCancel5[member.user.lang]);
                        member.verSettings = false;
                    }
                    if (member.ReachSettings2) {
                        player.sendMessage(StringList.InputStringCancel3[member.user.lang]);
                        member.ReachSettings2 = false;
                    }
                    if (member.horSettings2) {
                        player.sendMessage(StringList.InputStringCancel4[member.user.lang]);
                        member.horSettings2 = false;
                    }
                }
                break;
            case COMPASS:
                Member member = Member.getMember(player);
                Inventory orig = Reduce.spec;
                Inventory inventory = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[member.user.lang]);
                inventory.setContents(orig.getContents());
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    int a = Reduce.totalName.indexOf(member.target.user.player.getName());
                    if (a != -1)
                        ItemUtils.glow(inventory.getItem(a));
                }
                player.openInventory(inventory);
                break;
            case SLIME_BALL:
                //click slimeball
                Reduce.quitSpec(player, player, true);
                break;
        }
        Block block = e.getClickedBlock();
        if (block != null && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && block.getType().equals(Material.BED_BLOCK) && !player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK)
            e.setCancelled(true);
    }
}