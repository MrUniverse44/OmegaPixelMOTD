package me.blueslime.pixelmotd.listener.bungeecord.events;

import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.listener.PingBuilder;
import me.blueslime.pixelmotd.listener.bungeecord.BungeeMotdBuilder;
import me.blueslime.pixelmotd.listener.bungeecord.BungeePingBuilder;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.storage.FileStorage;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.SocketAddress;

public class ProxyPingListener implements Listener, Ping {

    private final SlimePlugin<Plugin> slimePlugin;

    private final BungeePingBuilder pingBuilder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    private MotdType type;

    private ConfigurationHandler modes;

    public ProxyPingListener(PixelMOTD<Plugin> slimePlugin, SlimeLogs logs) {
        this.pingBuilder = new BungeePingBuilder(
                slimePlugin,
                new BungeeMotdBuilder(slimePlugin, logs)
        );
        this.slimePlugin = slimePlugin;
        load();
    }

    public void update() {
        load();
        pingBuilder.update();
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

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();

        if (ping == null || event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            return;
        }

        final PendingConnection connection = event.getConnection();

        final SocketAddress address = connection.getSocketAddress();

        final int protocol = connection.getVersion();

        final String userName = getPlayerDatabase().getPlayer(
                address.toString(), unknown
        );

        if (isBlacklisted && modes.getStringList("blacklist.global.players.by-name").contains(userName)) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.BLACKLIST_HEX, ping, protocol, userName);
                return;
            }
            pingBuilder.execute(MotdType.BLACKLIST, ping, protocol, userName);
            return;
        }

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, ping, protocol, userName);
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST, ping, protocol, userName);
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX, ping, protocol, userName);
                return;
            }
            pingBuilder.execute(type, ping, protocol, userName);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.OUTDATED_SERVER_HEX, ping, protocol, userName);
                return;
            }
            pingBuilder.execute(MotdType.OUTDATED_SERVER, ping, protocol, userName);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.OUTDATED_CLIENT_HEX, ping, protocol, userName);
                return;
            }
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, ping, protocol, userName);
        }

    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }

}
