package top.speedcubing.reduce.BukkitEventListener;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import top.speedcubing.reduce.Member;

public class PlayerToggleSneak implements Listener {

    @EventHandler
    public void PlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        Member member = Member.getMember(e.getPlayer());
        if (e.isSneaking() && member.target != null && member.isLockedOnSomeone) {
            member.isLockedOnSomeone = false;
            EntityPlayer npc = member.target.user.toNMS();
            ((CraftPlayer) member.user.player).getHandle().playerConnection.sendPacket(new PacketPlayOutCamera(((CraftPlayer) member.user.player).getHandle()));
            ((CraftPlayer) member.user.player).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, 2));
            member.user.bSetAllowFlight(true);
            member.user.bSetFlying(true);
            member.user.bTeleport(new Location(member.user.bGetWorld(), npc.locX, npc.locY, npc.locZ, npc.yaw, npc.pitch));
        }
    }
}
