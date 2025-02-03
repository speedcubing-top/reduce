package top.speedcubing.reduce.list;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import top.speedcubing.lib.bukkit.inventory.InventoryUtils;
import top.speedcubing.lib.bukkit.inventory.ItemBuilder;
import top.speedcubing.reduce.Reduce;
import top.speedcubing.server.lang.LangItem;

public class ItemStackList {

    public static InventoryHolder SettingsHolder = () -> null;
    public static InventoryHolder KitEditorHolder = () -> null;
    public static InventoryHolder SpecHolder = () -> null;
    public static ItemStack[][] Sticks = new ItemStack[6][4];
    public static Inventory[] Settings = new Inventory[4];
    public static LangItem LeaveSpec = new LangItem(Material.SLIME_BALL).name("§bSpectate");
    public static Inventory[] InventoryKitEditor = new Inventory[4];
    public static ItemStack[] settings = new ItemStack[4];
    public static LangItem Spec = new LangItem(Material.COMPASS).name("§bSpectate");
    public static ItemStack[][][] stickeditor = new ItemStack[2][6][4];
    public static LangItem[][] Maps = new LangItem[2][Reduce.maptypes];
    public static ItemStack[][] prefereditor = new ItemStack[3][4];
    public static ItemStack[][] preferButton = new ItemStack[2][4];

    public static ItemStack[] Blocks = new ItemStack[81]; // blockedit-here

    public static LangItem[] categoryblockeditor = new LangItem[8];

    public static ItemStack[] blockeditor = new ItemStack[4];

    public static ItemStack[] wooleditor = new ItemStack[16];
    public static ItemStack[] sandstoneeditor = new ItemStack[6];
    public static ItemStack[] oreblockeditor = new ItemStack[7];
    public static ItemStack[] oreeditor = new ItemStack[7];
    public static ItemStack[] quartzeditor = new ItemStack[3];
    public static ItemStack[] woodeditor = new ItemStack[12];
    public static ItemStack[] clayeditor = new ItemStack[17];
    public static ItemStack[] specialeditor = new ItemStack[9]; // blockedit-here

