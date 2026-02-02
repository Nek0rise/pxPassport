package uwu.nekorise.pxPassport.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uwu.nekorise.pxPassport.config.GuiConfigStorage;
import uwu.nekorise.pxPassport.config.LangConfigStorage;
import uwu.nekorise.pxPassport.config.MainConfigStorage;
import uwu.nekorise.pxPassport.database.DatabaseStorage;
import uwu.nekorise.pxPassport.entity.PassportGUI;
import uwu.nekorise.pxPassport.entity.PassportItem;
import uwu.nekorise.pxPassport.inventory.GUIBuilder;
import uwu.nekorise.pxPassport.util.MMessage;
import uwu.nekorise.pxPassport.util.PlaceholderFormat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PassportRequestService {

    private final DatabaseStorage storage;
    private final Map<UUID, UUID> pendingRequests = new ConcurrentHashMap<>();

    public PassportRequestService(DatabaseStorage storage) {
        this.storage = storage;
    }

    public void requestView(Player requester, String ownerNick) {
        Player owner = findNearbyPlayer(requester, ownerNick, MainConfigStorage.getViewRadius());

        if (owner == null) {
            requester.sendMessage(MMessage.applyColor(LangConfigStorage.getViewPlayerIsFar()));
            return;
        }

        if (!storage.exists(owner.getName())) {
            requester.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerWithoutPassport()));
            return;
        }

        pendingRequests.put(owner.getUniqueId(), requester.getUniqueId());

        String message = LangConfigStorage.getViewRequest();
        String requestMessage = PlaceholderFormat.format(message, requester.getName(), null, null);
        owner.sendMessage(MMessage.applyColor(requestMessage));

        requester.sendMessage(MMessage.applyColor(LangConfigStorage.getViewRequestSent()));
    }

    public void accept(Player owner) {
        UUID requesterId = pendingRequests.remove(owner.getUniqueId());
        if (requesterId == null) {
            owner.sendMessage(MMessage.applyColor(LangConfigStorage.getNoRequests()));
            return;
        }

        Player requester = Bukkit.getPlayer(requesterId);
        if (requester == null) {
            owner.sendMessage(MMessage.applyColor(LangConfigStorage.getPlayerOffline()));
            return;
        }

        openPassport(owner.getName(), requester);
        owner.sendMessage(MMessage.applyColor(LangConfigStorage.getAccept()));
    }

    public void deny(Player owner) {
        UUID requesterId = pendingRequests.remove(owner.getUniqueId());
        if (requesterId == null) {
            owner.sendMessage(MMessage.applyColor(LangConfigStorage.getNoRequests()));
            return;
        }

        Player requester = Bukkit.getPlayer(requesterId);
        if (requester != null) {
            requester.sendMessage(MMessage.applyColor(LangConfigStorage.getViewRequestDeny()));
        }
        owner.sendMessage(MMessage.applyColor(LangConfigStorage.getDeny()));
    }

    public void openPassport(String ownerNick, Player viewer) {
        GUIBuilder builder = new GUIBuilder();
        Map<String, PassportItem> items = GuiConfigStorage.getPassportItems();
        PassportGUI gui = builder.build(items, ownerNick);
        viewer.openInventory(gui.getInventory());
    }

    private Player findNearbyPlayer(Player center, String nickname, double radius) {
        return center.getNearbyEntities(radius, radius, radius).stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .filter(p -> p.getName().equalsIgnoreCase(nickname))
                .findFirst()
                .orElse(null);
    }
}
