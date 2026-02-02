package uwu.nekorise.pxPassport.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.util.Log;

import java.util.HashMap;
import java.util.Map;

public class NegativeCharsConfig {
    @Getter private static Map<String, String> negativeChars;

    public static void loadData() {
        try {
            FileConfiguration cfg = ConfigManager.getConfig("negative-chars.yml");

            negativeChars = new HashMap<>();
            for (String key : cfg.getKeys(false)) {
                String value = cfg.getString(key, "");
                negativeChars.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PxPassport.getInstance(), "Failed to load negative-chars.yml");
        }
    }
}
