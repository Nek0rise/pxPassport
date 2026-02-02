package uwu.nekorise.pxPassport.util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uwu.nekorise.pxPassport.entity.ItemType;
import uwu.nekorise.pxPassport.entity.PassportItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIConfigParser {
    public static Map<String, PassportItem> getPassportItems(FileConfiguration config) {
        Map<String, PassportItem> passportItems = new HashMap<>();
        ConfigurationSection cfg = config.getConfigurationSection("passport-gui.items");

        if (cfg == null) {
            return null;
        }

        for (String itemKey : cfg.getKeys(false)) {
            ConfigurationSection itemSection = cfg.getConfigurationSection(itemKey);
            if (itemSection == null) {
                continue;
            }

            ItemType itemType = ItemType.valueOf(itemSection.getString("type", "ITEM"));
            int itemIndex = itemSection.getInt("index");
            Material material = Material.valueOf(itemSection.getString("material"));
            String itemModel = itemSection.getString("item-model");
            String itemName = itemSection.getString("display-name");
            List<String> lore = itemSection.getStringList("lore");

            Map<String, Object> itemData = readSection(itemSection);
            PassportItem item = new PassportItem(itemType, itemIndex, itemData, material, itemModel, itemName, lore);
            passportItems.put(itemKey, item);
        }

        return passportItems;
    }

    private static Map<String, Object> readSection(ConfigurationSection section) {
        Map<String, Object> result = new HashMap<>();

        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            if (value instanceof ConfigurationSection nested) {
                result.put(key, readSection(nested));
            } else {
                result.put(key, value);
            }
        }

        return result;
    }
}