package uwu.nekorise.pxPassport.inventory;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import uwu.nekorise.pxPassport.PxPassport;
import uwu.nekorise.pxPassport.config.GuiConfigStorage;
import uwu.nekorise.pxPassport.database.DatabaseStorage;
import uwu.nekorise.pxPassport.entity.AvatarHasIncorrectMaterial;
import uwu.nekorise.pxPassport.entity.ItemType;
import uwu.nekorise.pxPassport.entity.PassportGUI;
import uwu.nekorise.pxPassport.entity.PassportItem;
import uwu.nekorise.pxPassport.util.MMessage;
import uwu.nekorise.pxPassport.util.PlaceholderFormat;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
// im not proud of this code
public class GUIBuilder {
    public PassportGUI build(Map<String, PassportItem> passportItems, String targetPlayer) {
        DatabaseStorage storage = PxPassport.getInstance().getDatabaseService().getStorage();

        String guiName = GuiConfigStorage.getPassportName();
        String formattedGuiName = PlaceholderFormat.format(guiName, targetPlayer, Bukkit.getServer().getPlayer(targetPlayer), storage);
        Component title = MMessage.applyColor(formattedGuiName);

        PassportGUI passportGUI = new PassportGUI(title, GuiConfigStorage.getPassportSize(), targetPlayer);

        for (PassportItem original : passportItems.values()) {
            PassportItem passportItem = original.clone();
            if (passportItem.getItemType().equals(ItemType.AVATAR)) { // Avatar set
                if (!passportItem.getType().equals(Material.PLAYER_HEAD)) {
                    throw new AvatarHasIncorrectMaterial("Avatar can only have PLAYER_HEAD material");
                }
                SkullMeta skullMeta = (SkullMeta) passportItem.getItemMeta();

                String base64Texture = storage.getAvatar(targetPlayer);
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
                profile.setProperty(new ProfileProperty("textures", base64Texture));
                skullMeta.setPlayerProfile(profile);
                passportItem.setItemMeta(skullMeta);
            }

            ItemMeta meta = passportItem.getItemMeta(); // Display name set
            String displayName = meta.getDisplayName();
            if (displayName != null) {
                Component displayComponent = MMessage.applyColor(PlaceholderFormat.format(displayName, targetPlayer, Bukkit.getServer().getPlayer(targetPlayer), storage));
                meta.displayName(displayComponent);
            }

            List<String> lore = meta.getLore(); // Lore set
            if (lore != null) {
                List<Component> loreComponents = lore.stream().map(s -> PlaceholderFormat.format(s, targetPlayer, Bukkit.getServer().getPlayer(targetPlayer), storage)).map(MMessage::applyColor).collect(Collectors.toList());
                meta.lore(loreComponents);
            }
            passportItem.setItemMeta(meta);
            passportGUI.getInventory().setItem(passportItem.getIndex(), passportItem.toItemStack());
        }

        return passportGUI;
    }
}
