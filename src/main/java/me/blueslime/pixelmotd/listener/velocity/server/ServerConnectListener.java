package me.blueslime.pixelmotd.listener.velocity.server;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.blueslime.pixelmotd.listener.velocity.VelocityListener;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.type.VelocityPluginListener;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.ListUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Optional;
import java.util.UUID;

public class ServerConnectListener extends VelocityPluginListener {
    public ServerConnectListener(PixelMOTD<?> plugin) {
        super(plugin, VelocityListener.SERVER_CONNECT);
        register();
    }

    @Subscribe(order = PostOrder.EARLY)
    public void on(ServerPreConnectEvent event) {
        if (event == null) {
            return;
        }

        final Player connection = event.getPlayer();

        final ServerInfo previous = event.getOriginalServer().getServerInfo();
        final Optional<RegisteredServer> serverOptional   = event.getResult().getServer();

        final ServerInfo target;

        if (!serverOptional.isPresent()) {
            return;
        }

        target = serverOptional.get().getServerInfo();

        if (previous.getName().equalsIgnoreCase(target.getName())) {
            return;
        }

        if (connection == null) {
            return;
        }

        if (connection.getUniqueId() == null) {

            getLogs().info("The plugin will not apply the whitelist or blacklist check for a null-ip and will block player from joining to the server");
            event.setResult(
                    deny()
            );
            connection.sendMessage(
                    colorize("&cBlocking you from joining to the server, reason: &6Null IP&c, if this is a error, contact to the server staff")
            );
            return;

        }

        final String username = connection.getUsername();

        final UUID uuid = connection.getUniqueId();

        final String serverName = target.getName();

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        String path = getSerializer().toString(true) + "." + serverName;

        if (whitelist.getStatus(path + ".enabled", false)) {
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

                event.setResult(
                        deny()
                );
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

                event.setResult(
                        deny()
                );
            }
        }
    }

    private ServerPreConnectEvent.ServerResult deny() {
        return ServerPreConnectEvent.ServerResult.denied();
    }

    public Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
