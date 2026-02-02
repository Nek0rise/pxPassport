package uwu.nekorise.pxPassport.entity;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
public class PassportGUI implements InventoryHolder{
    @Setter
    private Inventory inventory;
    private final Component name;
    private final String targetPlayer;

    public PassportGUI(Component name, int size, String targetPlayer) {
        this.name = name;
        this.inventory = Bukkit.createInventory(this, size, this.name);
        this.targetPlayer = targetPlayer;
    }
}
