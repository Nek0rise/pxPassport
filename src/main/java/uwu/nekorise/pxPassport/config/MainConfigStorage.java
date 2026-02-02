package uwu.nekorise.pxPassport.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.database.DatabaseType;
import uwu.nekorise.pxPassport.util.Log;
import uwu.nekorise.pxPassport.util.ParserType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainConfigStorage {

    @Getter private static String language = "en";

    @Getter private static DatabaseType DBMS = DatabaseType.H2;
    @Getter private static String dbHost;
    @Getter private static int dbPort;
    @Getter private static String dbUser;
    @Getter private static String dbPass;

    @Getter private static boolean isMiniMessageFormatting = true;
    @Getter private static boolean isAlignmentEnabled = true;

    @Getter private static String passportGui;

    @Getter private static Map<String, String> giveSuggestions = Collections.emptyMap();

    @Getter private static int viewRadius = 10;

    public static final String DEFAULT_SKIN = "asd=";


    public static void loadData() {
        try {
            FileConfiguration cfg = ConfigManager.getConfig("config.yml");

            language = cfg.getString("language");

            DBMS = DatabaseType.valueOf(cfg.getString("database-type"));
            dbHost = cfg.getString("mysql.host");
            dbPort = cfg.getInt("mysql.port");
            dbUser = cfg.getString("mysql.user");
            dbPass = cfg.getString("mysql.password");

            isMiniMessageFormatting = ParserType.valueOf(cfg.getString("text-formatting.parser-type")).equals(ParserType.MINIMESSAGE);

            isAlignmentEnabled = cfg.getBoolean("text-formatting.enable-alignment");

            passportGui = cfg.getString("passport-gui");

            viewRadius = cfg.getInt("view-radius");

            loadGiveSuggestions(cfg);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.error(PxPassport.getInstance(), "Failed to load config.yml");
        }
    }

    private static void loadGiveSuggestions(FileConfiguration cfg) {
        ConfigurationSection section = cfg.getConfigurationSection("give-suggestions");

        if (section == null) {
            giveSuggestions = Collections.emptyMap();
            return;
        }

        Map<String, String> map = new HashMap<>();

        for (String key : section.getKeys(false)) {
            String value = section.getString(key);
            if (value != null && !value.isBlank()) {
                map.put(key, value);
            }
        }

        giveSuggestions = Map.copyOf(map);
    }
}
