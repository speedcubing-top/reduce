package speedcubing.List;

import cubing.bukkit.InventoryUtils;
import cubing.bukkit.ItemUtils;
import cubingserver.StringList.GlobalString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import speedcubing.Core.Reduce;

import java.util.Collections;

public class ItemStackList {

    public static InventoryHolder SettingsHolder = () -> null;
    public static InventoryHolder KitEditorHolder = () -> null;
    public static final InventoryHolder StickSkinHolder = () -> null;
    public static final InventoryHolder HitSettingsHolder = () -> null;
    public static InventoryHolder MapHolder = () -> null;
    public static InventoryHolder SkinHolder = () -> null;
    public static InventoryHolder PreferHolder = () -> null;
    public static InventoryHolder SpecHolder = () -> null;
    public static ItemStack[][] Sticks = new ItemStack[6][4];
    public static Inventory[] Settings = new Inventory[4];
    public static ItemStack[] LeaveSpec = new ItemStack[4];
    public static Inventory[] InventoryKitEditor = new Inventory[4];
    public static ItemStack[] settings = new ItemStack[4];
    public static ItemStack[] Spec = new ItemStack[4];
    public static ItemStack[][][] stickeditor = new ItemStack[2][6][4];
    public static ItemStack[][][] Maps = new ItemStack[2][Reduce.maptypes][4];
    public static ItemStack[][] prefereditor = new ItemStack[3][4];
    public static ItemStack[][] preferButton = new ItemStack[2][4];

    public static ItemStack[][] Blocks = new ItemStack[73][4];

    public static ItemStack[][] categoryblockeditor = new ItemStack[7][4];

    public static ItemStack[][] blockeditor = new ItemStack[5][4];
    public static ItemStack[][] wooleditor = new ItemStack[16][4];
    public static ItemStack[][] sandstoneeditor = new ItemStack[6][4];
    public static ItemStack[][] oreblockeditor = new ItemStack[7][4];
    public static ItemStack[][] oreeditor = new ItemStack[7][4];
    public static ItemStack[][] quartzeditor = new ItemStack[3][4];
    public static ItemStack[][] woodeditor = new ItemStack[12][4];
    public static ItemStack[][] clayeditor = new ItemStack[17][4];

    public static final String[] blockedit = {"§bBlock Skin", "§bBlock Skin", "§bBlock Skin", "§bBlock Skin"};
    public static final String[] sandstoneedit = {"§bCategory: Sandstone", "§bCategory: Sandstone", "§bCategory: Sandstone", "§bCategory: Sandstone"};
    public static final String[] wooledit = {"§bCategory: Wool", "§bCategory: Wool", "§bCategory: Wool", "§bCategory: Wool"};
    public static final String[] oreblockedit = {"§bCategory: Ore (Blocks)", "§bCategory: Ore (Blocks)", "§bCategory: Ore (Blocks)", "§bCategory: Ore (Blocks)"};
    public static final String[] oreedit = {"§bCategory: Ores", "§bCategory: Ores", "§bCategory: Ores", "§bCategory: Ores"};
    public static final String[] quartzedit = {"§bCategory: Quartz Blocks", "§bCategory: Quartz Blocks", "§bCategory: Quartz Blocks", "§bCategory: Quartz Blocks"};
    public static final String[] woodedit = {"§bCategory: Woods", "§bCategory: Woods", "§bCategory: Woods", "§bCategory: Woods"};
    public static final String[] clayedit = {"§bCategory: Clay", "§bCategory: Clay", "§bCategory: Clay", "§bCategory: Clay"};

