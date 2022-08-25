package me.blueslime.pixelmotd.listener.velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.listener.PingBuilder;
import me.blueslime.pixelmotd.listener.velocity.VelocityMotdBuilder;
import me.blueslime.pixelmotd.listener.velocity.VelocityPingBuilder;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.storage.FileStorage;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ProxyPingListener implements Ping {

    private final PixelMOTD<ProxyServer> slimePlugin;

    private final VelocityPingBuilder pingBuilder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private boolean isDebug;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    private MotdType type;

    private ConfigurationHandler modes;

    public ProxyPingListener(PixelMOTD<ProxyServer> slimePlugin, SlimeLogs logs) {
        this.pingBuilder = new VelocityPingBuilder(
                slimePlugin,
                new VelocityMotdBuilder(
                        slimePlugin,
                        logs
                )
        );
        this.slimePlugin = slimePlugin;
        load();
    }

    public void updateModes() {
        modes = slimePlugin.getConfigurationHandler(SlimeFile.MODES);
    }

    private void load() {
        updateModes();

        FileStorage fileStorage = slimePlugin.getLoader().getFiles();

        final ConfigurationHandler control = fileStorage.getConfigurationHandler(SlimeFile.SETTINGS);

        type = MotdType.NORMAL;

        unknown = slimePlugin.getConfigurationHandler(SlimeFile.SETTINGS).getString("settings.unknown-player", "unknown#1");

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

        SlimeLogs logs = slimePlugin.getLogs();

        if (ping == null) {
            return;
        }

        final int protocol = ping.getVersion().getProtocol();

        if (isDebug) {
            logs.debug("Loading motd for a specified user, client-protocol: " + protocol);
        }

        final String user;

        InboundConnection connection = event.getConnection();

        InetSocketAddress socketAddress = connection.getRemoteAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            user = getPlayerDatabase().getPlayer(address.getHostAddress(), unknown);
            if (isDebug) {
                logs.debug("User has been found in the database: " + user + ", client-protocol: " + protocol);
            }
        } else {
            user = unknown;
            if (isDebug) {
                logs.debug("This user is not in cache so it will show unknown player to the current loading motd, client-protocol: " + protocol);
            }
        }

        if (isBlacklisted && modes.getStringList("blacklist.global.players.by-name").contains(user)) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.BLACKLIST_HEX, event, protocol, user);
                if (isDebug) {
                    logs.debug("Showing BLACKLIST_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.BLACKLIST, event, protocol, user);
            if (isDebug) {
                logs.debug("Showing BLACKLIST, client-protocol: " + protocol);
            }
            return;
        }

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, event, protocol, user);
                if (isDebug) {
                    logs.debug("Showing WHITELIST_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST, event, protocol, user);
            if (isDebug) {
                logs.debug("Showing WHITELIST, client-protocol: " + protocol);
            }
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX, event, protocol, user);
                if (isDebug) {
                    logs.debug("Showing NORMAL_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(type, event, protocol, user);
            if (isDebug) {
                logs.debug("Showing MOTD Priority from settings.yml:" + type.original() + ", client-protocol: " + protocol);
            }
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.OUTDATED_SERVER_HEX, event, protocol, user);
                if (isDebug) {
                    logs.debug("Showing OUTDATED_SERVER_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.OUTDATED_SERVER, event, protocol, user);
            if (isDebug) {
                logs.debug("Showing OUTDATED_SERVER, client-protocol: " + protocol);
            }
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.OUTDATED_CLIENT_HEX, event, protocol, user);
                if (isDebug) {
                    logs.debug("Showing OUTDATED_CLIENT_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, event, protocol, user);
            if (isDebug) {
                logs.debug("Showing OUTDATED_CLIENT, client-protocol: " + protocol);
            }
        }
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }
}
