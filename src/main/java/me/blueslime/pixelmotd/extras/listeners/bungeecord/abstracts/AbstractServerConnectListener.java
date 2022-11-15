package me.blueslime.pixelmotd.extras.listeners.bungeecord.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.extras.listeners.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

public class AbstractServerConnectListener extends ConnectionListener<Plugin, ServerConnectEvent, TextComponent> implements Listener {

    public AbstractServerConnectListener(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void execute(ServerConnectEvent event) {
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

        ConfigurationHandler settings = getControl();

        String path = getPlace().toStringLowerCase() + "." + serverName;

        if (settings.getStatus("whitelist." + path + ".enabled", false)) {
            if (!checkPlayer(ListType.WHITELIST, path, username) && !checkUUID(ListType.WHITELIST, path, uuid)) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.whitelist"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        "whitelist." + path,
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

        if (settings.getStatus("blacklist." + path + ".enabled", false)) {
            if (checkPlayer(ListType.BLACKLIST, path, username) || checkUUID(ListType.BLACKLIST, path, uuid)) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.blacklist"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        "blacklist." + path,
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

    @Override
    public TextComponent colorize(String message) {
        return new TextComponent(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        message
                )
        );
    }
}
