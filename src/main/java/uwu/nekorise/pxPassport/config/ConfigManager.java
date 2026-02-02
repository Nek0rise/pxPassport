package uwu.nekorise.pxPassport.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uwu.nekorise.pxPassport.PxPassport;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private final static String path = PxPassport.getInstance().getDataFolder().getAbsolutePath() + "/";
    private final static List<String> langConfigFiles = List.of("en.yml", "ru.yml");
    private final static List<String> guiConfigFiles = List.of("example-rp.yml", "example-simple.yml");

    public static FileConfiguration getConfig(String cfgName) throws IOException, InvalidConfigurationException {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.options().parseComments(true);
        fileConfiguration.load(path + cfgName);

        return fileConfiguration;
    }

    private static void createConfigs() {
        File defaultConfig = new File(path, "config.yml");
        if (!defaultConfig.exists()) {
            PxPassport.getInstance().saveResource("config.yml", false);
        }

        for (String langConfigName : langConfigFiles) {
            File langConfig = new File(path, "lang/" + langConfigName);
            if (!langConfig.exists()) {
                PxPassport.getInstance().saveResource("lang/" + langConfigName, false);
            }
        }
        for (String guiConfigName : guiConfigFiles) {
            File guiFolder = new File(path, "gui/" + guiConfigName);
            if (!guiFolder.exists()) {
                PxPassport.getInstance().saveResource("gui/" + guiConfigName, false);
            }
        }

        File charsConfig = new File(path, "negative-chars.yml");
        if (!charsConfig.exists()) {
            PxPassport.getInstance().saveResource("negative-chars.yml", false);
        }
    }

    public static void loadConfig() {
        createConfigs();
        MainConfigStorage.loadData();
        LangConfigStorage.loadData();
        NegativeCharsConfig.loadData();
        GuiConfigStorage.loadData();
    }
}
