package uwu.nekorise.pxPassport.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import uwu.nekorise.pxPassport.config.MainConfigStorage;

public class MMessage {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component applyColor(String text) {
        if (!MainConfigStorage.isMiniMessageFormatting()) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
        }
        return miniMessage.deserialize(text);
    }
}

