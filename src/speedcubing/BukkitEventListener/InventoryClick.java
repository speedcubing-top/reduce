package speedcubing.BukkitEventListener;


import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import speedcubing.Core.BlockEditor;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;
import speedcubing.Member;
import speedcubing.lib.bukkit.WorldUtils;
import speedcubing.lib.bukkit.inventory.InventoryUtils;
import speedcubing.lib.bukkit.inventory.ItemBuilder;
import speedcubing.server.config;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.speedcubingServer;

import java.util.Timer;
import java.util.TimerTask;

public class InventoryClick implements Listener {
    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent e) {
        if (e.getCursor() != null && e.getCursor().getAmount() != 1)
            e.setCancelled(true);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        int RawSlot = e.getRawSlot();
        if (e.getAction() == InventoryAction.PICKUP_HALF || e.getAction() == InventoryAction.PLACE_ONE)
            e.setCancelled(true);
        if (RawSlot != -999) {
            Inventory inventory = e.getInventory();
            Player player = (Player) e.getWhoClicked();
            if (inventory.getName().equals("container.crafting")) {
                if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                    e.setCancelled(true);
            } else {
                InventoryHolder holder = inventory.getHolder();
                if (holder == ItemStackList.SettingsHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 10: {
                            Member member = Member.getMember(player);
                            Inventory inv = Bukkit.createInventory(ItemStackList.HitSettingsHolder, 27, StringList.npchitsettings[member.lang]);
                            String[] reach = StringList.ReachLore[member.lang];
                            String[] hor = StringList.HorLore[member.lang];
                            String[] ver = StringList.VerLore[member.lang];
                            Integer[] cache = member.integerCache;
                            inv.setItem(11, new ItemBuilder(Material.STICK).name(reach[0]).lore(reach[1], reach[2].replace("%kb%", String.format("%.3f", cache[5] / 1000D)), reach[3].replace("%kb2%", String.format("%.3f", cache[9] / 1000D)), reach[4], reach[5], reach[6], reach[7]).build());
                            inv.setItem(12, new ItemBuilder(Material.STICK).name(hor[0]).lore(hor[1], hor[2].replace("%kb%", String.format("%.3f", cache[6] / 1000D)), hor[3].replace("%kb2%", String.format("%.3f", cache[10] / 1000D)), hor[4], hor[5], hor[6], hor[7]).build());
                            inv.setItem(13, new ItemBuilder(Material.STICK).name(ver[0]).lore(ver[1], ver[2].replace("%kb%", String.format("%.3f", cache[7] / 1000D)), ver[3], ver[4], ver[5], ver[6], ver[7]).build());
                            inv.setItem(15, new ItemBuilder(Material.FEATHER).name(StringList.def[member.lang]).lore(member.booleanCache[3] ? "§cClick to turn off" : "§aClick to turn on").build());
                            player.openInventory(inv);
                        }
                        break;
                        case 11: {
                            Member member = Member.getMember(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            Inventory inv = player.getInventory();
                            player.openInventory(ItemStackList.InventoryKitEditor[member.lang]);
                            InventoryUtils.fillItem(inv, 9, 36, Reduce.Invalid[member.lang]);
                            member.kitedit = true;
                        }
                        break;
                        case 12:
                            BlockEditor.createMainPage(player);
                            break;
                        case 13: {
                            Member member = Member.getMember(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int x = Member.getMember(player).integerCache[3];
                            Inventory inv = Bukkit.createInventory(ItemStackList.StickSkinHolder, 27, StringList.stickedit[member.lang]);
                            for (int i = 0; i < 6; i++) {
                                inv.setItem(i + 10, ItemStackList.stickeditor[0][i][member.lang]);
                            }
                            org.bukkit.inventory.ItemStack selected = ItemStackList.stickeditor[1][x][member.lang];
                            inv.setItem(x + 10, selected);
                            player.openInventory(inv);
                        }
                        break;
                        case 14: {
                            Member member = Member.getMember(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            Inventory inv = Bukkit.createInventory(ItemStackList.MapHolder, 18, StringList.mapselect[member.lang]);
                            ItemStack[][] stacks = ItemStackList.Maps[0];
                            inv.setItem(0, stacks[0][member.lang]);
                            inv.setItem(1, stacks[1][member.lang]);
                            inv.setItem(2, stacks[2][member.lang]);
                            inv.setItem(3, stacks[3][member.lang]);
                            inv.setItem(member.map, ItemStackList.Maps[1][member.map][member.lang]);
                            player.openInventory(inv);
                        }
                        break;
                        case 15: {
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = Member.getMember(player).lang;
                            Inventory inv = Bukkit.createInventory(ItemStackList.SkinHolder, 27, StringList.title[lang]);
                            inv.setItem(10, new ItemBuilder(Material.SIGN).name(StringList.input[lang]).build());
                            inv.setItem(11, new ItemBuilder(Material.SKULL_ITEM).durability(3).name(StringList.own[lang]).owner(player.getName()).build());
                            inv.setItem(13, new ItemBuilder(Material.NAME_TAG).name(StringList.name[lang]).build());
                            inv.setItem(14, new ItemBuilder(Material.NAME_TAG).name(StringList.resetname[lang]).build());
                            inv.setItem(16, new ItemBuilder(Material.STICK).name(StringList.resetname[lang]).build());
                            player.openInventory(inv);
                        }
                        break;
                        case 16: {
                            Member member = Member.getMember(player);
                            int lang = member.lang;
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            Boolean[] bs = member.booleanCache;
                            org.bukkit.inventory.ItemStack s1 = ItemStackList.preferButton[0][lang];
                            ItemStack s2 = ItemStackList.preferButton[1][lang];
                            Inventory inv = Bukkit.createInventory(ItemStackList.PreferHolder, 36, StringList.prefedit[lang]);
                            for (int i = 0; i < 3; i++) {
                                inv.setItem(i + 12, ItemStackList.prefereditor[i][lang]);
                            }
                            inv.setItem(21, bs[0] ? s1 : s2);
                            inv.setItem(22, bs[1] ? s1 : s2);
                            inv.setItem(23, bs[2] ? s1 : s2);
                            player.openInventory(inv);
                        }
                        break;
                    }
                } else if (holder == ItemStackList.KitEditorHolder) {
                    if (RawSlot < 36)
                        e.setCancelled(true);
                    switch (RawSlot) {
                        case 3:
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            if (e.getCursor().getType() == Material.AIR) {
                                Member member = Member.getMember(player);
                                player.sendMessage(GlobalString.SavedSettings[member.lang]);
                                Integer[] reduce = member.integerCache;
                                Inventory inv = player.getInventory();
                                for (int i = 0; i < 9; i++) {
                                    ItemStack stack = inv.getItem(i);
                                    if (stack != null) {
                                        if (stack.getType() == Material.COMPASS)
                                            reduce[8] = i;
                                        else if (stack.getType().isBlock())
                                            reduce[1] = i;
                                        else if (stack.getType() == Material.INK_SACK)
                                            reduce[2] = i;
                                        else if (stack.getType() != Material.AIR)
                                            reduce[0] = i;
                                    }
                                }
                                member.kitedit = false;
                                player.closeInventory();
                                InventoryUtils.fillItem(player.getInventory(), 9, 36, new ItemStack(Material.AIR));
                                player.updateInventory();
                                player.getInventory().setHeldItemSlot(reduce[2]);
                            } else {
                                Inventory inv = player.getInventory();
                                for (int i = 0; i < 9; i++) {
                                    if (inv.getItem(i) == null) {
                                        inv.setItem(i, e.getCursor());
                                        break;
                                    }
                                }
                                player.setItemOnCursor(new ItemStack(Material.AIR));
                            }
                            break;
                        case 5:
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            player.closeInventory();
                            break;
                    }
                } else if (holder == ItemStackList.PreferHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 21: {
                            Member member = Member.getMember(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = member.booleanCache[0];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][member.lang]);
                            member.booleanCache[0] = !b;
                        }
                        break;
                        case 22: {
                            Member member = Member.getMember(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = member.booleanCache[1];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][member.lang]);
                            member.booleanCache[1] = !b;
                        }
                        break;
                        case 23: {
                            Member member = Member.getMember(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = member.booleanCache[2];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][member.lang]);
                            member.booleanCache[2] = !b;
                        }
                        break;
                    }
                } else if (holder == ItemStackList.StickSkinHolder) {
                    e.setCancelled(true);
                    if (RawSlot >= 10 && RawSlot <= 15) {
                        Member member = Member.getMember(player);
                        int x = RawSlot - 10;
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                        member.integerCache[3] = x;
                        player.closeInventory();
                        player.sendMessage(GlobalString.SavedSettings[member.lang]);
                        player.getInventory().setItem(member.integerCache[0], ItemStackList.Sticks[x][member.lang]);
                    }
                } else if (holder == ItemStackList.MapHolder) {
                    e.setCancelled(true);
                    Member member = Member.getMember(player);
                    if (RawSlot < Reduce.maptypes && RawSlot != member.map) { //swap map
                        Reduce.Rooms[RawSlot] += 1;
                        if (member.task != -1) {
                            Bukkit.getScheduler().cancelTask(member.task);
                            member.task = -1;
                        }
                        member.setSkin = false;
                        member.setName = false;
                        member.ReachSettings2 = false;
                        member.horSettings2 = false;
                        member.ReachSettings = false;
                        member.horSettings = false;
                        member.verSettings = false;
                        member.SideBar.cancel();
                        member.Holo.despawn();
                        member.npc.despawn();
                        member.clearBlocks();
                        Reduce.removeFromGUI(member, member.map, 0);
                        player.closeInventory();
                        member.startGame(player, RawSlot, Reduce.Rooms[RawSlot], member.booleanCache, member.integerCache, member.Spectators, member.Holo);
                    }
                } else if (holder == ItemStackList.SkinHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 10: {
                            Member member = Member.getMember(player);
                            player.sendMessage(StringList.InputString[member.lang]);
                            player.closeInventory();
                            member.setSkin = true;
                            speedcubingServer.node(true, member.id, member.tcpPort);
                        }
                        break;
                        case 11: {
                            Property property = ((CraftPlayer) player).getProfile().getProperties().get("textures").iterator().next();
                            Member.getMember(player).setSkin(player, property.getValue(), property.getSignature());
                            player.closeInventory();
                        }
                        break;
                        case 13: {
                            Member member = Member.getMember(player);
                            player.sendMessage(StringList.InputString2[member.lang]);
                            player.closeInventory();
                            member.setName = true;
                            speedcubingServer.node(true, member.id, member.tcpPort);
                        }
                        break;
                        case 14: {
                            Member.getMember(player).Holo.setName("§6" + config.SERVERIP);
                            player.closeInventory();
                        }
                        break;
                    }
                } else if (holder == ItemStackList.HitSettingsHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 11: {
                            Member member = Member.getMember(player);
                            player.closeInventory();
                            speedcubingServer.node(true, member.id, member.tcpPort);
                            player.sendMessage(StringList.veloinput2[member.lang]);
                            switch (e.getAction()) {
                                case PICKUP_ALL:
                                    member.ReachSettings = true;
                                    break;
                                case PICKUP_HALF:
                                    member.ReachSettings2 = true;
                                    break;
                            }
                        }
                        break;
                        case 12: {
                            Member member = Member.getMember(player);
                            player.closeInventory();
                            player.sendMessage(StringList.veloinput[member.lang]);
                            speedcubingServer.node(true, member.id, member.tcpPort);
                            switch (e.getAction()) {
                                case PICKUP_ALL:
                                    member.horSettings = true;
                                    break;
                                case PICKUP_HALF:
                                    member.horSettings2 = true;
                                    break;
                            }
                        }
                        break;
                        case 13: {
                            switch (e.getAction()) {
                                case PICKUP_ALL:
                                    Member member = Member.getMember(player);
                                    player.closeInventory();
                                    member.verSettings = true;
                                    player.sendMessage(StringList.veloinput[member.lang]);
                                    speedcubingServer.node(true, member.id, member.tcpPort);
                                    break;
                            }
                        }
                        break;
                        case 15: {
                            Member member = Member.getMember(player);
                            player.closeInventory();
                            member.booleanCache[3] = !member.booleanCache[3];
                        }
                        break;
                    }
                } else if (holder == ItemStackList.SpecHolder) {
                    e.setCancelled(true);
                    if (RawSlot < Reduce.totalName.size()) {
                        //join spectate
                        boolean isSpec = player.getGameMode() == GameMode.ADVENTURE;
                        Member member = Member.getMember(player);
                        if (isSpec && Reduce.totalName.get(RawSlot).equals(member.target.player.getName()))
                            return;
                        int map = 0;
                        int front = 0;
                        for (int i = 0; i < Reduce.specStorage.length; i++) {
                            int size = Reduce.specStorage[i].size();
                            if (RawSlot < front + size)
                                break;
                            else front += size;
                            map += 1;
                        }
                        int target = Reduce.specStorage[map].get(RawSlot - front);
                        if (target != 0 && target != member.id) {
                            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                            PlayerConnection connection = entityPlayer.playerConnection;
                            if (isSpec) {
                                if (member.task != -1) {
                                    Bukkit.getScheduler().cancelTask(member.task);
                                    member.task = -1;
                                }
                                member.target.Spectators.remove(connection);
                                member.target.npc.listener.remove(connection);
                                member.target.Holo.listener.remove(connection);
                            } else {
                                member.Spectators.forEach(a -> Reduce.quitSpec(null, a, true));
                                member.Spectators.clear();
                                Reduce.removeFromGUI(member, member.map, member.id);
                                player.closeInventory();
                                member.oldMap = member.map;
                                member.oldCoord = member.coord;
                                player.setGameMode(GameMode.ADVENTURE);
                                player.setAllowFlight(true);
                                member.clearBlocks();
                                Inventory inv = player.getInventory();
                                inv.clear();
                                inv.setItem(4, ItemStackList.Spec[member.lang]);
                                inv.setItem(8, ItemStackList.LeaveSpec[member.lang]);
                            }
                            Member targetMember = Member.getMember(target);
                            targetMember.Spectators.add(connection);
                            targetMember.npc.listener.add(connection);
                            targetMember.Holo.listener.add(connection);
                            member.coord = targetMember.coord;
                            member.map = map;
                            member.target = targetMember;
                            player.teleport(new Location(player.getWorld(), map * 500 + 0.5, 100, targetMember.coord + 0.5, 0, 0));
                            player.closeInventory();
                            Member.getMember(target).player.hidePlayer(player);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                                targetMember.npc.setListenerValues(connection).spawn().rollBackListenerValues();
                                targetMember.Holo.setListenerValues(connection).spawn().rollBackListenerValues();
                            }, 1);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, targetMember.npc.entityPlayer));
                                }
                            }, 4000);

                            if (member.setSkin) {
                                player.sendMessage(StringList.InputStringCancel[member.lang]);
                                member.setSkin = false;
                            }
                            if (member.setName) {
                                player.sendMessage(StringList.InputStringCancel2[member.lang]);
                                member.setName = false;
                            }
                            if (member.ReachSettings2) {
                                player.sendMessage(StringList.InputStringCancel3[member.lang]);
                                member.ReachSettings2 = false;
                            }
                            if (member.horSettings2) {
                                player.sendMessage(StringList.InputStringCancel4[member.lang]);
                                member.horSettings2 = false;
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
                    }
                } else BlockEditor.select(holder, e, RawSlot, player);
            }
        }
    }
}
