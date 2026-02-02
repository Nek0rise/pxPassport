package uwu.nekorise.pxPassport.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.util.Log;

public class LangConfigStorage {

    @Getter private static String passportUsage = "?";
    @Getter private static String viewPlayerIsFar = "?";
    @Getter private static String viewRequest = "?";
    @Getter private static String viewRequestSent = "?";
    @Getter private static String viewRequestDeny = "?";

    @Getter private static String giveUsage = "?";
    @Getter private static String giveSuccess = "?";

    @Getter private static String deleteUsage = "?";
    @Getter private static String deleteSuccess = "?";

    @Getter private static String noRequests = "?";
    @Getter private static String accept = "?";
    @Getter private static String deny = "?";

    @Getter private static String pxpassportUsage = "?";
    @Getter private static String pxpassportSuccess = "?";

    @Getter private static String playerOnly = "?";
    @Getter private static String playerOffline = "?";
    @Getter private static String playerWithoutPassport = "?";
    @Getter private static String noPermission = "?";

    public static void loadData() {
        try {
            FileConfiguration cfg = ConfigManager.getConfig("lang/" + MainConfigStorage.getLanguage() + ".yml");

            passportUsage = cfg.getString("passport-command.usage");

            viewPlayerIsFar = cfg.getString("passport-command.view.player-is-far");
            viewRequest = cfg.getString("passport-command.view.view-request");
            viewRequestSent = cfg.getString("passport-command.view.request-sent");
            viewRequestDeny = cfg.getString("passport-command.view.request-deny");

            giveUsage = cfg.getString("passport-command.give.usage");
            giveSuccess = cfg.getString("passport-command.give.success");

            deleteUsage = cfg.getString("passport-command.delete.usage");
            deleteSuccess = cfg.getString("passport-command.delete.success");

            noRequests = cfg.getString("passport-command.accept-deny.no-requests");
            accept = cfg.getString("passport-command.accept-deny.accept");
            deny = cfg.getString("passport-command.accept-deny.deny");

            pxpassportUsage = cfg.getString("pxpassport-command.usage");
            pxpassportSuccess = cfg.getString("pxpassport-command.success");

            playerOnly = cfg.getString("player-only");
            playerOffline = cfg.getString("player-offline");
            playerWithoutPassport = cfg.getString("player-without-passport");
            noPermission = cfg.getString("no-permission");
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PxPassport.getInstance(), "Failed to load language config");
        }
    }
}
