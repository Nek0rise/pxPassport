package uwu.nekorise.pxPassport;

import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import uwu.nekorise.pxPassport.command.PassportCommand;
import uwu.nekorise.pxPassport.command.PassportTabCompleter;
import uwu.nekorise.pxPassport.command.PxPassportCommand;
import uwu.nekorise.pxPassport.command.PxPassportTabCompleter;
import uwu.nekorise.pxPassport.config.ConfigManager;
import uwu.nekorise.pxPassport.database.DatabaseService;
import uwu.nekorise.pxPassport.event.OnGUIClick;
import uwu.nekorise.pxPassport.event.OnJoin;
import uwu.nekorise.pxPassport.service.PassportRequestService;
import uwu.nekorise.pxPassport.util.LogFilter;

public final class PxPassport extends JavaPlugin {
    @Getter private static PxPassport instance;
    @Getter private DatabaseService databaseService;
    @Getter private PassportRequestService passportService;

    @Override
    public void onEnable() {
        instance = this;

        ConfigManager.loadConfig();
        LogFilter.registerFilter();
        databaseService = new DatabaseService();
        databaseService.init();
        passportService = new PassportRequestService(databaseService.getStorage());

        registerCommands();
        registerTabCompleters();
        registerListeners();
        registerBStats();
    }

    @Override
    public void onDisable() {
        databaseService.shutdown();
    }

    private void registerCommands() {
        getCommand("pxpassport").setExecutor(new PxPassportCommand());
        getCommand("passport").setExecutor(new PassportCommand());
    }
    private void registerTabCompleters() {
        getCommand("pxpassport").setTabCompleter(new PxPassportTabCompleter());
        getCommand("passport").setTabCompleter(new PassportTabCompleter());
    }
    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new OnGUIClick(), instance);
        pm.registerEvents(new OnJoin(), instance);
    }
    private void registerBStats() {
        Metrics metrics = new Metrics(instance, 29240);
    }
}
//⠀⠀⠀⠀⠀⠀⠀⠀         ⠀⡔⠠⢤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//        ⡴⠒⠒⠒⠒⠒⠶⠦⠄⢹⣄⠀⠀⠑⠄⣀⡠⠤⠴⠒⠒⠒⠀⠀
//        ⢇⠀⠀⠀⠀⠀⠀⠐⠋⠀⠒⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠀
//        ⠈⢆⠀⠀⠀⠀⡤⠤⣄⠀⠀⠀⠀⡤⠤⢄⠀⠀⠀⠀⠀⣠⠃⠀
//        ⠀⡀⠑⢄⡀⡜⠀⡜⠉⡆⠀⠀⠀⡎⠙⡄⠳⡀⢀⣀⣜⠁⠀⠀
//        ⠀⠹⣍⠑⠀⡇⠀⢣⣰⠁⠀⠀⠀⠱⣠⠃⠀⡇⠁⣠⠞⠀⠀⠀
//        ⠀⠀⠀⡇⠔⣦⠀⠀⠀⠈⣉⣀⡀⠀⠀⠰⠶⠖⠘⢧⠀⠀⠀⠀
//        ⠀⠀⠰⠤⠐⠤⣀⡀⠀⠈⠑⣄⡁⠀⡀⣀⠴⠒⠀⠒⠃⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⠘⢯⡉⠁⠀⠀⠀⠀⠉⢆⠀⠀⠀⠀⠀⠀⠀⠀
//        ⠀⠀⠀⠀⠀⠀⢀⣞⡄⠀⠀⠀⠀⠀⠀⠈⡆⠀⠀⠀⠀⠀⠀⠀
