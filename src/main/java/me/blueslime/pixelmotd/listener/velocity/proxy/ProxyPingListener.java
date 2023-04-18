package me.blueslime.pixelmotd.listener.velocity.proxy;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.utils.ping.Ping;
import me.blueslime.pixelmotd.listener.type.VelocityPluginListener;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.VelocityFavicon;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.VelocityHover;
import me.blueslime.pixelmotd.motd.platforms.VelocityPing;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ProxyPingListener extends VelocityPluginListener implements Ping {

    private final VelocityPing builder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    public ProxyPingListener(PixelMOTD<?> plugin) {
        super(plugin);
        register();

        builder = new VelocityPing(
                getBasePlugin(),
                new VelocityFavicon(
                        getBasePlugin()
                ),
                new VelocityHover(
                        getBasePlugin()
                )
        );

        reload();
    }

    @Override
    public void reload() {
        ConfigurationHandler settings = getSettings();

        this.unknown = settings.getString("settings.unknown-player", "unknown#1");

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();


        this.isWhitelisted = whitelist.getStatus("enabled") &&
                whitelist.getStatus("motd");

        this.isBlacklisted = blacklist.getStatus("enabled") &&
                blacklist.getStatus("motd");

        hasOutdatedClient = settings.getStatus("settings.outdated-client-motd",true);
        hasOutdatedServer = settings.getStatus("settings.outdated-server-motd",true);

        MAX_PROTOCOL = settings.getInt("settings.max-server-protocol",756);
        MIN_PROTOCOL = settings.getInt("settings.min-server-protocol",47);

        builder.update();
    }

    @Subscribe(order = PostOrder.EARLY)
    public void on(ProxyPingEvent event) {
        final ServerPing ping = event.getPing();

        if (ping == null) {
            return;
        }

        final int protocol = ping.getVersion().getProtocol();

        final String user;

        InboundConnection connection = event.getConnection();

        InetSocketAddress socketAddress = connection.getRemoteAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            user = getPlayerDatabase().getPlayer(address.getHostAddress(), unknown);
        } else {
            user = unknown;
        }

        if (isBlacklisted && getBlacklist().getStringList("players.by-name").contains(user)) {
            builder.execute(MotdType.BLACKLIST, event, protocol, user);
            return;
        }

        if (isWhitelisted) {
            builder.execute(MotdType.WHITELIST, event, protocol, user);
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            builder.execute(MotdType.NORMAL, event, protocol, user);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            builder.execute(MotdType.OUTDATED_SERVER, event, protocol, user);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            builder.execute(MotdType.OUTDATED_CLIENT, event, protocol, user);
        }
    }

    @Override
    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return builder;
    }
}
