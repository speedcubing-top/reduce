package top.speedcubing.reduce.list;

import top.speedcubing.server.lang.Lang;

public class StringList {
    public static Lang spec = Lang.of("§bSpectate Menu");
    public static final Lang stickedit = Lang.of("§bStick Skin");
    public static final Lang prefedit = Lang.of("§bPreferences");
    public static final Lang InputStringCancel2 = Lang.of("§cNPC name change is cancelled.");
    public static final Lang InputStringCancel = Lang.of("§cNPC skin change is cancelled.");
    public static final Lang InputStringCancel3 = Lang.of("§cNPC Reach Settings is cancelled.");
    public static final Lang InputStringCancel4 = Lang.of("§cHorizontal Kb Settings is cancelled.");
    public static final Lang InputStringCancel5 = Lang.of("§cVertical Kb Settings is cancelled.");
    public static final Lang npchitsettings = Lang.of("§bHit Settings");


    public static String[][] ReachLore = {
            {"§bReach Distance Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"},
            {"§bReach Distance Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"},
            {"§bReach Distance Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"},
            {"§bReach Distance Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"}
    };


    public static String[][] HorLore = {
            {"§bHorizontal Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"},
            {"§bHorizontal Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"},
            {"§bHorizontal Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"},
            {"§bHorizontal Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§e %kb2%", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: Vanilla"}
    };


    public static String[][] VerLore = {
            {"§bVertical Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§c DISABLED", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: DISABLED"},
            {"§bVertical Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§c DISABLED", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: DISABLED"},
            {"§bVertical Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§c DISABLED", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: DISABLED"},
            {"§bVertical Kb Multiplier", "", "§dNormal:§e %kb%", "§dVanilla:§c DISABLED", "", "§cdefault: 1.000", "", "§aLeft: Normal§8 , §cRight: DISABLED"}
    };

    public static String[][] mapname = {
            {"§6Oneline", "§6Oneline", "§6Oneline", "§6Oneline"},
            {"§6BedWars", "§6BedWars", "§6BedWars", "§6BedWars"},
            {"§6OneBlock", "§6OneBlock", "§6OneBlock", "§6OneBlock"},
            {"§6Chrama", "§6Chrama", "§6Chrama", "§6Chrama"},
            {"§6Inclined", "§6Inclined", "§6Inclined", "§6Inclined"}};
    public static String[][] ReduceSidebar = {
            {"§3Reduce §7▶§b %t%", "§6speedcubing", "§6", "§aMap:", "§8»§b %str%", "§2", "§aBlock placed:", "§8»§b ", "§0"},
            {"§3Reduce §7▶§b %t%", "§6speedcubing", "§6", "§aMap:", "§8»§b %str%", "§2", "§aBlock placed:", "§8»§b ", "§0"},
            {"§3Reduce §7▶§b %t%", "§6speedcubing", "§6", "§aMap:", "§8»§b %str%", "§2", "§aBlock placed:", "§8»§b ", "§0"},
            {"§3Reduce §7▶§b %t%", "§6speedcubing", "§6", "§aMap:", "§8»§b %str%", "§2", "§aBlock placed:", "§8»§b ", "§0"}
    };
}
