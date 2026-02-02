package uwu.nekorise.pxPassport.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.nekorise.pxPassport.config.MainConfigStorage;
import uwu.nekorise.pxPassport.util.ArgumentParser;

import java.util.ArrayList;
import java.util.List;

public class PassportTabCompleter implements TabCompleter {
    private static List<String> subcommands;

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        subcommands = new ArrayList<>();
        if (sender.hasPermission("pxpassport.view.other") || sender.hasPermission("pxpassport.view.own")) {
            subcommands.add("view");
        }
        if (sender.hasPermission("pxpassport.give")) {
            subcommands.add("give");
        }
        if (sender.hasPermission("pxpassport.delete")) {
            subcommands.add("delete");
        }

        if (args.length == 1) {
            return subcommands;
        }

        if (!args[0].equalsIgnoreCase("give") || !sender.hasPermission("pxpassport.give")) {
            return null;
        }

        if (args.length == 2) {
            return null;
        }

        int logicalIndex = ArgumentParser.logicalIndexForTab(args, 2);
        boolean inQuotes = ArgumentParser.isInsideQuotes(args, 2);
        if (!inQuotes) {
            logicalIndex--;
        }

        if (logicalIndex < 0) {
            logicalIndex = 0;
        }

        String key = resolveKey(logicalIndex);
        if (key == null) return List.of();

        String hint = MainConfigStorage.getGiveSuggestions().get(key);
        if (hint == null || hint.isBlank()) return null;

        return List.of(inQuotes ? hint + "\"" : hint);
    }

    private String resolveKey(int index) {
        return switch (index) {
            case 0 -> "birthday";
            case 1 -> "city";
            case 2 -> "sex";
            default -> {
                int v = index - 2;
                yield v >= 1 && v <= 10 ? "value" + v : null;
            }
        };
    }
}
