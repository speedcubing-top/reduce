package top.speedcubing.reduce.list;

import top.speedcubing.common.text.Msg;
import top.speedcubing.server.lang.Lang;
import top.speedcubing.server.lang.LanguageSystem;

public class StringList {
    public static final String PREFIX = Msg.prefix("Reduce", 'b');

    public static Lang spec = Lang.of("%lang_reduce_inv_spectate%");
    public static final Lang stickedit = Lang.of("%lang_reduce_inv_stick_skin%");
    public static final Lang prefedit = Lang.of("%lang_reduce_inv_preferences%");
    public static final Lang InputStringCancel2 = Lang.of("%lang_reduce_msg_cancel_name%");
    public static final Lang InputStringCancel = Lang.of("%lang_reduce_msg_cancel_skin%");
    public static final Lang InputStringCancel3 = Lang.of("%lang_reduce_msg_cancel_reach%");
    public static final Lang InputStringCancel4 = Lang.of("%lang_reduce_msg_cancel_hor%");
    public static final Lang InputStringCancel5 = Lang.of("%lang_reduce_msg_cancel_ver%");
    public static final Lang npchitsettings = Lang.of("%lang_reduce_inv_hit_settings%");

    public static String[][] ReachLore = new String[LanguageSystem.langCount][];
    public static String[][] HorLore = new String[LanguageSystem.langCount][];
    public static String[][] VerLore = new String[LanguageSystem.langCount][];
    public static String[][] mapname = new String[5][];
    public static String[][] ReduceSidebar = new String[LanguageSystem.langCount][];

    static {
        for (int map = 0; map < mapname.length; map++)
            mapname[map] = lang("reduce_map_name_" + map);
        for (int i = 0; i < LanguageSystem.langCount; i++) {
            String normal = get("reduce_lore_normal", i) + " %kb%";
            String vanilla = get("reduce_lore_vanilla", i) + " %kb2%";
            String def = get("reduce_lore_default", i);
            String leftRight = get("reduce_lore_left_right", i);
            ReachLore[i] = new String[]{get("reduce_lore_reach_title", i), "", normal, vanilla, "", def, "", leftRight};
            HorLore[i] = new String[]{get("reduce_lore_hor_title", i), "", normal, vanilla, "", def, "", leftRight};
            VerLore[i] = new String[]{get("reduce_lore_ver_title", i), "", normal, get("reduce_lore_vanilla_disabled", i), "", def, "", get("reduce_lore_left_right_disabled", i)};
            ReduceSidebar[i] = new String[]{"§3Reduce §7▶§b %t%", "§6speedcubing", "§6", get("reduce_sidebar_map", i), "§8»§b %str%", "§2", get("reduce_sidebar_blocks", i), "§8»§b ", "§0"};
        }
    }

    private static String get(String key, int lang) {
        return Lang.of("%lang_" + key + "%").getString(lang);
    }

    public static String[] lang(String key) {
        String[] result = new String[LanguageSystem.langCount];
        for (int i = 0; i < result.length; i++)
            result[i] = get(key, i);
        return result;
    }
}
