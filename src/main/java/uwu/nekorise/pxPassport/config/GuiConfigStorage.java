package uwu.nekorise.pxPassport.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.entity.PassportItem;
import uwu.nekorise.pxPassport.util.GUIConfigParser;
import uwu.nekorise.pxPassport.util.Log;

import java.util.Map;

public class GuiConfigStorage {
    @Getter private static String passportName;
    @Getter private static int passportSize;
    @Getter private static Map<String, PassportItem> passportItems;

    public static void loadData() {
        try {
            FileConfiguration cfg = ConfigManager.getConfig("gui/" + MainConfigStorage.getPassportGui() + ".yml");

            passportName = cfg.getString("passport-gui.name");
            passportSize = cfg.getInt("passport-gui.size");
            passportItems = GUIConfigParser.getPassportItems(cfg);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.error(PxPassport.getInstance(), "Failed to load gui config");
        }
    }
}