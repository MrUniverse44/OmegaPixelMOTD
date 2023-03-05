package me.blueslime.pixelmotd.listener.velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.VelocityFavicon;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.storage.FileStorage;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.VelocityHover;
import me.blueslime.pixelmotd.motd.platforms.VelocityPing;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ProxyPingListener implements Ping {

    private final PixelMOTD<ProxyServer> plugin;

    private final VelocityPing pingBuilder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    public ProxyPingListener(PixelMOTD<ProxyServer> plugin) {
        this.pingBuilder = new VelocityPing(
                plugin,
                new VelocityFavicon(
                        plugin
                ),
                new VelocityHover(
                        plugin
                )
        );
        this.plugin = plugin;
        load();
    }
    private void load() {
        FileStorage fileStorage = plugin.getLoader().getFiles();

        final ConfigurationHandler control = fileStorage.getConfigurationHandler(Configuration.SETTINGS);

        this.unknown = plugin.getSettings().getString("settings.unknown-player", "unknown#1");

        ConfigurationHandler whitelist = plugin.getConfiguration(Configuration.WHITELIST);
        ConfigurationHandler blacklist = plugin.getConfiguration(Configuration.BLACKLIST);


        this.isWhitelisted = whitelist.getStatus("enabled") &&
                whitelist.getStatus("motd");

        this.isBlacklisted = blacklist.getStatus("enabled") &&
                blacklist.getStatus("motd");

        hasOutdatedClient = control.getStatus("settings.outdated-client-motd",true);
        hasOutdatedServer = control.getStatus("settings.outdated-server-motd",true);

        MAX_PROTOCOL = control.getInt("settings.max-server-protocol",756);
        MIN_PROTOCOL = control.getInt("settings.min-server-protocol",47);
    }

    public void update() {
        load();
        pingBuilder.update();
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onMotdPing(ProxyPingEvent event) {
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

        if (isBlacklisted && plugin.getConfiguration(Configuration.BLACKLIST).getStringList("players.by-name").contains(user)) {
            pingBuilder.execute(MotdType.BLACKLIST, event, protocol, user);
            return;
        }

        if (isWhitelisted) {
            pingBuilder.execute(MotdType.WHITELIST, event, protocol, user);
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            pingBuilder.execute(MotdType.NORMAL, event, protocol, user);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER, event, protocol, user);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, event, protocol, user);
        }
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }
}
