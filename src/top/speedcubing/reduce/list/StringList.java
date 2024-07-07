package top.speedcubing.reduce.list;

import top.speedcubing.server.lang.LangMessage;

public class StringList {
    public static LangMessage spec = new LangMessage("§bSpectate Menu", "§bSpectate Menu", "§bSpectate Menu", "§bSpectate Menu"
    );
    public static final LangMessage stickedit = new LangMessage("§bStick Skin", "§bStick Skin", "§bStick Skin", "§bStick Skin"
    );
    public static final LangMessage title = new LangMessage("§bNPC appearances", "§bNPC appearances", "§bNPC appearances", "§bNPC appearances"
    );
    public static final LangMessage input = new LangMessage("§aChange NPC's skin", "§aChange NPC's skin", "§aChange NPC's skin", "§aChange NPC's skin"
    );
    public static final LangMessage name = new LangMessage("§aChange NPC's DisplayName", "§aChange NPC's DisplayName", "§aChange NPC's DisplayName", "§aChange NPC's DisplayName"
    );
    public static final LangMessage resetname = new LangMessage("§aReset NPC's name to default", "§aReset NPC's name to default", "§aReset NPC's name to default", "§aReset NPC's name to default"
    );
    public static final LangMessage own = new LangMessage("§aUse your current skin", "§aUse your current skin", "§aUse your current skin", "§aUse your current skin"
    );
    public static final LangMessage mapselect = new LangMessage("§6Select Map", "§6Select Map", "§6Select Map", "§6Select Map"

    );
    public static final LangMessage prefedit = new LangMessage("§bPreferences", "§bPreferences", "§bPreferences", "§bPreferences"
    );
    public static LangMessage InputStringCancel2 = new LangMessage(
            "§cNPC name change is cancelled.",
            "§cNPC name change is cancelled.",
            "§cNPC name change is cancelled.",
            "§cNPC name change is cancelled."
    );

    public static LangMessage InputStringCancel = new LangMessage(
            "§cNPC skin change is cancelled.",
            "§cNPC skin change is cancelled.",
            "§cNPC skin change is cancelled.",
            "§cNPC skin change is cancelled."
    );

    public static LangMessage InputStringCancel3 = new LangMessage(
            "§cNPC Reach Settings is cancelled.",
            "§cNPC Reach Settings is cancelled.",
            "§cNPC Reach Settings is cancelled.",
            "§cNPC Reach Settings is cancelled."
    );
    public static LangMessage InputStringCancel4 = new LangMessage(
            "§cHorizontal Kb Settings is cancelled.",
            "§cHorizontal Kb Settings is cancelled.",
            "§cHorizontal Kb Settings is cancelled.",
            "§cHorizontal Kb Settings is cancelled."
    );


    public static LangMessage InputStringCancel5 = new LangMessage(
            "§cVertical Kb Settings is cancelled.",
            "§cVertical Kb Settings is cancelled.",
            "§cVertical Kb Settings is cancelled.",
            "§cVertical Kb Settings is cancelled."
    );

    public static LangMessage InputString = new LangMessage(
            "§aPlease input a valid username for NPC's skin in chat:",
            "§aPlease input a valid username for NPC's skin in chat:",
            "§aPlease input a valid username for NPC's skin in chat:",
            "§aPlease input a valid username for NPC's skin in chat:"
    );
    public static LangMessage InputString2 = new LangMessage(
            "§aPlease input a String for NPC's name in chat:",
            "§aPlease input a String for NPC's name in chat:",
            "§aPlease input a String for NPC's name in chat:",
            "§aPlease input a String for NPC's name in chat:"
    );

    public static final LangMessage npchitsettings = new LangMessage("§bHit Settings", "§bHit Settings", "§bHit Settings", "§bHit Settings");
    public static final LangMessage def = new LangMessage("§bVanilla KnockBack", "§bVanilla KnockBack", "§bVanilla KnockBack", "§bVanilla KnockBack");


    public static LangMessage veloinput = new LangMessage("§aPlease input a valid number (0.000 ~ 5.000)", "§aPlease input a valid number (0.000 ~ 5.000)", "§aPlease input a valid number (0.000 ~ 5.000)", "§aPlease input a valid number (0.000 ~ 5.000)");
    public static LangMessage veloinput2 = new LangMessage("§aPlease input a valid number (0.000 ~ 25.000)", "§aPlease input a valid number (0.000 ~ 25.000)", "§aPlease input a valid number (0.000 ~ 25.000)", "§aPlease input a valid number (0.000 ~ 25.000)");


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
