package uwu.nekorise.pxPassport.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import uwu.nekorise.pxPassport.config.MainConfigStorage;
import uwu.nekorise.pxPassport.database.DatabaseStorage;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlaceholderFormat {
    private static final Pattern VALUE_PATTERN = Pattern.compile("%value(\\d{1,2})%");

    public static String format(String text, String nickname, Player player, DatabaseStorage storage) {
        if (text == null || nickname == null) {
            return text;
        }

        if (storage == null) {
            return formatRaw(text, nickname);
        }

        LocalDate date = storage.getDate(nickname);

        text = text.replace("%id%", safe(storage.getId(nickname).toString()));
        text = text.replace("%player%", nickname);
        text = text.replace("%author%", safe(storage.getAuthor(nickname)));
        text = text.replace("%date%", date != null ? date.toString() : "");
        text = text.replace("%birthday%", safe(storage.getBirthday(nickname)));
        text = text.replace("%city%", safe(storage.getCity(nickname)));
        text = text.replace("%sex%", safe(storage.getSex(nickname)));

        text = replaceValuePlaceholders(text, nickname, storage);
        text = player != null ? PlaceholderAPI.setPlaceholders(player, text) : text;

        return MainConfigStorage.isAlignmentEnabled() ? NegativeFontFormat.format(text) : text;
    }

    private static String formatRaw(String text, String nickname) {
        text = text.replace("%id%", "-1");
        text = text.replace("%player%", nickname);

        return text;
    }

    private static String replaceValuePlaceholders(String text, String nickname, DatabaseStorage storage) {
        Matcher matcher = VALUE_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));

            if (index < 1 || index > 10) {
                matcher.appendReplacement(buffer, "");
                continue;
            }

            String value = storage.getValue(nickname, index);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(safe(value)));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String safe(String value) {
        return value != null ? value : "";
    }
}