package me.blueslime.pixelmotd.listener.sponge.server;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.utils.ping.Ping;
import me.blueslime.pixelmotd.listener.type.SpongePluginListener;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.SpongeFavicon;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.SpongeHover;
import me.blueslime.pixelmotd.motd.platforms.SpongePing;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.StatusClient;
import org.spongepowered.api.util.Tristate;

import java.net.SocketAddress;

public class ServerPingListener extends SpongePluginListener implements Ping {

    private final SpongePing builder;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private String unknown;

    public ServerPingListener(PixelMOTD<Server> plugin) {
        super(plugin);
        register();

        builder = new SpongePing(
                getBasePlugin(),
                new SpongeFavicon(
                        getBasePlugin()
                ),
                new SpongeHover(
                        getBasePlugin()
                )
        );

        reload();
    }

    @Override
    public void reload() {
        ConfigurationHandler settings = getSettings();

        if (settings != null) {
            this.unknown = settings.getString("settings.unknown-player", "unknown#1");
        } else {
            this.unknown = "unknown#1";
        }

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();


        this.isWhitelisted = whitelist.getStatus("enabled") &&
                whitelist.getStatus("motd");

        this.isBlacklisted = blacklist.getStatus("enabled") &&
                blacklist.getStatus("motd");
    }

    @IsCancelled(value = Tristate.UNDEFINED)
    @Listener
    public void onClientPingServer(ClientPingServerEvent event) {
        if (event == null || event.isCancelled()) {
            return;
        }

        final StatusClient connection = event.client();

        final SocketAddress address = connection.address();

        final String userName = getPlayerDatabase().getPlayer(
                address.toString(), unknown
        );

        if (isBlacklisted && getBlacklist().getStringList("players.by-name").contains(userName)) {
            builder.execute(MotdType.BLACKLIST, event, -1, userName);
            return;
        }

        if (isWhitelisted) {
            builder.execute(MotdType.WHITELIST, event, -1, userName);
            return;
        }

        builder.execute(MotdType.NORMAL, event, -1, userName);
        return;
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return builder;
    }
}
