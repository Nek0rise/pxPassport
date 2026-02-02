package uwu.nekorise.pxPassport.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.config.MainConfigStorage;
import uwu.nekorise.pxPassport.database.DatabaseStorage;
import uwu.nekorise.pxPassport.util.Skin;

public class OnJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        DatabaseStorage storage = PxPassport.getInstance().getDatabaseService().getStorage();
        if (storage.exists(player.getName())) {
            String skin = Skin.getSkinBase64(player);
            storage.updateAvatar(player.getName(), skin != null ? skin : MainConfigStorage.DEFAULT_SKIN);
        }
    }
}
