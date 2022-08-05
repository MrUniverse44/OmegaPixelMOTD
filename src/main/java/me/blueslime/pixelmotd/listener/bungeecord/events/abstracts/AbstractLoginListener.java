package me.blueslime.pixelmotd.listener.bungeecord.events.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.SocketAddress;

public class AbstractLoginListener extends ConnectionListener<Plugin, LoginEvent, TextComponent> implements Listener {

    public AbstractLoginListener(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @Override
    public void execute(LoginEvent event) {
        if (event == null || event.isCancelled()) {
            return;
        }

        final PendingConnection connection = event.getConnection();

        if (connection == null) {
            return;
        }

        if (connection.getUniqueId() == null) {

            getLogs().info("The plugin will not apply the whitelist or blacklist check for a null-ip and will block player from joining to the server");
            event.setCancelled(true);
            event.setCancelReason(
                    colorize("&cBlocking you from joining to the server, reason: &6Null IP&c, if this is a error, contact to the server staff")
            );
            return;

        }

        final SocketAddress address = connection.getSocketAddress();

        final String username = connection.getName();

        final String uuid = connection.getUniqueId().toString();

        getPlayerDatabase().fromSocket(
                address.toString(),
                username
        );

        ConfigurationHandler settings = getControl();

        String path = ".global.players.by-";

        if (hasWhitelist()) {
            if (settings.getStringList("whitelist" + path + "name").contains(username) ||
                    !settings.getStringList("whitelist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-whitelist"));

                event.setCancelReason(
                        colorize(
                                replace(
                                        reason,
                                        "whitelist.global",
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
                    !settings.getStringList("blacklist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-blacklist"));

                event.setCancelReason(
                        colorize(
                                replace(
                                        reason,
                                        "blacklist.global",
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