    public void Load() {
        String[] leavespec = {"§cLeave Spectate mode", "§cLeave Spectate mode", "§cLeave Spectate mode", "§cLeave Spectate mode"};
        String[] specitem = {"§bSpectate", "§bSpectate", "§bSpectate", "§bSpectate"};
        for (int i = 0; i < 4; i++) {
            Maps[0][0][i] = ItemUtils.item(Material.LOG, StringList.mapname[0][i], 1, 3, false, false, null, null, null, null);
            Maps[0][1][i] = ItemUtils.item(Material.BED, StringList.mapname[1][i], 1, -1, false, false, null, null, null, null);
            Maps[0][2][i] = ItemUtils.item(Material.BARRIER, StringList.mapname[2][i], 1, -1, false, false, null, null, null, null);
            Maps[0][3][i] = ItemUtils.item(Material.SMOOTH_BRICK, StringList.mapname[3][i], 1, 1, false, false, null, null, null, null);
            Maps[1][0][i] = ItemUtils.item(Material.LOG, StringList.mapname[0][i], 1, 3, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i]));
            Maps[1][1][i] = ItemUtils.item(Material.BED, StringList.mapname[1][i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i]));
            Maps[1][2][i] = ItemUtils.item(Material.BARRIER, StringList.mapname[2][i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i]));
            Maps[1][3][i] = ItemUtils.item(Material.SMOOTH_BRICK, StringList.mapname[3][i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i]));
            Spec[i] = ItemUtils.item(Material.COMPASS, specitem[i], 1, -1, false, false, null, null, null, null);
            LeaveSpec[i] = ItemUtils.item(Material.SLIME_BALL, leavespec[i], 1, -1, false, false, null, null, null, null);
        }
        {
            String[] stick = {"§6Stock", "§6Stick", "§6擊退棒", "§6击退棒"};
            String[] block = {"§6Blöcke", "§6Blocks", "§6方塊", "§6方块"};
            for (int i = 0; i < 4; i++) {
                ItemStack[] sticks = {
                        ItemUtils.item(Material.STICK, stick[i], 1, -1, false, false, null, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null),
                        ItemUtils.item(Material.DIAMOND_SWORD, stick[i], 1, -1, false, true, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES}, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null),
                        ItemUtils.item(Material.DIAMOND_HOE, stick[i], 1, -1, false, true, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES}, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null),
                        ItemUtils.item(Material.BLAZE_ROD, stick[i], 1, -1, false, false, null, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null),
                        ItemUtils.item(Material.BONE, stick[i], 1, -1, false, false, null, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null),
                        ItemUtils.item(Material.FEATHER, stick[i], 1, -1, false, false, null, new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}, null)
                };
                for (int j = 0; j < sticks.length; j++) {
                    Sticks[j][i] = sticks[j];
                }

                ItemStack[] blocks = {
                        ItemUtils.item(Material.SANDSTONE, block[i], 64, 2, false, false, null, null, null, null),
                        ItemUtils.item(Material.RED_SANDSTONE, block[i], 64, 2, false, false, null, null, null, null),
                        ItemUtils.item(Material.GOLD_BLOCK, block[i], 64, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.DIAMOND_BLOCK, block[i], 64, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.EMERALD_BLOCK, block[i], 64, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.PRISMARINE, block[i], 64, 2, false, false, null, null, null, null),
                        ItemUtils.item(Material.PACKED_ICE, block[i], 64, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.QUARTZ_BLOCK, block[i], 64, -1, false, false, null, null, null, null)
                };
                int length2 = blocks.length;
                for (int j = 0; j < length2; j++) {
                    Blocks[j][i] = blocks[j];
                }
            }
        }
        {
            String[] display = {"§cpause & settings", "§cpause & settings", "§cpause & settings", "§cpause & settings"};
            for (int i = 0; i < 4; i++) {
                settings[i] = ItemUtils.item(Material.INK_SACK, display[i], 1, 1, false, false, null, null, null, null);
            }
        }
        {
            String[] title = {"§cReduce Einstellungen", "§cReduce Settings", "§cReduce 設定", "§cReduce 设置"};
            String[] hit = {"§bHit Settings", "§bHit Settings", "§bHit Settings", "§bHit Settings"};
            String[] inv = {"§bInventar", "§bInventory", "§b物品欄", "§b物品栏"};
            String[] block = {"§bBlock Skin", "§bBlock Skin", "§c方塊材質", "§c方块材质"};
            String[] stick = {"§bKnockBackStick Skin", "§bKnockBackStick Skin", "§b擊退棒材質", "§b击退棒材质"};
            String[] map = {"§bChange Map", "§bChange Map", "§bChange Map", "§bChange Map"};
            String[] skin = {"§bNPC Settings", "§bNPC Settings", "§bNPC Settings", "§bNPC Settings"};
            String[] other = {"§bPreferences", "§bPreferences", "§bPreferences", "§bPreferences"};
            for (int i = 0; i < 4; i++) {
                Settings[i] = Bukkit.createInventory(SettingsHolder, 27, title[i]);
                Settings[i].setItem(10, ItemUtils.item(Material.STICK, hit[i], 1, -1, false, false, null, null, null, null));
                Settings[i].setItem(11, ItemUtils.item(Material.CHEST, inv[i], 1, 3, false, false, null, null, null, null));
                Settings[i].setItem(12, ItemUtils.item(Material.SANDSTONE, block[i], 1, 2, false, false, null, null, null, null));
                Settings[i].setItem(13, ItemUtils.item(Material.BLAZE_ROD, stick[i], 1, -1, false, false, null, null, null, null));
                Settings[i].setItem(14, ItemUtils.item(Material.EMPTY_MAP, map[i], 1, -1, false, false, null, null, null, null));
                Settings[i].setItem(15, ItemUtils.item(Material.SKULL_ITEM, skin[i], 1, 4, false, false, null, null, null, null));
                Settings[i].setItem(16, ItemUtils.item(Material.REDSTONE_COMPARATOR, other[i], 1, -1, false, false, null, null, null, null));
            }
        }
        {
            String[] title = {"§b Inventar", "§cReduce Inventory", "§cReduce 物品欄", "§cReduce 物品栏"};
            String[] save = {"§aÄnderungen speichern", "§aSave Changes", "§a儲存設定", "§a储存设置"};
            String[] unsave = {"§cnicht gespeicherte Änderungen", "§cUnSave Changes", "§c不儲存設定", "§c不储存设置"};
            for (int i = 0; i < 4; i++) {
                InventoryKitEditor[i] = Bukkit.createInventory(KitEditorHolder, 9, title[i]);
                InventoryUtils.fillItem(InventoryKitEditor[i], 0, 9, ItemUtils.item(Material.STAINED_GLASS_PANE, "§a", 1, 7, false, false, null, null, null, null));
                InventoryKitEditor[i].setItem(3, ItemUtils.item(Material.INK_SACK, save[i], 1, 10, false, false, null, null, null, null));
                InventoryKitEditor[i].setItem(5, ItemUtils.item(Material.INK_SACK, unsave[i], 1, 1, false, false, null, null, null, null));
            }
        }

        {
            String[][] prefs = {
                    {"§6NPC damage color", "§6NPC damage color", "§6NPC damage color", "§6NPC damage color"},
                    {"§6NPC damage sound", "§6NPC damage sound", "§6NPC damage sound", "§6NPC damage sound"},
                    {"§6NPC spam swing", "§6NPC spam swing", "§6NPC spam swing", "§6NPC spam swing"}
            };
            String[] active = {"§aActivated", "§aActivated", "§aActivated", "§aActivated"};
            String[] deactive = {"§cDeactivated", "§cDeactivated", "§cDeactivated", "§cDeactivated"};
            for (int i = 0; i < 4; i++) {
                ItemStack[] prefers = {
                        ItemUtils.item(Material.REDSTONE, prefs[0][i], 1, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.NOTE_BLOCK, prefs[1][i], 1, 3, false, false, null, null, null, null),
                        ItemUtils.item(Material.STICK, prefs[2][i], 1, -1, true, false, null, null, null, null),
                };
                ItemStack[] pref = {ItemUtils.item(Material.INK_SACK, active[i], 1, 10, false, false, null, null, null, null),
                        ItemUtils.item(Material.INK_SACK, deactive[i], 1, 1, false, false, null, null, null, null)
                };
                for (int j = 0; j < prefers.length; j++) {
                    prefereditor[j][i] = prefers[j];
                }
                for (int j = 0; j < pref.length; j++) {
                    preferButton[j][i] = pref[j];
                }
            }
        }

        {
            String[] stick = {"§6Stock", "§6Stick", "§6擊退棒", "§6击退棒"};
            String[] sword = {"§6Schwert", "§6Sword", "§6劍", "§6剑"};
            String[] hoe = {"§6Hoe", "§6Hoe", "§6Hoe", "§6Hoe"};
            String[] blaze = {"§6Blaze Rod", "§6Blaze Rod", "§6烈焰棒", "§6烈焰棒"};
            String[] bone = {"§6Bone", "§6Bone", "§6骨頭", "§6骨头"};
            String[] feather = {"§6Feather", "§6Feather", "§6Feather", "§6Feather"};
            for (int i = 0; i < 4; i++) {
                ItemStack[] sticks = {
                        ItemUtils.item(Material.STICK, stick[i], 1, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.DIAMOND_SWORD, sword[i], 1, -1, false, false, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES}, null, null, null),
                        ItemUtils.item(Material.DIAMOND_HOE, hoe[i], 1, -1, false, false, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES}, null, null, null),
                        ItemUtils.item(Material.BLAZE_ROD, blaze[i], 1, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.BONE, bone[i], 1, -1, false, false, null, null, null, null),
                        ItemUtils.item(Material.FEATHER, feather[i], 1, -1, false, false, null, null, null, null)
                };
                ItemStack[] sticks2 = {
                        ItemUtils.item(Material.STICK, stick[i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i])),
                        ItemUtils.item(Material.DIAMOND_SWORD, sword[i], 1, -1, true, false, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES}, null, null, Collections.singletonList(GlobalString.selected[i])),
                        ItemUtils.item(Material.DIAMOND_HOE, hoe[i], 1, -1, true, false, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES}, null, null, Collections.singletonList(GlobalString.selected[i])),
                        ItemUtils.item(Material.BLAZE_ROD, blaze[i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i])),
                        ItemUtils.item(Material.BONE, bone[i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i])),
                        ItemUtils.item(Material.FEATHER, feather[i], 1, -1, true, false, null, null, null, Collections.singletonList(GlobalString.selected[i]))
                };
                int length = sticks.length;
                for (int j = 0; j < length; j++) {
                    stickeditor[0][j][i] = sticks[j];
                    stickeditor[1][j][i] = sticks2[j];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            String[][] block = {
                    {"§bSandstone Blocks", "§bSandstone Blocks", "§bSandstone Blocks", "§bSandstone Blocks"},
                    {"§bWool Blocks", "§bWool Blocks", "§bWool Blocks", "§bWool Blocks"},
                    {"§bOre (Blocks)", "§bOre (Blocks)", "§bOre (Blocks)", "§bOre (Blocks)"},
                    {"§bOres", "§bOres", "§bOres", "§bOres"},
                    {"§bQuartz Blocks", "§bQuartz Blocks", "§bQuartz Blocks", "§bQuartz Blocks"},
                    {"§bWoods", "§bWoods", "§bWoods", "§bWoods"},
                    {"§bClay", "§bClay", "§bClay", "§bClay"}
            };

            ItemStack[] categoryblockeditor = {
                    ItemUtils.item(Material.SANDSTONE, block[0][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i])),
                    ItemUtils.item(Material.WOOL, block[1][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i])),
                    ItemUtils.item(Material.DIAMOND_BLOCK, block[2][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i])),
                    ItemUtils.item(Material.DIAMOND_ORE, block[3][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i])),
                    ItemUtils.item(Material.QUARTZ_BLOCK, block[4][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i])),
                    ItemUtils.item(Material.WOOD, block[5][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i])),
                    ItemUtils.item(Material.STAINED_CLAY, block[6][i], 1, -1, false, false, null, null, null, Collections.singletonList(GlobalString.category[i]))
            };

            ItemStack[] blockeditor = {
                    ItemUtils.item(Material.PACKED_ICE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.MELON_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.SNOW_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.OBSIDIAN, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.ENDER_STONE, null, 1, -1, false, false, null, null, null, null)
            };

            ItemStack[] sandstoneeditor = {
                    ItemUtils.item(Material.SANDSTONE, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.SANDSTONE, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.SANDSTONE, null, 1, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.RED_SANDSTONE, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.RED_SANDSTONE, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.RED_SANDSTONE, null, 1, 2, false, false, null, null, null, null),
            };

            ItemStack[] wooleditor = {
                    ItemUtils.item(Material.WOOL, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 4, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 5, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 6, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 7, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 8, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 9, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 10, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 11, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 12, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 13, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 14, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 1, 15, false, false, null, null, null, null)
            };
            ItemStack[] oreblockeditor = {
                    ItemUtils.item(Material.DIAMOND_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.IRON_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.REDSTONE_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.LAPIS_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.EMERALD_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.GOLD_BLOCK, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.COAL_BLOCK, null, 1, -1, false, false, null, null, null, null),
            };
            ItemStack[] oreeditor = {
                    ItemUtils.item(Material.DIAMOND_ORE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.IRON_ORE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.REDSTONE_ORE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.LAPIS_ORE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.EMERALD_ORE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.GOLD_ORE, null, 1, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.COAL_ORE, null, 1, -1, false, false, null, null, null, null),
            };
            ItemStack[] quartzeditor = {
                    ItemUtils.item(Material.QUARTZ_BLOCK, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.QUARTZ_BLOCK, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.QUARTZ_BLOCK, null, 1, 2, false, false, null, null, null, null),
            };
            ItemStack[] woodeditor = {
                    ItemUtils.item(Material.WOOD, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 1, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 1, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 1, 4, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 1, 5, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 1, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 1, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG_2, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG_2, null, 1, 1, false, false, null, null, null, null),
            };

            ItemStack[] clayeditor = {
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 4, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 5, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 6, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 7, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 8, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 9, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 10, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 11, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 12, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 13, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 14, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 1, 15, false, false, null, null, null, null),
                    ItemUtils.item(Material.HARD_CLAY, null, 1, -1, false, false, null, null, null, null)
            };
            ItemStack[] blocks = {
                    ItemUtils.item(Material.SANDSTONE, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.SANDSTONE, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.SANDSTONE, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.RED_SANDSTONE, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.RED_SANDSTONE, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.RED_SANDSTONE, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 4, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 5, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 6, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 7, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 8, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 9, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 10, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 11, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 12, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 13, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 14, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOL, null, 64, 15, false, false, null, null, null, null),
                    ItemUtils.item(Material.DIAMOND_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.IRON_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.REDSTONE_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.LAPIS_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.EMERALD_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.GOLD_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.COAL_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.DIAMOND_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.IRON_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.REDSTONE_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.LAPIS_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.EMERALD_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.GOLD_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.COAL_ORE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.QUARTZ_BLOCK, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.QUARTZ_BLOCK, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.QUARTZ_BLOCK, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 64, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 64, 4, false, false, null, null, null, null),
                    ItemUtils.item(Material.WOOD, null, 64, 5, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG, null, 64, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG_2, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.LOG_2, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 0, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 1, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 3, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 4, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 5, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 6, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 7, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 8, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 9, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 10, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 11, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 12, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 13, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 14, false, false, null, null, null, null),
                    ItemUtils.item(Material.STAINED_CLAY, null, 64, 15, false, false, null, null, null, null),
                    ItemUtils.item(Material.HARD_CLAY, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.PACKED_ICE, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.MELON_BLOCK, null, 64, 2, false, false, null, null, null, null),
                    ItemUtils.item(Material.SNOW_BLOCK, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.OBSIDIAN, null, 64, -1, false, false, null, null, null, null),
                    ItemUtils.item(Material.ENDER_STONE, null, 64, -1, false, false, null, null, null, null)
            };

            for (int j = 0; j < blocks.length; j++) {
                ItemStackList.Blocks[j][i] = blocks[j];
            }
            for (int j = 0; j < categoryblockeditor.length; j++) {
                ItemStackList.categoryblockeditor[j][i] = categoryblockeditor[j];
            }
            for (int j = 0; j < blockeditor.length; j++) {
                ItemStackList.blockeditor[j][i] = blockeditor[j];
            }
            for (int j = 0; j < sandstoneeditor.length; j++) {
                ItemStackList.sandstoneeditor[j][i] = sandstoneeditor[j];
            }
            for (int j = 0; j < wooleditor.length; j++) {
                ItemStackList.wooleditor[j][i] = wooleditor[j];
            }
            for (int j = 0; j < oreblockeditor.length; j++) {
                ItemStackList.oreblockeditor[j][i] = oreblockeditor[j];
            }
            for (int j = 0; j < oreeditor.length; j++) {
                ItemStackList.oreeditor[j][i] = oreeditor[j];
            }
            for (int j = 0; j < quartzeditor.length; j++) {
                ItemStackList.quartzeditor[j][i] = quartzeditor[j];
            }
            for (int j = 0; j < woodeditor.length; j++) {
                ItemStackList.woodeditor[j][i] = woodeditor[j];
            }
            for (int j = 0; j < clayeditor.length; j++) {
                ItemStackList.clayeditor[j][i] = clayeditor[j];
            }
        }
    }
}