    public void Load() {
        Maps[0][0] = new LangItem(Material.LOG).durability(3).name("§6Oneline");
        Maps[0][1] = new LangItem(Material.BED).name("§6BedWars");
        Maps[0][2] = new LangItem(Material.BARRIER).name("§6OneBlock");
        Maps[0][3] = new LangItem(Material.SMOOTH_BRICK).name("§6Chrama");
        Maps[0][4] = new LangItem(Material.SANDSTONE).name("§6Inclined");
        Maps[1][0] = new LangItem(Material.LOG).durability(3).glow().durability(3).name("§6Oneline").lore2("general_selected");
        Maps[1][1] = new LangItem(Material.BED).glow().name("§6BedWars").lore2("general_selected");
        Maps[1][2] = new LangItem(Material.BARRIER).glow().name("§6OneBlock").lore2("general_selected");
        Maps[1][3] = new LangItem(Material.SMOOTH_BRICK).glow().name("§6Chrama").lore2("general_selected");
        Maps[1][4] = new LangItem(Material.SANDSTONE).glow().name("§6Inclined").lore2("general_selected");
        {
            String[] stick = {"§6Stock", "§6Stick", "§6擊退棒", "§6击退棒"};
            for (int i = 0; i < 4; i++) {
                ItemStack[] sticks = {
                        new ItemBuilder(Material.STICK).name(stick[i]).ench(Enchantment.KNOCKBACK, 1).build(),
                        new ItemBuilder(Material.DIAMOND_SWORD).name(stick[i]).unBreak().hideAttr().ench(Enchantment.KNOCKBACK, 1).build(),
                        new ItemBuilder(Material.DIAMOND_HOE).name(stick[i]).unBreak().hideAttr().ench(Enchantment.KNOCKBACK, 1).build(),
                        new ItemBuilder(Material.BLAZE_ROD).name(stick[i]).ench(Enchantment.KNOCKBACK, 1).build(),
                        new ItemBuilder(Material.BONE).name(stick[i]).ench(Enchantment.KNOCKBACK, 1).build(),
                        new ItemBuilder(Material.FEATHER).name(stick[i]).ench(Enchantment.KNOCKBACK, 1).build()
                };
                for (int j = 0; j < sticks.length; j++) {
                    Sticks[j][i] = sticks[j];
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
                    {"§6NPC keep swinging", "§6NPC keep swinging", "§6NPC keep swinging", "§6NPC keep swinging"}
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
                        new ItemBuilder(Material.STICK).name(stick[i]).lore("§aSelected!").build(),
                        new ItemBuilder(Material.DIAMOND_SWORD).glow().name(sword[i]).hideAttr().lore("§aSelected!").build(),
                        new ItemBuilder(Material.DIAMOND_HOE).glow().name(hoe[i]).hideAttr().lore("§aSelected!").build(),
                        new ItemBuilder(Material.BLAZE_ROD).name(blaze[i]).lore("§aSelected!").build(),
                        new ItemBuilder(Material.BONE).name(bone[i]).lore("§aSelected!").build(),
                        new ItemBuilder(Material.FEATHER).name(feather[i]).lore("§aSelected!").build()
                };
                int length = sticks.length;
                for (int j = 0; j < length; j++) {
                    stickeditor[0][j][i] = sticks[j];
                    stickeditor[1][j][i] = sticks2[j];
                }
            }
        }


        LangItem[] categoryblockeditor = {
                new LangItem(Material.SANDSTONE).name("%lang_block_item_sandstone%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.WOOL).name("%lang_block_item_wool%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.DIAMOND_BLOCK).name("%lang_block_item_ore%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.DIAMOND_ORE).name("%lang_block_item_ores%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.QUARTZ_BLOCK).name("%lang_block_item_quartz%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.WOOD).name("%lang_block_item_wood%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.STAINED_CLAY).name("%lang_block_item_clay%").lore2("%lang_inventory_see_category%"),
                new LangItem(Material.BARRIER).name("%lang_block_item_special%").lore2("%lang_inventory_see_category%")

        };

        ItemStack[] blockeditor = {
                new ItemBuilder(Material.MELON_BLOCK).durability(2).amount(64).build(),
                new ItemBuilder(Material.SNOW_BLOCK).amount(64).build(),
                new ItemBuilder(Material.OBSIDIAN).amount(64).build(),
                new ItemBuilder(Material.ENDER_STONE).amount(64).build()
        };

        ItemStack[] sandstoneeditor = {
                new ItemBuilder(Material.SANDSTONE).build(),
                new ItemBuilder(Material.SANDSTONE).durability(1).build(),
                new ItemBuilder(Material.SANDSTONE).durability(2).build(),
                new ItemBuilder(Material.RED_SANDSTONE).build(),
                new ItemBuilder(Material.RED_SANDSTONE).durability(1).build(),
                new ItemBuilder(Material.RED_SANDSTONE).durability(2).build(),
        };

        ItemStack[] wooleditor = {
                new ItemBuilder(Material.WOOL).build(),
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
                new ItemBuilder(Material.QUARTZ_BLOCK).build(),
                new ItemBuilder(Material.QUARTZ_BLOCK).durability(1).build(),
                new ItemBuilder(Material.QUARTZ_BLOCK).durability(2).build(),
        };
        ItemStack[] woodeditor = {
                new ItemBuilder(Material.WOOD).build(),
                new ItemBuilder(Material.WOOD).durability(1).build(),
                new ItemBuilder(Material.WOOD).durability(2).build(),
                new ItemBuilder(Material.WOOD).durability(3).build(),
                new ItemBuilder(Material.WOOD).durability(4).build(),
                new ItemBuilder(Material.WOOD).durability(5).build(),
                new ItemBuilder(Material.LOG).build(),
                new ItemBuilder(Material.LOG).durability(1).build(),
                new ItemBuilder(Material.LOG).durability(2).build(),
                new ItemBuilder(Material.LOG).durability(3).build(),
                new ItemBuilder(Material.LOG_2).build(),
                new ItemBuilder(Material.LOG_2).durability(1).build(),
        };


        ItemStack[] clayeditor = {
                new ItemBuilder(Material.STAINED_CLAY).build(),
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

        // blockedit-here
        ItemStack[] specialeditor = {
                new ItemBuilder(Material.BARRIER).build(),
                new ItemBuilder(Material.ENDER_PORTAL_FRAME).build(),
                new ItemBuilder(Material.COMMAND).build(),
                new ItemBuilder(Material.HOPPER).build(),
                new ItemBuilder(Material.BEDROCK).build(),
                new ItemBuilder(Material.IRON_FENCE).build(),
                new ItemBuilder(Material.BIRCH_FENCE).build(),
                new ItemBuilder(Material.COBBLE_WALL).build(),
                new ItemBuilder(Material.WOOL).durability(0).ench(Enchantment.DURABILITY, 1).hideEnch().name("§cR§6A§eI§aN§bB§9O§5W").build(),
        };


        ItemStack[] blocks = {
                new ItemBuilder(Material.SANDSTONE).amount(64).build(),
                new ItemBuilder(Material.SANDSTONE).durability(1).amount(64).build(),
                new ItemBuilder(Material.SANDSTONE).durability(2).amount(64).build(),
                new ItemBuilder(Material.RED_SANDSTONE).amount(64).build(),
                new ItemBuilder(Material.RED_SANDSTONE).durability(1).amount(64).build(),
                new ItemBuilder(Material.RED_SANDSTONE).durability(2).amount(64).build(),
                new ItemBuilder(Material.WOOL).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(1).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(2).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(3).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(4).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(5).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(6).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(7).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(8).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(9).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(10).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(11).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(12).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(13).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(14).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(15).amount(64).build(),
                new ItemBuilder(Material.DIAMOND_BLOCK).amount(64).build(),
                new ItemBuilder(Material.IRON_BLOCK).amount(64).build(),
                new ItemBuilder(Material.REDSTONE_BLOCK).amount(64).build(),
                new ItemBuilder(Material.LAPIS_BLOCK).amount(64).build(),
                new ItemBuilder(Material.EMERALD_BLOCK).amount(64).build(),
                new ItemBuilder(Material.GOLD_BLOCK).amount(64).build(),
                new ItemBuilder(Material.COAL_BLOCK).amount(64).build(),
                new ItemBuilder(Material.DIAMOND_ORE).amount(64).build(),
                new ItemBuilder(Material.IRON_ORE).amount(64).build(),
                new ItemBuilder(Material.REDSTONE_ORE).amount(64).build(),
                new ItemBuilder(Material.LAPIS_ORE).amount(64).build(),
                new ItemBuilder(Material.EMERALD_ORE).amount(64).build(),
                new ItemBuilder(Material.GOLD_ORE).amount(64).build(),
                new ItemBuilder(Material.COAL_ORE).amount(64).build(),
                new ItemBuilder(Material.QUARTZ_BLOCK).amount(64).build(),
                new ItemBuilder(Material.QUARTZ_BLOCK).durability(1).amount(64).build(),
                new ItemBuilder(Material.QUARTZ_BLOCK).durability(2).amount(64).build(),
                new ItemBuilder(Material.WOOD).amount(64).build(),
                new ItemBuilder(Material.WOOD).durability(1).amount(64).build(),
                new ItemBuilder(Material.WOOD).durability(2).amount(64).build(),
                new ItemBuilder(Material.WOOD).durability(3).amount(64).build(),
                new ItemBuilder(Material.WOOD).durability(4).amount(64).build(),
                new ItemBuilder(Material.WOOD).durability(5).amount(64).build(),
                new ItemBuilder(Material.LOG).amount(64).build(),
                new ItemBuilder(Material.LOG).durability(1).amount(64).build(),
                new ItemBuilder(Material.LOG).durability(2).amount(64).build(),
                new ItemBuilder(Material.LOG).durability(3).amount(64).build(),
                new ItemBuilder(Material.LOG_2).amount(64).build(),
                new ItemBuilder(Material.LOG_2).durability(1).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(1).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(2).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(3).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(4).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(5).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(6).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(7).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(8).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(9).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(10).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(11).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(12).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(13).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(14).amount(64).build(),
                new ItemBuilder(Material.STAINED_CLAY).durability(15).amount(64).build(),
                new ItemBuilder(Material.HARD_CLAY).amount(64).build(),

                // blockedit-here
                new ItemBuilder(Material.BARRIER).amount(64).build(),
                new ItemBuilder(Material.ENDER_PORTAL_FRAME).amount(64).build(),
                new ItemBuilder(Material.COMMAND).amount(64).build(),
                new ItemBuilder(Material.HOPPER).amount(64).build(),
                new ItemBuilder(Material.BEDROCK).amount(64).build(),
                new ItemBuilder(Material.IRON_FENCE).amount(64).build(),
                new ItemBuilder(Material.BIRCH_FENCE).amount(64).build(),
                new ItemBuilder(Material.COBBLE_WALL).amount(64).build(),
                new ItemBuilder(Material.WOOL).durability(0).ench(Enchantment.DURABILITY, 1).hideEnch().name("§cR§6A§eI§aN§bB§9O§5W").amount(64).build(),

                new ItemBuilder(Material.MELON_BLOCK).durability(2).amount(64).build(),
                new ItemBuilder(Material.SNOW_BLOCK).amount(64).build(),
                new ItemBuilder(Material.OBSIDIAN).amount(64).build(),
                new ItemBuilder(Material.ENDER_STONE).amount(64).build()
        };

        System.arraycopy(blocks, 0, ItemStackList.Blocks, 0, blocks.length);
        System.arraycopy(categoryblockeditor, 0, ItemStackList.categoryblockeditor, 0, categoryblockeditor.length);
        System.arraycopy(blockeditor, 0, ItemStackList.blockeditor, 0, blockeditor.length);
        System.arraycopy(sandstoneeditor, 0, ItemStackList.sandstoneeditor, 0, sandstoneeditor.length);
        System.arraycopy(wooleditor, 0, ItemStackList.wooleditor, 0, wooleditor.length);
        System.arraycopy(oreblockeditor, 0, ItemStackList.oreblockeditor, 0, oreblockeditor.length);
        System.arraycopy(oreeditor, 0, ItemStackList.oreeditor, 0, oreeditor.length);
        System.arraycopy(quartzeditor, 0, ItemStackList.quartzeditor, 0, quartzeditor.length);
        System.arraycopy(woodeditor, 0, ItemStackList.woodeditor, 0, woodeditor.length);
        System.arraycopy(clayeditor, 0, ItemStackList.clayeditor, 0, clayeditor.length);
        System.arraycopy(specialeditor, 0, ItemStackList.specialeditor, 0, specialeditor.length);
    }
}
