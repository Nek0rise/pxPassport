package uwu.nekorise.pxPassport.entity;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

@Getter
public class PassportItem extends ItemStack {

    private final int index;
    private final Map<String, Object> sections;
    private final ItemType itemType;

    public PassportItem(ItemType itemType, int index, Map<String, Object> sections, Material material, String itemModel, String name, List<String> lore) {
        super(material);
        this.index = index;
        this.sections = sections;
        this.itemType = itemType;

        ItemMeta meta = super.getItemMeta();
        if (itemModel != null) {
            String[] parts = itemModel.split(":", 2);
            meta.setItemModel(new NamespacedKey(parts[0], parts[1]));
        }
        meta.setDisplayName(name);
        meta.setLore(lore);
        super.setItemMeta(meta);
    }

    @Override
    public PassportItem clone() {
        PassportItem copy = new PassportItem(this.itemType, this.index, this.sections, this.getType(), null, null, null);

        copy.setItemMeta(this.getItemMeta().clone());
        return copy;
    }


    public ItemStack toItemStack() {
        return super.clone();
    }
}
