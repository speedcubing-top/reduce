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
                    UUID uuid = player.getUniqueId();
                    int lang = User.getLang(uuid);
                    player.openInventory(ItemStackList.Settings[lang]);
                    if (Reduce.setSkin.contains(uuid)) {
                        player.sendMessage(StringList.InputStringCancel[lang]);
                        Reduce.setSkin.remove(uuid);
                    }
                    if (Reduce.setName.contains(uuid)) {
                        player.sendMessage(StringList.InputStringCancel2[lang]);
                        Reduce.setName.remove(uuid);
                    }
                    if (Reduce.ReachSettings.contains(uuid)) {
                        player.sendMessage(StringList.InputStringCancel3[lang]);
                        Reduce.ReachSettings.remove(uuid);
                    }
                    if (Reduce.horSettings.contains(uuid)) {
                        player.sendMessage(StringList.InputStringCancel4[lang]);
                        Reduce.horSettings.remove(uuid);
                    }
                    if (Reduce.verSettings.contains(uuid)) {
                        player.sendMessage(StringList.InputStringCancel5[lang]);
                        Reduce.verSettings.remove(uuid);
                    }
                }
                break;
            case COMPASS:
                UUID uuid = player.getUniqueId();
                Inventory orig = Reduce.spec;
                Inventory inventory = Bukkit.createInventory(orig.getHolder(), orig.getSize(), StringList.spec[User.getLang(uuid)]);
                inventory.setContents(orig.getContents());
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    int a = Reduce.totalName.indexOf(Bukkit.getPlayer(Reduce.target.get(uuid)).getName());
                    if (a != -1)
                        ItemUtils.glow(inventory.getItem(a));
                }
                player.openInventory(inventory);
                break;
            case SLIME_BALL:
                //click slimeball
                Console.sendColoredConsole("§8[§bReduce§8] §a " + player.getName() + "§b interact at SlimeBall.");
                Reduce.quitSpec(player, ((CraftPlayer) player).getHandle().playerConnection, true);
                break;
        }
        Block block = e.getClickedBlock();
        if (block != null && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && block.getType().equals(Material.BED_BLOCK) && !player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK)
            e.setCancelled(true);
    }
}