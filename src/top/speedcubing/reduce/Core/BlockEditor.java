package top.speedcubing.reduce.Core;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import top.speedcubing.lib.bukkit.inventory.ItemUtils;
import top.speedcubing.reduce.List.ItemStackList;
import top.speedcubing.reduce.Member;
import top.speedcubing.server.lang.GlobalString;

public class BlockEditor {

    public static final InventoryHolder BlockHolder = () -> null;
    public static final InventoryHolder sandstoneholder = () -> null;
    public static final InventoryHolder woolholder = () -> null;
    public static final InventoryHolder oreblockeditor = () -> null;
    public static final InventoryHolder oreeditor = () -> null;
    public static final InventoryHolder quartzeditor = () -> null;
    public static final InventoryHolder woodeditor = () -> null;
    public static final InventoryHolder clayeditor = () -> null;


    static int length;

    public void Load() {
        length = ItemStackList.wooleditor.length + ItemStackList.sandstoneeditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length + ItemStackList.quartzeditor.length + ItemStackList.woodeditor.length + ItemStackList.clayeditor.length;
    }

    public static void createMainPage(Player player) {
        Member member = Member.getMember(player);
        member.user.sound(Sound.CLICK);
        int lang = member.user.lang;
        Inventory inv = Bukkit.createInventory(BlockHolder, 27, ItemStackList.blockedit[lang]);
        int index = member.integerCache[4] - length;
        for (int i = 0; i < ItemStackList.categoryblockeditor.length; i++) {
            inv.setItem(i + 1, ItemStackList.categoryblockeditor[i][lang]);
        }
        for (int i = 0; i < ItemStackList.blockeditor.length; i++) {
            inv.setItem(i + 11, ItemStackList.blockeditor[i][lang]);
        }
        if (index >= 0)
            inv.setItem(index + 11, ItemUtils.glow(ItemUtils.setLore(ItemStackList.blockeditor[index][lang].clone(), GlobalString.selected.get(lang))));
        player.openInventory(inv);
    }

