package speedcubing.BukkitEventListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        e.setDamage(0);
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            e.setCancelled(true);
    }
}
