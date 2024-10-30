package top.speedcubing.reduce.bukkitlistener;


import com.mojang.authlib.properties.Property;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import top.speedcubing.lib.bukkit.WorldUtils;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.reduce.Member;
import top.speedcubing.reduce.Reduce;
import top.speedcubing.reduce.core.BlockEditor;
import top.speedcubing.reduce.list.ItemStackList;
import top.speedcubing.reduce.list.StringList;
import top.speedcubing.server.lang.LangInv;
import top.speedcubing.server.lang.LangItem;
import top.speedcubing.server.speedcubingServer;

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
            final Member member = Member.getMember(player);
            if (inventory.getName().equals("container.crafting")) {
                if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                    e.setCancelled(true);
            } else {
                InventoryHolder holder = inventory.getHolder();
                if (holder == ItemStackList.SettingsHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 10: {
                            LangInv inv = new LangInv(27, StringList.npchitsettings);
                            String[] reach = StringList.ReachLore[member.user.lang];
                            String[] hor = StringList.HorLore[member.user.lang];
                            String[] ver = StringList.VerLore[member.user.lang];
                            Integer[] cache = member.integerCache;
                            inv.setItem(new LangItem(Material.STICK).name(reach[0]).lore2(reach[1] + "\n" + reach[2].replace("%kb%", String.format("%.3f", cache[5] / 1000D)) + "\n" + reach[3].replace("%kb2%", String.format("%.3f", cache[9] / 1000D)) + "\n" + reach[4] + "\n" + reach[5] + "\n" + reach[6] + "\n" + reach[7]), 11);
                            inv.setItem(new LangItem(Material.STICK).name(hor[0]).lore2(hor[1] + "\n" + hor[2].replace("%kb%", String.format("%.3f", cache[6] / 1000D)) + "\n" + hor[3].replace("%kb2%", String.format("%.3f", cache[10] / 1000D)) + "\n" + hor[4] + "\n" + hor[5] + "\n" + hor[6] + "\n" + hor[7]), 12);
                            inv.setItem(new LangItem(Material.STICK).name(ver[0]).lore2(ver[1] + "\n" + ver[2].replace("%kb%", String.format("%.3f", cache[7] / 1000D)) + "\n" + ver[3] + "\n" + ver[4] + "\n" + ver[5] + "\n" + ver[6] + "\n" + ver[7]), 13);
                            inv.setItem(new LangItem(Material.FEATHER).name("§bVanilla KnockBack").lore2(member.booleanCache[3] ? "§cClick to turn off" : "§aClick to turn on"), 15);

                            inv.setClickEvent((r) -> {
                                player.closeInventory();
                                member.user.setInput(true);
                                member.user.sendMessage("§aPlease input a valid number (0.000 ~ 25.000)");
                                switch (e.getAction()) {
                                    case PICKUP_ALL:
                                        member.ReachSettings = true;
                                        break;
                                    case PICKUP_HALF:
                                        member.ReachSettings2 = true;
                                        break;
                                }
                            }, 11);
                            inv.setClickEvent((r) -> {
                                player.closeInventory();
                                member.user.sendMessage("§aPlease input a valid number (0.000 ~ 5.000)");
                                member.user.setInput(true);
                                switch (e.getAction()) {
                                    case PICKUP_ALL:
                                        member.horSettings = true;
                                        break;
                                    case PICKUP_HALF:
                                        member.horSettings2 = true;
                                        break;
                                }
                            }, 12);
                            inv.setClickEvent((r) -> {
                                if (Objects.requireNonNull(e.getAction()) == InventoryAction.PICKUP_ALL) {
                                    player.closeInventory();
                                    member.verSettings = true;
                                    member.user.sendMessage("§aPlease input a valid number (0.000 ~ 5.000)");
                                    member.user.setInput(true);
                                }
                            }, 13);
                            inv.setClickEvent((r) -> {
                                player.closeInventory();
                                member.booleanCache[3] = !member.booleanCache[3];
                            }, 15);
                            member.user.openLangInventory(inv);
                        }
                        break;
                        case 11: {
                            member.user.sound(Sound.CLICK);
                            Inventory inv = player.getInventory();
                            player.openInventory(ItemStackList.InventoryKitEditor[member.user.lang]);
                            InventoryUtils.fillItem(inv, 9, 36, Reduce.Invalid.get(member.user));
                            member.kitedit = true;
                        }
                        break;
                        case 12:
                            BlockEditor.createMainPage(player);
                            break;
                        case 13: {
                            member.user.sound(Sound.CLICK);
                            int x = Member.getMember(player).integerCache[3];
                            LangInv inv = new LangInv(27, StringList.stickedit);
                            for (int i = 0; i < 6; i++) {
                                inv.setItem(ItemStackList.stickeditor[0][i][member.user.lang], i + 10);
                            }
                            inv.setItem(ItemStackList.stickeditor[1][x][member.user.lang],x + 10);

                            inv.setAllClickEvent((r) -> {
                                int rawSlot = r.getEvent().getRawSlot();
                                if (rawSlot >= 10 && rawSlot <= 15) {
                                    member.user.sound(Sound.CLICK);
                                    member.integerCache[3] = rawSlot - 10;
                                    player.closeInventory();
                                    member.user.sendMessage("%lang_general_saved_setting%");
                                    player.getInventory().setItem(member.integerCache[0], ItemStackList.Sticks[member.integerCache[3]][member.user.lang]);
                                }
                            });
                            member.user.openLangInventory(inv);
                        }
                        break;
                        case 14: {
                            member.user.sound(Sound.CLICK);
                            LangInv inv = new LangInv(18, "§6Select Map");
                            LangItem[] stacks = ItemStackList.Maps[0];
                            inv.setItem(stacks[0], 0);
                            inv.setItem(stacks[1], 1);
                            inv.setItem(stacks[2], 2);
                            inv.setItem(stacks[3], 3);
                            inv.setItem(stacks[4], 4);
                            inv.setItem(ItemStackList.Maps[1][member.map], member.map);
                            inv.setAllClickEvent((r) -> {
                                int rawSlot = r.getEvent().getRawSlot();
                                if (rawSlot < Reduce.maptypes && rawSlot != member.map) { //swap map
                                    int newroom = 1;
                                    while (Reduce.Rooms[rawSlot].contains(newroom)) {
                                        newroom++;
                                    }
                                    Reduce.Rooms[rawSlot].add(newroom);
                                    int map = member.map;
                                    int coord = member.coord;
                                    int oldroom = Reduce.coordToRoom(coord);
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
                                    member.removeFromGUI(true);
                                    player.closeInventory();
                                    member.startGame(rawSlot, newroom, member.booleanCache);
                                    Reduce.Rooms[map].remove(oldroom);
                                    WorldUtils.fillAir(-20 + map * 500, 80, -20 + coord, 20 + map * 500, 120, 20 + coord, player.getWorld());
                                }
                            });
                            member.user.openLangInventory(inv);
                        }
                        break;
                        case 15: {
                            member.user.sound(Sound.CLICK);
                            LangInv inv = new LangInv(27, "§bNPC appearances");
                            inv.setItem(new LangItem(Material.SIGN).name("§aChange NPC's skin"), 10);
                            inv.setItem(new LangItem(Material.SKULL_ITEM).durability(3).name("§aUse your current skin").owner(player.getName()), 11);
                            inv.setItem(new LangItem(Material.NAME_TAG).name("§aChange NPC's DisplayName"), 13);
                            inv.setItem(new LangItem(Material.NAME_TAG).name("§aReset NPC's name to default"), 14);
                            inv.setClickEvent((r) -> {
                                member.user.sendMessage("§aPlease input a valid username for NPC's skin in chat:");
                                player.closeInventory();
                                member.setSkin = true;
                                member.user.setInput(true);
                            }, 10);
                            inv.setClickEvent((r) -> {
                                Property property = ((CraftPlayer) player).getProfile().getProperties().get("textures").iterator().next();
                                Member.getMember(player).setSkin(player, property.getValue(), property.getSignature());
                                player.closeInventory();
                            }, 11);
                            inv.setClickEvent((r) -> {
                                member.user.sendMessage("§aPlease input a String for NPC's name in chat:");
                                player.closeInventory();
                                member.setName = true;
                                member.user.setInput(true);
                            }, 13);
                            inv.setClickEvent((r) -> {
                                Member.getMember(player).Holo.setName("§6speedcubing");
                                player.closeInventory();
                            }, 14);
                            member.user.openLangInventory(inv);
                        }
                        break;
                        case 16: {
                            int lang = member.user.lang;
                            member.user.sound(Sound.CLICK);
                            Boolean[] bs = member.booleanCache;
                            org.bukkit.inventory.ItemStack s1 = ItemStackList.preferButton[0][lang];
                            ItemStack s2 = ItemStackList.preferButton[1][lang];
                            // ItemStackList.PreferHolder,
                            LangInv inv = new LangInv(36, StringList.prefedit);
                            for (int i = 0; i < 3; i++) {
                                inv.setItem(ItemStackList.prefereditor[i][lang], i + 12);
                            }
                            inv.setItem(bs[0] ? s1 : s2, 21);
                            inv.setItem(bs[1] ? s1 : s2, 22);
                            inv.setItem(bs[2] ? s1 : s2, 23);
                            inv.setClickEvent((r) -> {
                                member.user.sound(Sound.CLICK);
                                boolean b = member.booleanCache[0];
                                r.getEvent().getClickedInventory().setItem(r.getEvent().getRawSlot(), ItemStackList.preferButton[b ? 1 : 0][member.user.lang]);
                                member.booleanCache[0] = !b;
                            }, 21);
                            inv.setClickEvent((r) -> {
                                member.user.sound(Sound.CLICK);
                                boolean b = member.booleanCache[1];
                                r.getEvent().getClickedInventory().setItem(r.getEvent().getRawSlot(), ItemStackList.preferButton[b ? 1 : 0][member.user.lang]);
                                member.booleanCache[1] = !b;
                            }, 22);
                            inv.setClickEvent((r) -> {
                                member.user.sound(Sound.CLICK);
                                boolean b = member.booleanCache[2];
                                r.getEvent().getClickedInventory().setItem(r.getEvent().getRawSlot(), ItemStackList.preferButton[b ? 1 : 0][member.user.lang]);
                                member.booleanCache[2] = !b;
                            }, 23);
                            member.user.openLangInventory(inv);
                        }
                        break;
                    }
                } else if (holder == ItemStackList.KitEditorHolder) {
                    if (RawSlot < 36)
                        e.setCancelled(true);
                    switch (RawSlot) {
                        case 3:
                            member.user.sound(Sound.CLICK);
                            if (e.getCursor().getType() == Material.AIR) {
                                member.user.sendMessage("%lang_general_saved_setting%");
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
                            member.user.sound(Sound.CLICK);
                            player.closeInventory();
                            break;
                    }
                } else if (holder == ItemStackList.SpecHolder) {
                    e.setCancelled(true);
                    if (RawSlot < Reduce.totalName.size()) {
                        //join spectate
                        boolean isSpec = player.getGameMode() == GameMode.ADVENTURE;
                        if (isSpec && Reduce.totalName.get(RawSlot).equals(member.target.user.bGetName()))
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
                        if (target != 0 && target != member.user.id) {
                            if (isSpec) {
                                if (member.task != -1) {
                                    Bukkit.getScheduler().cancelTask(member.task);
                                    member.task = -1;
                                }
                                member.target.Spectators.remove(member);
                                member.target.npc.removeListener(player);
                                member.target.Holo.removeListener(player);
                            } else {
                                member.Spectators.forEach(a -> Reduce.quitSpec(true, a, true));
                                member.Spectators.clear();
                                member.removeFromGUI(false);
                                player.closeInventory();
                                member.oldMap = member.map;
                                member.oldCoord = member.coord;
                                player.setGameMode(GameMode.ADVENTURE);
                                player.setAllowFlight(true);
                                member.clearBlocks();
                                Inventory inv = player.getInventory();
                                inv.clear();
                                member.user.setLangItem(inv, 4, ItemStackList.Spec);
                                member.user.setLangItem(inv, 8, ItemStackList.LeaveSpec);
                            }
                            Member targetMember = Member.getMember(target);
                            targetMember.Spectators.add(member);
                            targetMember.npc.addListener(player);
                            targetMember.Holo.addListener(player);
                            member.coord = targetMember.coord;
                            member.map = map;
                            member.target = targetMember;
                            player.teleport(new Location(Reduce.world, map * 500 + 0.5, 100, targetMember.coord + 0.5, 0, 0));
                            player.closeInventory();
                            targetMember.user.bHidePlayer(player);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(speedcubingServer.getInstance(), () -> {
                                targetMember.npc.setListener(player).spawn().hideFromTab(4000).undoSetListener();
                                targetMember.Holo.setListener(player).spawn().undoSetListener();
                            }, 1);

                            if (member.setSkin) {
                                member.user.sendMessage(StringList.InputStringCancel);
                                member.setSkin = false;
                            }
                            if (member.setName) {
                                member.user.sendMessage(StringList.InputStringCancel2);
                                member.setName = false;
                            }
                            if (member.ReachSettings2) {
                                member.user.sendMessage(StringList.InputStringCancel3);
                                member.ReachSettings2 = false;
                            }
                            if (member.horSettings2) {
                                member.user.sendMessage(StringList.InputStringCancel4);
                                member.horSettings2 = false;
                            }
                            if (member.ReachSettings) {
                                member.user.sendMessage(StringList.InputStringCancel3);
                                member.ReachSettings = false;
                            }
                            if (member.horSettings) {
                                member.user.sendMessage(StringList.InputStringCancel4);
                                member.horSettings = false;
                            }
                            if (member.verSettings) {
                                member.user.sendMessage(StringList.InputStringCancel5);
                                member.verSettings = false;
                            }
                        }
                    }
                }
            }
        }
    }
}
