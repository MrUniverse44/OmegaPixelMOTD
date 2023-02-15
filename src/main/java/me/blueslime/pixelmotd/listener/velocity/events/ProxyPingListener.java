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

    private MotdType type;

    private ConfigurationHandler modes;

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

    public void updateModes() {
        modes = plugin.getConfigurationHandler(Configuration.MODES);
    }

    private void load() {
        updateModes();

        FileStorage fileStorage = plugin.getLoader().getFiles();

        final ConfigurationHandler control = fileStorage.getConfigurationHandler(Configuration.SETTINGS);

        type = MotdType.NORMAL;

        unknown = plugin.getSettings().getString("settings.unknown-player", "unknown#1");

        if (control.getString("settings.default-priority-motd", "DEFAULT").equalsIgnoreCase("HEX")) {
            type = MotdType.NORMAL_HEX;
        }

        isWhitelisted = modes.getStatus("whitelist.global.enabled") && modes.getStatus("whitelist.global.enable-motd");
        isBlacklisted = modes.getStatus("blacklist.global.enabled") && modes.getStatus("blacklist.global.enable-motd");

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

        if (isBlacklisted && modes.getStringList("blacklist.global.players.by-name").contains(user)) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.BLACKLIST_HEX, event, protocol, user);
                return;
            }
            pingBuilder.execute(MotdType.BLACKLIST, event, protocol, user);
            return;
        }

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, event, protocol, user);
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST, event, protocol, user);
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX, event, protocol, user);
                return;
            }
            pingBuilder.execute(type, event, protocol, user);
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