    public static void select(InventoryHolder holder, InventoryClickEvent e, int RawSlot, Player player) {
        if (holder == BlockHolder) {
            e.setCancelled(true);
            if (RawSlot >= 11 && RawSlot < 11 + ItemStackList.blockeditor.length) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot + length - 11;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            } else if (RawSlot >= 1 && RawSlot <= ItemStackList.categoryblockeditor.length) {
                e.setCancelled(true);
                switch (RawSlot) {
                    case 1: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        int lang = member.user.lang;
                        Inventory inv = Bukkit.createInventory(sandstoneholder, 27, ItemStackList.sandstoneedit[lang]);
                        int set = member.integerCache[4];
                        for (int i = 0; i < 3; i++) {
                            inv.setItem(i + 10, ItemStackList.sandstoneeditor[i][lang]);
                        }
                        for (int i = 3; i < 6; i++) {
                            inv.setItem(i + 11, ItemStackList.sandstoneeditor[i][lang]);
                        }
                        if (set < 6)
                            inv.setItem((set < 3) ? set + 10 : set + 11, ItemUtils.glow(ItemUtils.setLore(ItemStackList.sandstoneeditor[set][lang].clone(), GlobalString.selected.get(lang))));
                        player.openInventory(inv);
                    }
                    break;
                    case 2: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        int lang = member.user.lang;
                        Inventory inv = Bukkit.createInventory(woolholder, 27, ItemStackList.wooledit[lang]);
                        int index = member.integerCache[4] - ItemStackList.sandstoneeditor.length;
                        for (int i = 0; i < ItemStackList.wooleditor.length; i++) {
                            inv.setItem(i, ItemStackList.wooleditor[i][lang]);
                        }
                        if (index < ItemStackList.wooleditor.length && index >= 0)
                            inv.setItem(index, ItemUtils.glow(ItemUtils.setLore(ItemStackList.wooleditor[index][lang].clone(), GlobalString.selected.get(lang))));
                        player.openInventory(inv);
                    }
                    break;
                    case 3: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        int lang = member.user.lang;
                        Inventory inv = Bukkit.createInventory(oreblockeditor, 27, ItemStackList.oreblockedit[lang]);
                        int index = member.integerCache[4] - (ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length);
                        for (int i = 0; i < ItemStackList.oreblockeditor.length; i++) {
                            inv.setItem(i + 10, ItemStackList.oreblockeditor[i][lang]);
                        }
                        if (index < ItemStackList.oreblockeditor.length && index >= 0)
                            inv.setItem(index + 10, ItemUtils.glow(ItemUtils.setLore(ItemStackList.oreblockeditor[index][lang].clone(), GlobalString.selected.get(lang))));
                        player.openInventory(inv);
                    }
                    break;
                    case 4: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        int lang = member.user.lang;
                        Inventory inv = Bukkit.createInventory(oreeditor, 27, ItemStackList.oreedit[lang]);
                        int index = member.integerCache[4] - (ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length);
                        for (int i = 0; i < ItemStackList.oreeditor.length; i++) {
                            inv.setItem(i + 10, ItemStackList.oreeditor[i][lang]);
                        }
                        if (index < ItemStackList.oreeditor.length && index >= 0)
                            inv.setItem(index + 10, ItemUtils.glow(ItemUtils.setLore(ItemStackList.oreeditor[index][lang].clone(), GlobalString.selected.get(lang))));
                        player.openInventory(inv);
                    }
                    break;
                    case 5: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        int lang = member.user.lang;
                        Inventory inv = Bukkit.createInventory(quartzeditor, 27, ItemStackList.quartzedit[lang]);
                        int index = member.integerCache[4] - (ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length);
                        for (int i = 0; i < ItemStackList.quartzeditor.length; i++) {
                            inv.setItem(i + 12, ItemStackList.quartzeditor[i][lang]);
                        }
                        if (index < ItemStackList.quartzeditor.length && index >= 0)
                            inv.setItem(index + 12, ItemUtils.glow(ItemUtils.setLore(ItemStackList.quartzeditor[index][lang].clone(), GlobalString.selected.get(lang))));
                        player.openInventory(inv);
                    }
                    break;
                    case 6: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        int lang = member.user.lang;
                        Inventory inv = Bukkit.createInventory(woodeditor, 27, ItemStackList.woodedit[lang]);
                        int index = member.integerCache[4] - (ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length + ItemStackList.quartzeditor.length);
                        for (int i = 0; i < 3; i++) {
                            inv.setItem(i + 1, ItemStackList.woodeditor[i][lang]);
                        }
                        for (int i = 3; i < 6; i++) {
                            inv.setItem(i + 2, ItemStackList.woodeditor[i][lang]);
                        }
                        for (int i = 6; i < 9; i++) {
                            inv.setItem(i + 4, ItemStackList.woodeditor[i][lang]);
                        }
                        for (int i = 9; i < 12; i++) {
                            inv.setItem(i + 5, ItemStackList.woodeditor[i][lang]);
                        }
                        if (index < ItemStackList.woodeditor.length && index >= 0)
                            inv.setItem((index < 3 ? index + 1 : (index < 6 ? index + 2 : (index < 9 ? index + 4 : index + 5))), ItemUtils.glow(ItemUtils.setLore(ItemStackList.woodeditor[index][lang].clone(), GlobalString.selected.get(lang))));
                        player.openInventory(inv);
                    }
                    break;
                    case 7: {
                        Member member = Member.getMember(player);
                        member.user.sound(Sound.CLICK);
                        Inventory inv = Bukkit.createInventory(clayeditor, 27, ItemStackList.clayedit[member.user.lang]);
                        int index = member.integerCache[4] - (ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length + ItemStackList.quartzeditor.length + ItemStackList.woodeditor.length);
                        for (int i = 0; i < ItemStackList.clayeditor.length; i++) {
                            inv.setItem(i, ItemStackList.clayeditor[i][member.user.lang]);
                        }
                        if (index < ItemStackList.clayeditor.length && index >= 0)
                            inv.setItem(index, ItemUtils.glow(ItemUtils.setLore(ItemStackList.clayeditor[index][member.user.lang].clone(), GlobalString.selected.get(member.user.lang))));
                        player.openInventory(inv);
                    }
                    break;
                }
            }
        } else if (holder == sandstoneholder) {
            e.setCancelled(true);
            if (RawSlot > 9 && RawSlot < 17 && RawSlot != 13) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot > 12 ? RawSlot - 11 : RawSlot - 10;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        } else if (holder == woolholder) {
            e.setCancelled(true);
            if (RawSlot < 16) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot + ItemStackList.sandstoneeditor.length;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        } else if (holder == oreblockeditor) {
            e.setCancelled(true);
            if (RawSlot <= 16 && RawSlot >= 10) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot + ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length - 10;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        } else if (holder == oreeditor) {
            e.setCancelled(true);
            if (RawSlot <= 16 && RawSlot >= 10) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot + ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length - 10;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        } else if (holder == quartzeditor) {
            e.setCancelled(true);
            if (RawSlot <= 14 && RawSlot >= 12) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot + ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length - 12;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        } else if (holder == woodeditor) {
            e.setCancelled(true);
            if (RawSlot <= 16 && RawSlot >= 1 && RawSlot != 13 && RawSlot != 4 && RawSlot != 8 && RawSlot != 9) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = (RawSlot < 4 ? RawSlot - 1 : (RawSlot < 8 ? RawSlot - 2 : (RawSlot < 13 ? RawSlot - 4 : RawSlot - 5))) + ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length + ItemStackList.quartzeditor.length;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        } else if (holder == clayeditor) {
            e.setCancelled(true);
            if (RawSlot < 17) {
                Member member = Member.getMember(player);
                member.user.sound(Sound.CLICK);
                int x = RawSlot + ItemStackList.sandstoneeditor.length + ItemStackList.wooleditor.length + ItemStackList.oreblockeditor.length + ItemStackList.oreeditor.length + ItemStackList.quartzeditor.length + ItemStackList.woodeditor.length;
                member.integerCache[4] = x;
                player.closeInventory();
                player.getInventory().setItem(member.integerCache[1], ItemStackList.Blocks[x][member.user.lang]);
                member.user.sendLangMessage(GlobalString.SavedSettings);
            }
        }
    }
}
