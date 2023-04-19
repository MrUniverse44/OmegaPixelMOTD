package me.blueslime.pixelmotd.listener.bungeecord.server;

import me.blueslime.pixelmotd.listener.bungeecord.BungeeListener;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.type.BungeePluginListener;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.ListUtil;
import me.blueslime.pixelmotd.utils.color.BungeeHexUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ServerConnectListener extends BungeePluginListener {
    public ServerConnectListener(PixelMOTD<?> plugin) {
        super(plugin, BungeeListener.SERVER_CONNECT);
        register();
    }

    @EventHandler
    public void on(ServerConnectEvent event) {
        if (event == null || event.isCancelled()) {
            return;
        }

        final ProxiedPlayer connection = event.getPlayer();

        if (connection == null) {
            return;
        }

        if (connection.getUniqueId() == null) {

            getLogs().info("The plugin will not apply the whitelist or blacklist check for a null-ip and will block player from joining to the server");
            event.setCancelled(true);
            connection.sendMessage(
                    colorize("&cBlocking you from joining to the server, reason: &6Null IP&c, if this is a error, contact to the server staff")
            );
            return;

        }

        final String username = connection.getName();

        final UUID uuid = connection.getUniqueId();

        final String serverName = event.getTarget().getName();

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        String path = getSerializer().toString(true) + "." + serverName;

        if (whitelist.getStatus(path +".enabled", false)) {
            if (!checkPlayer(ListType.WHITELIST, path, username) && !checkUUID(ListType.WHITELIST, path, uuid)) {
                String reason = ListUtil.ListToString(whitelist.getStringList("kick-message.individual"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        true,
                                        path,
                                        username,
                                        uuid.toString()
                                )
                        )
                );
                if (getSettings().getStatus("settings.debug-mode")) {
                    getLogs().debug("Event ServerConnectEvent will be cancelled because the path: whitelist." + path + ".enabled is true");
                }
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
                                        path,
                                        username,
                                        uuid.toString()
                                )
                        )
                );
                if (getSettings().getStatus("settings.debug-mode")) {
                    getLogs().debug("Event ServerConnectEvent will be cancelled because the path: blacklist." + path + ".enabled is true");
                }
                event.setCancelled(true);
            }
        }
    }

    public TextComponent colorize(String message) {
        return new TextComponent(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        BungeeHexUtil.convert(
                                message
                        )
                )
        );
    }
}
