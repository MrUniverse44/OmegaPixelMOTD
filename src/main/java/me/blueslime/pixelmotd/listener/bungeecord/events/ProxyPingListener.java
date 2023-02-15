package me.blueslime.pixelmotd.listener.bungeecord.events;

import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.BungeeFavicon;
import me.blueslime.pixelmotd.listener.bungeecord.BungeePingBuilder;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.SocketAddress;

public class ProxyPingListener implements Listener, Ping {

    private final BungeePingBuilder builder;
    private final PixelMOTD<Plugin> plugin;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    private MotdType type;

    private ConfigurationHandler modes;

    public ProxyPingListener(PixelMOTD<Plugin> plugin) {
        this.plugin = plugin;

        BungeeFavicon favicon = new BungeeFavicon(
                plugin
        );

        this.builder = new BungeePingBuilder(
                plugin,
                favicon
        );
        load();
    }

    public void update() {
        load();
        builder.update();
    }

    private void load() {
        this.modes = plugin.getConfigurationHandler(Configuration.MODES);

        final ConfigurationHandler control = plugin.getSettings();

        if (control != null) {
            this.unknown = control.getString("settings.unknown-player", "unknown#1");

            if (control.getString("settings.default-priority-motd", "DEFAULT").equalsIgnoreCase("HEX")) {
                this.type = MotdType.NORMAL_HEX;
            } else {
                this.type = MotdType.NORMAL;
            }
        } else {
            this.unknown = "unknown#1";
        }


        this.isWhitelisted = modes.getStatus("whitelist.global.enabled") && modes.getStatus("whitelist.global.enable-motd");
        this.isBlacklisted = modes.getStatus("blacklist.global.enabled") && modes.getStatus("blacklist.global.enable-motd");

        if (control != null) {
            hasOutdatedClient = control.getStatus("settings.outdated-client-motd", true);
            hasOutdatedServer = control.getStatus("settings.outdated-server-motd", true);

            MAX_PROTOCOL = control.getInt("settings.max-server-protocol", 756);
            MIN_PROTOCOL = control.getInt("settings.min-server-protocol", 47);
        } else {
            hasOutdatedClient = true;
            hasOutdatedServer = true;

            MAX_PROTOCOL = 758;
            MIN_PROTOCOL = 47;
        }
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
                builder.execute(MotdType.BLACKLIST_HEX, ping, protocol, userName);
                return;
            }
            builder.execute(MotdType.BLACKLIST, ping, protocol, userName);
            return;
        }

        if (isWhitelisted) {
            if (protocol >= 735) {
                builder.execute(MotdType.WHITELIST_HEX, ping, protocol, userName);
                return;
            }
            builder.execute(MotdType.WHITELIST, ping, protocol, userName);
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                builder.execute(MotdType.NORMAL_HEX, ping, protocol, userName);
                return;
            }
            builder.execute(type, ping, protocol, userName);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            builder.execute(MotdType.OUTDATED_SERVER, ping, protocol, userName);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            builder.execute(MotdType.OUTDATED_CLIENT, ping, protocol, userName);
        }

    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return builder;
    }

}
