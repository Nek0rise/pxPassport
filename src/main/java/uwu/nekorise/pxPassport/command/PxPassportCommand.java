package uwu.nekorise.pxPassport.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import uwu.nekorise.pxPassport.config.ConfigManager;
import uwu.nekorise.pxPassport.config.LangConfigStorage;
import uwu.nekorise.pxPassport.util.MMessage;

public class PxPassportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("pxpassport.reload")) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getNoPermission()));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPxpassportUsage()));
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            return reloadConfig(sender);
        }
        sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPxpassportUsage()));
        return false;
    }

    private boolean reloadConfig(CommandSender sender) {
        try {
            ConfigManager.loadConfig();
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPxpassportSuccess()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
