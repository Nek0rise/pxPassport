package uwu.nekorise.pxPassport.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Skin {
    public static String getSkinBase64(Player player) {
        PlayerProfile profile = player.getPlayerProfile();

        Optional<ProfileProperty> textures = profile.getProperties().stream()
                .filter(p -> p.getName().equals("textures"))
                .findFirst();

        return textures.map(ProfileProperty::getValue).orElse(null);
    }
}
