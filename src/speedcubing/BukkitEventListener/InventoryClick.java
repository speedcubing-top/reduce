package speedcubing.BukkitEventListener;


import com.mojang.authlib.properties.Property;
import cubing.spigot.lib.bukkit.*;
import cubing.spigot.lib.bukkit.InventoryUtils;
import cubing.spigot.lib.utils.Console;
import cubingserver.StringList.GlobalString;
import cubingserver.libs.User;
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
import speedcubing.Core.BlockEditor;
import speedcubing.Core.Reduce;
import speedcubing.List.ItemStackList;
import speedcubing.List.StringList;
import speedcubing.Main;

import java.util.*;

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
                            UUID uuid = player.getUniqueId();
                            int lang = User.getLang(uuid);
                            Inventory inv = Bukkit.createInventory(ItemStackList.HitSettingsHolder, 27, StringList.npchitsettings[lang]);
                            String[] reach = StringList.ReachLore[lang];
                            String[] hor = StringList.HorLore[lang];
                            String[] ver = StringList.VerLore[lang];
                            Integer[] cache = Reduce.integerCache.get(uuid);
                            inv.setItem(11, new ItemBuilder(Material.STICK).name(reach[0]).lore(reach[1], reach[2].replace("%kb%", String.format("%.3f", cache[5] / 1000D)), reach[3], reach[4]).build());
                            inv.setItem(13, new ItemBuilder(Material.STICK).name(hor[0]).lore(hor[1], hor[2].replace("%kb%", String.format("%.3f", cache[6] / 1000D)), hor[3], hor[4]).build());
                            inv.setItem(15, new ItemBuilder(Material.STICK).name(ver[0]).lore(ver[1], ver[2].replace("%kb%", String.format("%.3f", cache[7] / 1000D)), ver[3], ver[4]).build());
                            player.openInventory(inv);
                        }
                        break;
                        case 11: {
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = User.getLang(uuid);
                            Inventory inv = player.getInventory();
                            player.openInventory(ItemStackList.InventoryKitEditor[lang]);
                            InventoryUtils.fillItem(inv, 9, 36, Reduce.Invalid[lang]);
                            Reduce.kitedit.add(uuid);
                        }
                        break;
                        case 12:
                            BlockEditor.createMainPage(player);
                            break;
                        case 13: {
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = User.getLang(uuid);
                            int x = Reduce.integerCache.get(uuid)[3];
                            Inventory inv = Bukkit.createInventory(ItemStackList.StickSkinHolder, 27, StringList.stickedit[lang]);
                            for (int i = 0; i < 6; i++) {
                                inv.setItem(i + 10, ItemStackList.stickeditor[0][i][lang]);
                            }
                            org.bukkit.inventory.ItemStack selected = ItemStackList.stickeditor[1][x][lang];
                            inv.setItem(x + 10, selected);
                            player.openInventory(inv);
                        }
                        break;
                        case 14: {
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = User.getLang(uuid);
                            int map = Reduce.PlayerMap.get(uuid);
                            Inventory inv = Bukkit.createInventory(ItemStackList.MapHolder, 18, StringList.mapselect[lang]);
                            inv.setItem(0, ItemStackList.Maps[0][0][lang]);
                            inv.setItem(1, ItemStackList.Maps[0][1][lang]);
                            inv.setItem(2, ItemStackList.Maps[0][2][lang]);
                            inv.setItem(3, ItemStackList.Maps[0][3][lang]);
                            inv.setItem(map, ItemStackList.Maps[1][map][lang]);
                            player.openInventory(inv);
                        }
                        break;
                        case 15: {
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = User.getLang(player.getUniqueId());
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
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            int lang = User.getLang(uuid);
                            Boolean[] bs = Reduce.booleanCache.get(uuid);
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
                                player.sendMessage(GlobalString.SavedSettings[User.getLang(uuid)]);
                                Integer[] reduce = Reduce.integerCache.get(uuid);
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
                                Reduce.kitedit.remove(uuid);
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
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = Reduce.booleanCache.get(uuid)[0];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][User.getLang(uuid)]);
                            Reduce.booleanCache.get(uuid)[0] = !b;
                        }
                        break;
                        case 22: {
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = Reduce.booleanCache.get(uuid)[1];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][User.getLang(uuid)]);
                            Reduce.booleanCache.get(uuid)[1] = !b;
                        }
                        break;
                        case 23: {
                            UUID uuid = player.getUniqueId();
                            player.playSound(player.getLocation(), Sound.CLICK, 1F, 1F);
                            boolean b = Reduce.booleanCache.get(uuid)[2];
                            e.getClickedInventory().setItem(RawSlot, ItemStackList.preferButton[b ? 1 : 0][User.getLang(uuid)]);
                            Reduce.booleanCache.get(uuid)[2] = !b;
                        }
                        break;
                    }
                } else if (holder == ItemStackList.StickSkinHolder) {
                    e.setCancelled(true);
                    if (RawSlot >= 10 && RawSlot <= 15) {
                        UUID uuid = player.getUniqueId();
                        int lang = User.getLang(uuid);
                        int x = RawSlot - 10;
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 1F);
                        Reduce.integerCache.get(uuid)[3] = x;
                        player.closeInventory();
                        player.sendMessage(GlobalString.SavedSettings[lang]);
                        player.getInventory().setItem(Reduce.integerCache.get(uuid)[0], ItemStackList.Sticks[x][lang]);
                    }
                } else if (holder == ItemStackList.MapHolder) {
                    e.setCancelled(true);
                    UUID uuid = player.getUniqueId();
                    if (RawSlot < Reduce.maptypes && RawSlot != Reduce.PlayerMap.get(uuid)) { //swap map
                        if (Reduce.used[RawSlot] == Reduce.rooms) {
                            Console.sendColoredConsole("§8[§bReduce§8] §a " + player.getName() + "§b swapped to map §a" + RawSlot);
                            player.sendMessage(GlobalString.NoEnoughServers[User.getLang(uuid)]);
                            return;
                        }
                        int newroom = -1;
                        for (int h = 0; h < Reduce.rooms; h++) {
                            if (Reduce.Rooms[RawSlot][h]) {
                                newroom = h;
                                break;
                            }
                        }
                        int map = Reduce.PlayerMap.get(uuid);
                        int coord = Reduce.PlayerCoord.get(uuid);
                        int oldroom = Reduce.coordToRoom(coord);
                        World world = player.getWorld();
                        world.getChunkAt(new Location(world, 500 * map + 0.5, 100, coord + 0.5)).load();
                        if (Reduce.DamageTask.containsKey(uuid)) {
                            Bukkit.getScheduler().cancelTask(Reduce.DamageTask.get(uuid));
                            Reduce.DamageTask.remove(uuid);
                        }
                        Reduce.setSkin.remove(uuid);
                        Reduce.setName.remove(uuid);
                        Reduce.ReachSettings.remove(uuid);
                        Reduce.horSettings.remove(uuid);
                        Reduce.verSettings.remove(uuid);
                        Reduce.SideBar.get(uuid).cancel();
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, Reduce.EntityPlayers.get(uuid)));
                        for (BlockPosition b : Reduce.blocks.get(uuid)) {
                            world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
                        }

                        Reduce.removeFromGUI(player.getName(), uuid, map, null);
                        player.closeInventory();
                        Reduce.GameStart(RawSlot, player, uuid, newroom, Reduce.Spectators.get(uuid));
                        Reduce.used[map] -= 1;
                        Reduce.Rooms[map][oldroom] = true;
                    }
                } else if (holder == ItemStackList.SkinHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 10: {
                            UUID uuid = player.getUniqueId();
                            player.sendMessage(StringList.InputString[User.getLang(uuid)]);
                            player.closeInventory();
                            Reduce.setSkin.add(uuid);
                        }
                        break;
                        case 11: {
                            UUID uuid = player.getUniqueId();
                            EntityPlayer entityPlayer = Reduce.EntityPlayers.get(uuid);
                            Property property = ((CraftPlayer) player).getProfile().getProperties().get("textures").iterator().next();
                            Reduce.setSkin(player, entityPlayer, uuid, new String[]{property.getValue(), property.getSignature()});
                            player.closeInventory();
                        }
                        break;
                        case 13: {
                            UUID uuid = player.getUniqueId();
                            player.sendMessage(StringList.InputString2[User.getLang(uuid)]);
                            player.closeInventory();
                            Reduce.setName.add(uuid);
                        }
                        break;
                        case 14: {
                            UUID uuid = player.getUniqueId();
                            EntityArmorStand stand = Reduce.Holo.get(uuid);
                            stand.setCustomName("§6" + cubingserver.config.SERVERIP);
                            Reduce.sendPackets(((CraftPlayer) player).getHandle().playerConnection, Reduce.Spectators.get(uuid), new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
                            player.closeInventory();
                        }
                        break;
                    }
                } else if (holder == ItemStackList.HitSettingsHolder) {
                    e.setCancelled(true);
                    switch (RawSlot) {
                        case 11: {
                            UUID uuid = player.getUniqueId();
                            player.closeInventory();
                            player.sendMessage(StringList.veloinput2[User.getLang(uuid)]);
                            Reduce.ReachSettings.add(uuid);
                        }
                        break;
                        case 13: {
                            UUID uuid = player.getUniqueId();
                            player.closeInventory();
                            player.sendMessage(StringList.veloinput[User.getLang(uuid)]);
                            Reduce.horSettings.add(uuid);
                        }
                        break;
                        case 15: {
                            UUID uuid = player.getUniqueId();
                            player.closeInventory();
                            player.sendMessage(StringList.veloinput[User.getLang(uuid)]);
                            Reduce.verSettings.add(uuid);
                        }
                        break;
                    }
                } else if (holder == ItemStackList.SpecHolder) {
                    e.setCancelled(true);
                    if (RawSlot < Reduce.totalName.size()) {
                        Console.sendColoredConsole("§8[§bReduce§8] §a " + player.getName() + "§b clicked §a" + RawSlot);
                        //join spectate
                        UUID uuid = player.getUniqueId();
                        boolean isSpec = player.getGameMode() == GameMode.ADVENTURE;
                        if (isSpec && Reduce.totalName.get(RawSlot).equals(Bukkit.getPlayer(Reduce.target.get(uuid)).getName()))
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
                        if (target.equals(uuid))
                            return;
                        if (Reduce.booleanCache.get(uuid)[3] && !isSpec) {
                            for (PlayerConnection c : Reduce.Spectators.get(uuid)) {
                                Reduce.quitSpec(null, c, true);
                            }
                            Reduce.removeFromGUI(player.getName(), uuid, Reduce.PlayerMap.get(uuid), uuid);
                            player.closeInventory();
                        }

                        int lang = User.getLang(uuid);
                        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                        PlayerConnection connection = ePlayer.playerConnection;
                        if (isSpec) {
                            if (Reduce.DamageTask.containsKey(uuid)) {
                                Bukkit.getScheduler().cancelTask(Reduce.DamageTask.get(uuid));
                                Reduce.DamageTask.remove(uuid);
                            }
                            Reduce.Spectators.get(Reduce.target.get(uuid)).remove(connection);
                        } else {
                            Reduce.Spectators.get(target).add(connection);
                            player.setGameMode(GameMode.ADVENTURE);
                            player.setAllowFlight(true);
                            World world = player.getWorld();
                            for (BlockPosition b : Reduce.blocks.get(uuid)) {
                                world.getBlockAt(b.getX(), b.getY(), b.getZ()).setType(Material.AIR);
                            }
                            Reduce.blocks.get(uuid).clear();
                            Inventory inv = player.getInventory();
                            inv.clear();
                            inv.setItem(4, ItemStackList.Spec[lang]);
                            inv.setItem(8, ItemStackList.LeaveSpec[lang]);
                        }
                        int targetcoord = Reduce.PlayerCoord.get(target);
                        Player targetPlayer = Bukkit.getPlayer(target);
                        Reduce.PlayerCoord.put(uuid, targetcoord);
                        Reduce.PlayerMap.put(uuid, map);
                        Reduce.target.put(uuid, target);
                        PlayerUtils.teleport(connection, map * 500 + 0.5, 100, targetcoord + 0.5, 0, 0);
                        player.closeInventory();
                        targetPlayer.hidePlayer(player);
                        EntityPlayer entityPlayer = Reduce.EntityPlayers.get(target);
                        ((CraftPlayer) targetPlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ePlayer));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
                            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
                            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                            connection.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) 64));
                            connection.sendPacket(new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, CraftItemStack.asNMSCopy(ItemStackList.Sticks[0][0])));
                            connection.sendPacket(new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, 1));
                            connection.sendPacket(new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, 0));
                            connection.sendPacket(new PacketPlayOutScoreboardTeam(Reduce.NoNameTag, Collections.singletonList(" "), 3));
                            connection.sendPacket(new PacketPlayOutSpawnEntityLiving(Reduce.Holo.get(target)));
                        }, 1);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                            }
                        }, 4000);

                        if (Reduce.setSkin.contains(uuid)) {
                            player.sendMessage(StringList.InputStringCancel[lang]);
                            Reduce.setSkin.remove(uuid);
                        }
                        if (Reduce.setName.contains(uuid)) {
                            player.sendMessage(StringList.InputStringCancel2[lang]);
                            Reduce.setName.remove(uuid);
                        }
                        if (Reduce.ReachSettings.contains(uuid)) {
                            player.sendMessage(StringList.InputStringCancel3[lang]);
                            Reduce.ReachSettings.remove(uuid);
                        }
                        if (Reduce.horSettings.contains(uuid)) {
                            player.sendMessage(StringList.InputStringCancel4[lang]);
                            Reduce.horSettings.remove(uuid);
                        }
                        if (Reduce.verSettings.contains(uuid)) {
                            player.sendMessage(StringList.InputStringCancel5[lang]);
                            Reduce.verSettings.remove(uuid);
                        }
                    }
                } else BlockEditor.select(holder, e, RawSlot, player);
            }
        }
    }
}
