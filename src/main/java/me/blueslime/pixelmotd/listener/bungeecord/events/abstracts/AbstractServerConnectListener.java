package me.blueslime.pixelmotd.listener.bungeecord.events.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class AbstractServerConnectListener extends ConnectionListener<Plugin, ServerConnectEvent, TextComponent> implements Listener {

    public AbstractServerConnectListener(PixelMOTD<Plugin> plugin) {
        super(plugin);
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

        final String uuid = connection.getUniqueId().toString();

        final String serverName = event.getTarget().getName();

        ConfigurationHandler settings = getControl();

        String path = "." + getPlace().toStringLowerCase() + "." + serverName + ".players.by-";

        if (settings.getStatus("whitelist" + serverName + ".enabled", false)) {
            if (!settings.getStringList("whitelist" + path + "name").contains(username) ||
                    !settings.getStringList("whitelist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.whitelist"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        "whitelist." + serverName,
                                        username,
                                        uuid
                                )
                        )
                );

                event.setCancelled(true);
                return;
            }
        }

        if (hasBlacklist()) {
            if (settings.getStringList("blacklist" + path + "name").contains(username) ||
                    settings.getStringList("blacklist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.blacklist"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        "blacklist." + serverName,
                                        username,
                                        uuid
                                )
                        )
                );

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
