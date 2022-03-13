package speedcubing.List;

import cubing.bukkit.InventoryUtils;
import cubing.bukkit.ItemBuilder;
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
            Maps[0][0][i] = new ItemBuilder(Material.LOG).durability(3).name(StringList.mapname[0][i]).build();
            Maps[0][1][i] = new ItemBuilder(Material.BED).name(StringList.mapname[1][i]).build();
            Maps[0][2][i] = new ItemBuilder(Material.BARRIER).name(StringList.mapname[2][i]).build();
            Maps[0][3][i] = new ItemBuilder(Material.SMOOTH_BRICK).durability(1).name(StringList.mapname[3][i]).build();
            Maps[1][0][i] = new ItemBuilder(Material.LOG).glow().durability(3).name(StringList.mapname[0][i]).lore(GlobalString.selected[i]).build();
            Maps[1][1][i] = new ItemBuilder(Material.BED).glow().name(StringList.mapname[1][i]).lore(GlobalString.selected[i]).build();
            Maps[1][2][i] = new ItemBuilder(Material.BARRIER).glow().name(StringList.mapname[2][i]).lore(GlobalString.selected[i]).build();
            Maps[1][3][i] = new ItemBuilder(Material.SMOOTH_BRICK).glow().name(StringList.mapname[3][i]).lore(GlobalString.selected[i]).build();
            Spec[i] = new ItemBuilder(Material.COMPASS).name(specitem[i]).build();
            LeaveSpec[i] = new ItemBuilder(Material.SLIME_BALL).name(leavespec[i]).build();
        }
        {
            String[] stick = {"§6Stock", "§6Stick", "§6擊退棒", "§6击退棒"};
            String[] block = {"§6Blöcke", "§6Blocks", "§6方塊", "§6方块"};
            for (int i = 0; i < 4; i++) {
                ItemStack[] sticks = {
                        new ItemBuilder(Material.STICK).name(stick[i]).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build(),
                        new ItemBuilder(Material.DIAMOND_SWORD).name(stick[i]).unBreak().hideAttr().ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build(),
                        new ItemBuilder(Material.DIAMOND_HOE).name(stick[i]).unBreak().hideAttr().ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build(),
                        new ItemBuilder(Material.BLAZE_ROD).name(stick[i]).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build(),
                        new ItemBuilder(Material.BONE).name(stick[i]).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build(),
                        new ItemBuilder(Material.FEATHER).name(stick[i]).ench(new Enchantment[]{Enchantment.KNOCKBACK}, new int[]{1}).build()
                };
                for (int j = 0; j < sticks.length; j++) {
                    Sticks[j][i] = sticks[j];
                }

                ItemStack[] blocks = {
                        new ItemBuilder(Material.SANDSTONE).amount(64).durability(2).name(block[i]).build(),
                        new ItemBuilder(Material.RED_SANDSTONE).amount(64).durability(2).name(block[i]).build(),
                        new ItemBuilder(Material.GOLD_BLOCK).amount(64).name(block[i]).build(),
                        new ItemBuilder(Material.DIAMOND_BLOCK).amount(64).name(block[i]).build(),
                        new ItemBuilder(Material.EMERALD_BLOCK).amount(64).name(block[i]).build(),
                        new ItemBuilder(Material.PRISMARINE).amount(64).durability(2).name(block[i]).build(),
                        new ItemBuilder(Material.PACKED_ICE).amount(64).name(block[i]).build(),
                        new ItemBuilder(Material.QUARTZ_BLOCK).amount(64).name(block[i]).build()
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
                settings[i] = new ItemBuilder(Material.INK_SACK).durability(1).name(display[i]).build();
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
                Settings[i].setItem(10, new ItemBuilder(Material.STICK).name(hit[i]).build());
                Settings[i].setItem(11, new ItemBuilder(Material.CHEST).durability(3).name(inv[i]).build());
                Settings[i].setItem(12, new ItemBuilder(Material.SANDSTONE).durability(2).name(block[i]).build());
                Settings[i].setItem(13, new ItemBuilder(Material.BLAZE_ROD).name(stick[i]).build());
                Settings[i].setItem(14, new ItemBuilder(Material.EMPTY_MAP).name(map[i]).build());
                Settings[i].setItem(15, new ItemBuilder(Material.SKULL_ITEM).durability(4).name(skin[i]).build());
                Settings[i].setItem(16, new ItemBuilder(Material.REDSTONE_COMPARATOR).name(other[i]).build());
            }
        }
        {
            String[] title = {"§b Inventar", "§cReduce Inventory", "§cReduce 物品欄", "§cReduce 物品栏"};
            String[] save = {"§aÄnderungen speichern", "§aSave Changes", "§a儲存設定", "§a储存设置"};
            String[] unsave = {"§cnicht gespeicherte Änderungen", "§cUnSave Changes", "§c不儲存設定", "§c不储存设置"};
            for (int i = 0; i < 4; i++) {
                InventoryKitEditor[i] = Bukkit.createInventory(KitEditorHolder, 9, title[i]);
                InventoryUtils.fillItem(InventoryKitEditor[i], 0, 9, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("§a").build());
                InventoryKitEditor[i].setItem(3, new ItemBuilder(Material.INK_SACK).durability(10).name(save[i]).build());
                InventoryKitEditor[i].setItem(5, new ItemBuilder(Material.INK_SACK).durability(1).name(unsave[i]).build());
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
                        new ItemBuilder(Material.REDSTONE).name(prefs[0][i]).build(),
                        new ItemBuilder(Material.NOTE_BLOCK).durability(3).name(prefs[1][i]).build(),
                        new ItemBuilder(Material.STICK).glow().name(prefs[2][i]).build(),
                };
                ItemStack[] pref = {
                        new ItemBuilder(Material.INK_SACK).durability(10).name(active[i]).build(),
                        new ItemBuilder(Material.INK_SACK).durability(1).name(deactive[i]).build()
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
                        new ItemBuilder(Material.STICK).name(stick[i]).build(),
                        new ItemBuilder(Material.DIAMOND_SWORD).name(sword[i]).hideAttr().build(),
                        new ItemBuilder(Material.DIAMOND_HOE).name(hoe[i]).hideAttr().build(),
                        new ItemBuilder(Material.BLAZE_ROD).name(blaze[i]).build(),
                        new ItemBuilder(Material.BONE).name(bone[i]).build(),
                        new ItemBuilder(Material.FEATHER).name(feather[i]).build()
                };
                ItemStack[] sticks2 = {
                        new ItemBuilder(Material.STICK).name(stick[i]).lore(GlobalString.selected[i]).build(),
                        new ItemBuilder(Material.DIAMOND_SWORD).glow().name(sword[i]).hideAttr().lore(GlobalString.selected[i]).build(),
                        new ItemBuilder(Material.DIAMOND_HOE).glow().name(hoe[i]).hideAttr().lore(GlobalString.selected[i]).build(),
                        new ItemBuilder(Material.BLAZE_ROD).name(blaze[i]).lore(GlobalString.selected[i]).build(),
                        new ItemBuilder(Material.BONE).name(bone[i]).lore(GlobalString.selected[i]).build(),
                        new ItemBuilder(Material.FEATHER).name(feather[i]).lore(GlobalString.selected[i]).build()
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
                    new ItemBuilder(Material.SANDSTONE).name(block[0][i]).lore(GlobalString.category[i]).build(),
                    new ItemBuilder(Material.WOOL).name(block[1][i]).lore(GlobalString.category[i]).build(),
                    new ItemBuilder(Material.DIAMOND_BLOCK).name(block[2][i]).lore(GlobalString.category[i]).build(),
                    new ItemBuilder(Material.DIAMOND_ORE).name(block[3][i]).lore(GlobalString.category[i]).build(),
                    new ItemBuilder(Material.QUARTZ_BLOCK).name(block[4][i]).lore(GlobalString.category[i]).build(),
                    new ItemBuilder(Material.WOOD).name(block[5][i]).lore(GlobalString.category[i]).build(),
                    new ItemBuilder(Material.STAINED_CLAY).name(block[6][i]).lore(GlobalString.category[i]).build()
            };

            ItemStack[] blockeditor = {
                    new ItemBuilder(Material.PACKED_ICE).build(),
                    new ItemBuilder(Material.MELON_BLOCK).build(),
                    new ItemBuilder(Material.SNOW_BLOCK).build(),
                    new ItemBuilder(Material.OBSIDIAN).build(),
                    new ItemBuilder(Material.ENDER_STONE).build()
            };

            ItemStack[] sandstoneeditor = {
                    new ItemBuilder(Material.SANDSTONE).durability(0).build(),
                    new ItemBuilder(Material.SANDSTONE).durability(1).build(),
                    new ItemBuilder(Material.SANDSTONE).durability(2).build(),
                    new ItemBuilder(Material.RED_SANDSTONE).durability(0).build(),
                    new ItemBuilder(Material.RED_SANDSTONE).durability(1).build(),
                    new ItemBuilder(Material.RED_SANDSTONE).durability(2).build(),
            };

            ItemStack[] wooleditor = {
                    new ItemBuilder(Material.WOOL).durability(0).build(),
                    new ItemBuilder(Material.WOOL).durability(1).build(),
                    new ItemBuilder(Material.WOOL).durability(2).build(),
                    new ItemBuilder(Material.WOOL).durability(3).build(),
                    new ItemBuilder(Material.WOOL).durability(4).build(),
                    new ItemBuilder(Material.WOOL).durability(5).build(),
                    new ItemBuilder(Material.WOOL).durability(6).build(),
                    new ItemBuilder(Material.WOOL).durability(7).build(),
                    new ItemBuilder(Material.WOOL).durability(8).build(),
                    new ItemBuilder(Material.WOOL).durability(9).build(),
                    new ItemBuilder(Material.WOOL).durability(10).build(),
                    new ItemBuilder(Material.WOOL).durability(11).build(),
                    new ItemBuilder(Material.WOOL).durability(12).build(),
                    new ItemBuilder(Material.WOOL).durability(13).build(),
                    new ItemBuilder(Material.WOOL).durability(14).build(),
                    new ItemBuilder(Material.WOOL).durability(15).build()
            };
            ItemStack[] oreblockeditor = {
                    new ItemBuilder(Material.DIAMOND_BLOCK).build(),
                    new ItemBuilder(Material.IRON_BLOCK).build(),
                    new ItemBuilder(Material.REDSTONE_BLOCK).build(),
                    new ItemBuilder(Material.LAPIS_BLOCK).build(),
                    new ItemBuilder(Material.EMERALD_BLOCK).build(),
                    new ItemBuilder(Material.GOLD_BLOCK).build(),
                    new ItemBuilder(Material.COAL_BLOCK).build(),
            };
            ItemStack[] oreeditor = {
                    new ItemBuilder(Material.DIAMOND_ORE).build(),
                    new ItemBuilder(Material.IRON_ORE).build(),
                    new ItemBuilder(Material.REDSTONE_ORE).build(),
                    new ItemBuilder(Material.LAPIS_ORE).build(),
                    new ItemBuilder(Material.EMERALD_ORE).build(),
                    new ItemBuilder(Material.GOLD_ORE).build(),
                    new ItemBuilder(Material.COAL_ORE).build(),
            };
            ItemStack[] quartzeditor = {
                    new ItemBuilder(Material.QUARTZ_BLOCK).durability(0).build(),
                    new ItemBuilder(Material.QUARTZ_BLOCK).durability(1).build(),
                    new ItemBuilder(Material.QUARTZ_BLOCK).durability(2).build(),
            };
            ItemStack[] woodeditor = {
                    new ItemBuilder(Material.WOOD).durability(0).build(),
                    new ItemBuilder(Material.WOOD).durability(1).build(),
                    new ItemBuilder(Material.WOOD).durability(2).build(),
                    new ItemBuilder(Material.WOOD).durability(3).build(),
                    new ItemBuilder(Material.WOOD).durability(4).build(),
                    new ItemBuilder(Material.WOOD).durability(5).build(),
                    new ItemBuilder(Material.LOG).durability(0).build(),
                    new ItemBuilder(Material.LOG).durability(1).build(),
                    new ItemBuilder(Material.LOG).durability(2).build(),
                    new ItemBuilder(Material.LOG).durability(3).build(),
                    new ItemBuilder(Material.LOG_2).durability(0).build(),
                    new ItemBuilder(Material.LOG_2).durability(1).build(),
            };

            ItemStack[] clayeditor = {
                    new ItemBuilder(Material.STAINED_CLAY).durability(0).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(1).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(2).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(3).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(4).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(5).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(6).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(7).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(8).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(9).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(10).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(11).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(12).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(13).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(14).build(),
                    new ItemBuilder(Material.STAINED_CLAY).durability(15).build(),
                    new ItemBuilder(Material.HARD_CLAY).build()
            };
            ItemStack[] blocks = {
                    new ItemBuilder(Material.SANDSTONE).amount(64).durability(0).build(),
                    new ItemBuilder(Material.SANDSTONE).amount(64).durability(1).build(),
                    new ItemBuilder(Material.SANDSTONE).amount(64).durability(2).build(),
                    new ItemBuilder(Material.RED_SANDSTONE).amount(64).durability(0).build(),
                    new ItemBuilder(Material.RED_SANDSTONE).amount(64).durability(1).build(),
                    new ItemBuilder(Material.RED_SANDSTONE).amount(64).durability(2).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(0).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(1).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(2).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(3).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(4).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(5).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(6).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(7).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(8).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(9).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(10).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(11).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(12).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(13).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(14).build(),
                    new ItemBuilder(Material.WOOL).amount(64).durability(15).build(),
                    new ItemBuilder(Material.DIAMOND_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.IRON_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.REDSTONE_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.LAPIS_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.EMERALD_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.GOLD_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.COAL_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.DIAMOND_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.IRON_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.REDSTONE_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.LAPIS_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.EMERALD_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.GOLD_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.COAL_ORE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.QUARTZ_BLOCK).amount(64).durability(0).build(),
                    new ItemBuilder(Material.QUARTZ_BLOCK).amount(64).durability(1).build(),
                    new ItemBuilder(Material.QUARTZ_BLOCK).amount(64).durability(2).build(),
                    new ItemBuilder(Material.WOOD).amount(64).durability(0).build(),
                    new ItemBuilder(Material.WOOD).amount(64).durability(1).build(),
                    new ItemBuilder(Material.WOOD).amount(64).durability(2).build(),
                    new ItemBuilder(Material.WOOD).amount(64).durability(3).build(),
                    new ItemBuilder(Material.WOOD).amount(64).durability(4).build(),
                    new ItemBuilder(Material.WOOD).amount(64).durability(5).build(),
                    new ItemBuilder(Material.LOG).amount(64).durability(0).build(),
                    new ItemBuilder(Material.LOG).amount(64).durability(1).build(),
                    new ItemBuilder(Material.LOG).amount(64).durability(2).build(),
                    new ItemBuilder(Material.LOG).amount(64).durability(3).build(),
                    new ItemBuilder(Material.LOG_2).amount(64).durability(0).build(),
                    new ItemBuilder(Material.LOG_2).amount(64).durability(1).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(0).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(1).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(2).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(3).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(4).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(5).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(6).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(7).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(8).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(9).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(10).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(11).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(12).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(13).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(14).build(),
                    new ItemBuilder(Material.STAINED_CLAY).amount(64).durability(15).build(),
                    new ItemBuilder(Material.HARD_CLAY).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.PACKED_ICE).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.MELON_BLOCK).amount(64).durability(2).build(),
                    new ItemBuilder(Material.SNOW_BLOCK).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.OBSIDIAN).amount(64).durability(-1).build(),
                    new ItemBuilder(Material.ENDER_STONE).amount(64).durability(-1).build()
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
