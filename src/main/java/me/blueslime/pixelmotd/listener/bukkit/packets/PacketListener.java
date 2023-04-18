package me.blueslime.pixelmotd.listener.bukkit.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.utils.ping.Ping;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.version.player.PlayerVersionHandler;
import me.blueslime.pixelmotd.version.player.handlers.ProtocolLib;
import me.blueslime.pixelmotd.listener.type.BukkitPacketPluginListener;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.ProtocolFavicon;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.ProtocolHover;
import me.blueslime.pixelmotd.motd.platforms.ProtocolPing;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PacketListener extends BukkitPacketPluginListener implements Ping {
    private final PlayerVersionHandler playerVersionHandler = new ProtocolLib();

    private final ProtocolPing builder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    public PacketListener(PixelMOTD<?> plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Status.Server.SERVER_INFO);
        register();

        this.builder = new ProtocolPing(
                getBasePlugin(),
                new ProtocolFavicon(
                        getBasePlugin()
                ),
                new ProtocolHover(
                        getBasePlugin()
                )
        );

        reload();
    }

    @Override
    public void reload() {
        final ConfigurationHandler control = getSettings();

        unknown = control.getString("settings.unknown-player", "unknown#1");

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        this.isWhitelisted = whitelist.getStatus("enabled") &&
                whitelist.getStatus("motd");

        this.isBlacklisted = blacklist.getStatus("enabled") &&
                blacklist.getStatus("motd");

        hasOutdatedClient = control.getStatus("settings.outdated-client-motd",true);
        hasOutdatedServer = control.getStatus("settings.outdated-server-motd",true);

        MAX_PROTOCOL = control.getInt("settings.max-server-protocol",756);
        MIN_PROTOCOL = control.getInt("settings.min-server-protocol",47);

        builder.update();
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) {
            if (getSettings().getBoolean("settings.debug-mode", false)) {
                getLogs().debug("The plugin is receiving a different packet of SERVER_INFO, the received packet is: " + event.getPacketType());
            }
            return;
        }

        if (event.isCancelled()) {
            if (getSettings().getBoolean("settings.debug-mode", false)) {
                getLogs().debug("Another plugin is cancelling your motd event, the plugin can't show the motd :(");
            }
            return;
        }

        if (event.getPlayer() == null && !getSettings().getBoolean("bukkit.allow-null-connections.enabled", false)) {
            return;
        }

        InetSocketAddress socketAddress = null;

        if (event.getPlayer() != null) {
            socketAddress = event.getPlayer().getAddress();
        }

        final String user;

        int protocol;

        if (event.getPlayer() != null) {
            protocol = playerVersionHandler.getProtocol(event.getPlayer());
        } else {
            protocol = getSettings().getInt("bukkit.allow-null-connections.default-protocol", 762);
        }

        if (socketAddress != null) {
            final InetAddress address = socketAddress.getAddress();

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
