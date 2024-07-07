package top.speedcubing.reduce.bukkitlistener;

import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
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
            member.user.sendPacket(new PacketPlayOutCamera(member.user.toNMS()), new PacketPlayOutGameStateChange(3, 2));
            member.user.bSetAllowFlight(true);
            member.user.bSetFlying(true);
            member.user.bTeleport(member.target.user.player);
        }
    }
}
