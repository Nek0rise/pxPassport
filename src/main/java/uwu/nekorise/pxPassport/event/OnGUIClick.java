package uwu.nekorise.pxPassport.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import uwu.nekorise.pxPassport.entity.PassportGUI;

public class OnGUIClick implements Listener {
    @EventHandler
    public void onGUIClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof PassportGUI)) return;
        e.setCancelled(true);
    }
}
