package uwu.nekorise.pxPassport.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.config.LangConfigStorage;
import uwu.nekorise.pxPassport.config.MainConfigStorage;
import uwu.nekorise.pxPassport.database.DatabaseStorage;
import uwu.nekorise.pxPassport.service.PassportRequestService;
import uwu.nekorise.pxPassport.util.ArgumentParser;
import uwu.nekorise.pxPassport.util.MMessage;
import uwu.nekorise.pxPassport.util.PlaceholderFormat;
import uwu.nekorise.pxPassport.util.Skin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PassportCommand implements CommandExecutor {
    private PassportRequestService viewService;
    private DatabaseStorage storage;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = sender instanceof Player p ? p : null;
        storage = PxPassport.getInstance().getDatabaseService().getStorage();
        viewService = PxPassport.getInstance().getPassportService();

        if (args.length == 0) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPassportUsage()));
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "view" -> view(sender, player, args);
            case "accept" -> accept(sender, player);
            case "deny" -> deny(sender, player);
            case "give" -> give(sender, args);
            case "delete" -> delete(sender, args);
            default -> {
                sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPassportUsage()));
                yield true;
            }
        };
    }

    private boolean view(CommandSender sender, Player player, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerOnly()));
            return false;
        }

        if (player == null) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerOnly()));
            return false;
        }

        if (args.length < 2) {

            if (!player.hasPermission("pxpassport.view.own")) {
                player.sendMessage(MMessage.applyColor(LangConfigStorage.getNoPermission()));
                return false;
            }

            if (!storage.exists(player.getName())) {
                player.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerWithoutPassport()));
                return false;
            }

            viewService.openPassport(player.getName(), player);
            return true;
        }

        if (!player.hasPermission("pxpassport.view.other")) {
            player.sendMessage(MMessage.applyColor(LangConfigStorage.getNoPermission()));
            return false;
        }

        viewService.requestView(player, args[1]);
        return true;
    }


    private boolean accept(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerOnly()));
            return false;
        }

        viewService.accept(player);
        return true;
    }

    private boolean deny(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerOnly()));
            return false;
        }

        viewService.deny(player);
        return true;
    }

    private boolean give(CommandSender sender, String[] args) {
        if (!sender.hasPermission("pxpassport.give")) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getNoPermission()));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getGiveUsage()));
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerOffline()));
            return false;
        }

        List<String> parsed = ArgumentParser.parse(args, 2);
        String nickname = target.getName();

        String skin = Skin.getSkinBase64(target);

        storage.updateAvatar(nickname, skin != null ? skin : MainConfigStorage.DEFAULT_SKIN);
        storage.updateAuthor(nickname, sender.getName());
        storage.updateBirthday(nickname, get(parsed, 0));
        storage.updateCity(nickname, get(parsed, 1));
        storage.updateSex(nickname, get(parsed, 2));

        Map<Integer, String> values = new LinkedHashMap<>();
        for (int i = 3; i < 13; i++) {
            values.put(i - 2, get(parsed, i));
        }
        storage.updateValues(nickname, values);

        String message = LangConfigStorage.getGiveSuccess();
        sender.sendMessage(MMessage.applyColor(PlaceholderFormat.format(message, nickname, target, storage)));

        return true;
    }

    private boolean delete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("pxpassport.delete")) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getNoPermission()));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getDeleteUsage()));
            return false;
        }

        String nickname = args[1];
        if (!storage.exists(nickname)) {
            sender.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerWithoutPassport()));
            return false;
        }
        storage.deletePassport(nickname);

        String message = LangConfigStorage.getDeleteSuccess();
        sender.sendMessage(MMessage.applyColor(PlaceholderFormat.format(message, nickname, null, null)));

        return true;
    }

    private String get(List<String> list, int index) {
        if (index >= list.size()) return null;
        String v = list.get(index);
        return v == null || v.isBlank() ? null : v;
    }
}
