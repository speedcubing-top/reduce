package speedcubing.BukkitEventListener;


import com.mojang.authlib.properties.Property;
import com.sun.xml.internal.ws.api.model.MEP;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
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
import speedcubing.lib.bukkit.InventoryUtils;
import speedcubing.lib.bukkit.ItemBuilder;
import speedcubing.lib.bukkit.PlayerUtils;
import speedcubing.lib.bukkit.WorldUtils;
import speedcubing.server.config;
import speedcubing.server.libs.GlobalString;
import speedcubing.server.libs.User;
import speedcubing.server.speedcubingServer;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class InventoryClick implements Listener {

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        int RawSlot = e.getRawSlot();
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
                            Member member = Member.getMember(player.getUniqueId());
                            Inventory inv = Bukkit.createInventory(ItemStackList.HitSettingsHolder, 27, StringList.npchitsettings[member.user.lang]);
                            String[] reach = StringList.ReachLore[member.user.lang];
                            String[] hor = StringList.HorLore[member.user.lang];
                            String[] ver = StringList.VerLore[member.user.lang];
                            Integer[] cache = member.integerCache;
                            inv.setItem(11, new ItemBuilder(Material.STICK).name(reach[0]).lore(reach[1], reach[2].replace("%kb%", String.format("%.3f", cache[5] / 1000D)), reach[3].replace("%kb2%", String.format("%.3f", cache[9] / 1000D)), reach[4], reach[5], reach[6], reach[7]).build());
                            inv.setItem(12, new ItemBuilder(Material.STICK).name(hor[0]).lore(hor[1], hor[2].replace("%kb%", String.format("%.3f", cache[6] / 1000D)), hor[3].replace("%kb2%", String.format("%.3f", cache[10] / 1000D)), hor[4], hor[5], hor[6], hor[7]).build());
                            inv.setItem(13, new ItemBuilder(Material.STICK).name(ver[0]).lore(ver[1], ver[2].replace("%kb%", String.format("%.3f", cache[7] / 1000D)), ver[3], ver[4], ver[5], ver[6], ver[7]).build());
                            inv.setItem(15, new ItemBuilder(Material.FEATHER).name(StringList.def[member.user.lang]).lore(member.booleanCache[3] ? "§cClick to turn off" : "§aClick to turn on").build());
                            player.openInventory(inv);
                        }
                        break;
                        case 11: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            Inventory inv = player.getInventory();
                            player.openInventory(ItemStackList.InventoryKitEditor[member.user.lang]);
                            InventoryUtils.fillItem(inv, 9, 36, Reduce.Invalid[member.user.lang]);
                            member.kitedit = true;
                        }
                        break;
                        case 12:
                            BlockEditor.createMainPage(player);
                            break;
                        case 13: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int x = Member.getMember(player.getUniqueId()).integerCache[3];
                            Inventory inv = Bukkit.createInventory(ItemStackList.StickSkinHolder, 27, StringList.stickedit[member.user.lang]);
                            for (int i = 0; i < 6; i++) {
                                inv.setItem(i + 10, ItemStackList.stickeditor[0][i][member.user.lang]);
                            }
                            org.bukkit.inventory.ItemStack selected = ItemStackList.stickeditor[1][x][member.user.lang];
                            inv.setItem(x + 10, selected);
                            player.openInventory(inv);
                        }
                        break;
                        case 14: {
                            Member member = Member.getMember(player.getUniqueId());
                            int m = member.Map;
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            Inventory inv = Bukkit.createInventory(ItemStackList.MapHolder, 18, StringList.mapselect[member.user.lang]);
                            ItemStack[][] stacks = ItemStackList.Maps[0];
                            inv.setItem(0, stacks[0][member.user.lang]);
                            inv.setItem(1, stacks[1][member.user.lang]);
                            inv.setItem(2, stacks[2][member.user.lang]);
                            inv.setItem(3, stacks[3][member.user.lang]);
                            inv.setItem(m, ItemStackList.Maps[1][m][member.user.lang]);
                            player.openInventory(inv);
                        }
                        break;
                        case 15: {
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = Member.getMember(player.getUniqueId()).user.lang;
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
                            Member member = Member.getMember(player.getUniqueId());
                            int lang = member.user.lang;
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
                    if (RawSlot <= 35 || (e.getAction() == InventoryAction.PICKUP_HALF && e.getCurrentItem().getAmount() != 1))
                        e.setCancelled(true);
                    switch (RawSlot) {
                        case 3:
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            if (e.getCursor().getType() == Material.AIR) {
                                Member member = Member.getMember(uuid);
                                player.sendMessage(GlobalString.SavedSettings[member.user.lang]);
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
                            Member member = Member.getMember(player.getUniqueId());
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = member.booleanCache[0];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][member.user.lang]);
                            member.booleanCache[0] = !b;
                        }
                        break;
                        case 22: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = member.booleanCache[1];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][member.user.lang]);
                            member.booleanCache[1] = !b;
                        }
                        break;
                        case 23: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = member.booleanCache[2];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][member.user.lang]);
                            member.booleanCache[2] = !b;
                        }
                        break;
                    }
                } else if (holder == ItemStackList.StickSkinHolder) {
                    e.setCancelled(true);
                    if (RawSlot >= 10 && RawSlot <= 15) {
                        Member member = Member.getMember(player.getUniqueId());
                        int x = RawSlot - 10;
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                        member.integerCache[3] = x;
                        player.closeInventory();
                        player.sendMessage(GlobalString.SavedSettings[member.user.lang]);
                        player.getInventory().setItem(member.integerCache[0], ItemStackList.Sticks[x][member.user.lang]);
                    }
                } else if (holder == ItemStackList.MapHolder) {
                    e.setCancelled(true);
                    Member member = Member.getMember(player.getUniqueId());
                    if (RawSlot < Reduce.maptypes && RawSlot != member.Map) { //swap map
                        int newroom = -1;
                        for (int h = 1; h < Integer.MAX_VALUE; h++) {
                            if (!Reduce.Rooms[RawSlot].contains(h)) {
                                Reduce.Rooms[RawSlot].add(h);
                                newroom = h;
                                break;
                            }
                        }
                        int map = member.Map;
                        int coord = member.Coord;
                        int oldroom = Reduce.coordToRoom(coord);
                        World world = player.getWorld();
                        world.getChunkAt(new Location(world, 500 * map + 0.5, 100, coord + 0.5)).load();
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
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(member.Holo.getId()));
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(member.EntityPlayers.getId()));
                        member.clearBlocks();
                        Reduce.removeFromGUI(player, map, null);
                        player.closeInventory();
                        new Member(player, RawSlot, newroom, member.booleanCache, member.integerCache, member.Spectators);
                        Reduce.Rooms[map].remove(oldroom);
                        WorldUtils.fillAir(-10 + map * 500, 90, -10 + coord, 10 + map * 500, 110, 10 + coord, player.getWorld());
                    }
                } else if (holder == ItemStackList.SkinHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 10: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.sendMessage(StringList.InputString[member.user.lang]);
                            player.closeInventory();
                            member.setSkin = true;
                            speedcubingServer.node(true, player.getUniqueId(), member.user.tcpPort);
                        }
                        break;
                        case 11: {
                            Member member = Member.getMember(player.getUniqueId());
                            EntityPlayer entityPlayer = member.EntityPlayers;
                            Property property = ((CraftPlayer) player).getProfile().getProperties().get("textures").iterator().next();
                            member.setSkin(player, entityPlayer, new String[]{property.getValue(), property.getSignature()});
                            player.closeInventory();
                        }
                        break;
                        case 13: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.sendMessage(StringList.InputString2[member.user.lang]);
                            player.closeInventory();
                            member.setName = true;
                            speedcubingServer.node(true, player.getUniqueId(), member.user.tcpPort);
                        }
                        break;
                        case 14: {
                            Member member = Member.getMember(player.getUniqueId());
                            EntityArmorStand stand = member.Holo;
                            stand.setCustomName("§6" + config.SERVERIP);
                            Reduce.sendPackets(((CraftPlayer) player).getHandle().playerConnection, member.Spectators, new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
                            player.closeInventory();
                        }
                        break;
                    }
                } else if (holder == ItemStackList.HitSettingsHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 11: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.closeInventory();
                            speedcubingServer.node(true, player.getUniqueId(), member.user.tcpPort);
                            player.sendMessage(StringList.veloinput2[member.user.lang]);
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
                            Member member = Member.getMember(player.getUniqueId());
                            player.closeInventory();
                            player.sendMessage(StringList.veloinput[member.user.lang]);
                            speedcubingServer.node(true, player.getUniqueId(), member.user.tcpPort);
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
                                    Member member = Member.getMember(player.getUniqueId());
                                    player.closeInventory();
                                    member.verSettings = true;
                                    player.sendMessage(StringList.veloinput[member.user.lang]);
                                    speedcubingServer.node(true, player.getUniqueId(), member.user.tcpPort);
                                    break;
                            }
                        }
                        break;
                        case 15: {
                            Member member = Member.getMember(player.getUniqueId());
                            player.closeInventory();
                            member.booleanCache[3] = !member.booleanCache[3];
                        }
                        break;
                    }
                } else if (holder == ItemStackList.SpecHolder) {
                    e.setCancelled(true);
                    if (RawSlot < Reduce.totalName.size()) {
                        //join spectate
                        UUID uuid = player.getUniqueId();
                        boolean isSpec = player.getGameMode() == GameMode.ADVENTURE;
                        Member member = Member.getMember(uuid);
                        if (isSpec && Reduce.totalName.get(RawSlot).equals(Bukkit.getPlayer(member.target).getName()))
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
                        UUID target = Reduce.specStorage[map].get(RawSlot - front);
                        if (!target.equals(uuid)) {
                            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                            PlayerConnection connection = ePlayer.playerConnection;
                            if (isSpec) {
                                if (member.task != -1) {
                                    Bukkit.getScheduler().cancelTask(member.task);
                                    member.task = -1;
                                }
                                Member.getMember(member.target).Spectators.remove(connection);
                            } else {
                                member.Spectators.forEach(a -> Reduce.quitSpec(null, a, true));
                                member.Spectators.clear();
                                Reduce.removeFromGUI(player, member.Map, uuid);
                                player.closeInventory();
                                member.oldMap = member.Map;
                                member.oldCoord = member.Coord;
                                player.setGameMode(GameMode.ADVENTURE);
                                player.setAllowFlight(true);
                                member.clearBlocks();
                                Inventory inv = player.getInventory();
                                inv.clear();
                                inv.setItem(4, ItemStackList.Spec[member.user.lang]);
                                inv.setItem(8, ItemStackList.LeaveSpec[member.user.lang]);
                            }
                            Member targetMember = Member.getMember(target);
                            targetMember.Spectators.add(connection);
                            Player targetPlayer = Bukkit.getPlayer(target);
                            member.Coord = targetMember.Coord;
                            member.Map = map;
                            member.target = target;
                            PlayerUtils.teleport(connection, map * 500 + 0.5, 100, targetMember.Coord + 0.5, 0, 0);
                            player.closeInventory();
                            targetPlayer.hidePlayer(player);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                            EntityPlayer entityPlayer = targetMember.EntityPlayers;
                            ((CraftPlayer) targetPlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ePlayer));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
                                connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                                connection.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) 64));
                                connection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
                                connection.sendPacket(new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, 1));
                                connection.sendPacket(new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, 0));
                                connection.sendPacket(new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, Collections.singletonList(" "), 3));
                                connection.sendPacket(new PacketPlayOutSpawnEntityLiving(targetMember.Holo));
                            }, 1);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                                }
                            }, 4000);

                            if (member.setSkin) {
                                player.sendMessage(StringList.InputStringCancel[member.user.lang]);
                                member.setSkin = false;
                            }
                            if (member.setName) {
                                player.sendMessage(StringList.InputStringCancel2[member.user.lang]);
                                member.setName = false;
                            }
                            if (member.ReachSettings2) {
                                player.sendMessage(StringList.InputStringCancel3[member.user.lang]);
                                member.ReachSettings2 = false;
                            }
                            if (member.horSettings2) {
                                player.sendMessage(StringList.InputStringCancel4[member.user.lang]);
                                member.horSettings2 = false;
                            }
                            if (member.ReachSettings) {
                                player.sendMessage(StringList.InputStringCancel3[member.user.lang]);
                                member.ReachSettings = false;
                            }
                            if (member.horSettings) {
                                player.sendMessage(StringList.InputStringCancel4[member.user.lang]);
                                member.horSettings = false;
                            }
                            if (member.verSettings) {
                                player.sendMessage(StringList.InputStringCancel5[member.user.lang]);
                                member.verSettings = false;
                            }
                        }
                    }
                } else BlockEditor.select(holder, e, RawSlot, player);
            }
        }
    }
}
