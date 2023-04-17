package me.blueslime.pixelmotd.listener.bukkit.player;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.type.BukkitPluginListener;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.ListUtil;
import me.blueslime.pixelmotd.utils.color.BungeeHexUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class PlayerTeleportListener extends BukkitPluginListener implements Listener {

    public PlayerTeleportListener(PixelMOTD<?> plugin) {
        super(plugin);
        register();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerTeleportEvent event) {
        if (event == null || event.isCancelled()) {
            return;
        }

        final Player connection = event.getPlayer();

        final String username = connection.getName();

        final UUID uuid = connection.getUniqueId();

        final String playerWorld = connection.getWorld().getName();

        if (event.getTo() == null || event.getTo().getWorld() == null) {
            return;
        }

        final String target = event.getTo().getWorld().getName();

        if (playerWorld.equalsIgnoreCase(target)) {
            return;
        }

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        String path = getSerializer().toString(true) + "." + target;

        if (whitelist.getStatus(path + ".enabled", false)) {
            if (!checkPlayer(ListType.WHITELIST, path, username) && !checkUUID(ListType.WHITELIST, path, uuid)) {
                String reason = ListUtil.ListToString(whitelist.getStringList("kick-message.individual"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        true,
                                        target,
                                        username,
                                        uuid.toString()
                                )
                        )
                );

                event.setCancelled(true);
                return;
            }
        }

        if (blacklist.getStatus(path + ".enabled", false)) {
            if (checkPlayer(ListType.BLACKLIST, path, username) || checkUUID(ListType.BLACKLIST, path, uuid)) {
                String reason = ListUtil.ListToString(blacklist.getStringList("kick-message.individual"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        false,
                                        target,
                                        username,
                                        uuid.toString()
                                )
                        )
                );

                event.setCancelled(true);
            }
        }
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes(
                '&',
                BungeeHexUtil.convert(message)
        );
    }

}
