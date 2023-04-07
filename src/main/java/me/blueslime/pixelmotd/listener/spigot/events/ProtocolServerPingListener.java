package me.blueslime.pixelmotd.listener.spigot.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.ProtocolFavicon;
import me.blueslime.pixelmotd.listener.spigot.version.PlayerVersionHandler;
import me.blueslime.pixelmotd.listener.spigot.version.handlers.None;
import me.blueslime.pixelmotd.listener.spigot.version.handlers.ProtocolLib;
import me.blueslime.pixelmotd.listener.spigot.version.handlers.ViaVersion;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.storage.FileStorage;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.ProtocolHover;
import me.blueslime.pixelmotd.motd.platforms.ProtocolPing;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ProtocolServerPingListener extends PacketAdapter implements Ping {

    private final PlayerVersionHandler playerVersionHandler;

    private final PixelMOTD<JavaPlugin> plugin;

    private final ProtocolPing pingBuilder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    public ProtocolServerPingListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin.getPlugin(), ListenerPriority.HIGHEST, PacketType.Status.Server.SERVER_INFO);

        this.pingBuilder = new ProtocolPing(
                plugin,
                new ProtocolFavicon(
                        plugin
                ),
                new ProtocolHover(
                        plugin
                )
        );

        if (plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            this.playerVersionHandler = new ProtocolLib();
        } else {
            if (plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
                this.playerVersionHandler = new None();
            } else {
                this.playerVersionHandler = new ViaVersion();
            }
        }

        this.plugin = plugin;
        load();
    }

    public void update() {
        load();
        pingBuilder.update();
    }

    private void load() {
        FileStorage fileStorage = plugin.getLoader().getFiles();

        final ConfigurationHandler control = fileStorage.getConfigurationHandler(Configuration.SETTINGS);

        unknown = plugin.getSettings().getString("settings.unknown-player", "unknown#1");

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

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) {
            if (plugin.getSettings().getBoolean("settings.debug-mode", false)) {
                plugin.getLogs().debug("The plugin is receiving a different packet of SERVER_INFO, the received packet is: " + event.getPacketType());
            }
            return;
        }

        if (event.isCancelled()) {
            if (plugin.getSettings().getBoolean("settings.debug-mode", false)) {
                plugin.getLogs().debug("Another plugin is cancelling your motd event, the plugin can't show the motd :(");
            }
            return;
        }

        if (event.getPlayer() == null && !plugin.getSettings().getBoolean("bukkit.allow-null-connections.enabled", false)) {
            return;
        }

        WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        if (ping == null) {
            if (event.getPacket().getServerPings().size() > 2) {
                ping = event.getPacket().getServerPings().read(1);
            }
        }

        if (ping == null) {
            if (plugin.getSettings().getBoolean("settings.debug-mode", false)) {
                plugin.getLogs().debug("The plugin is receiving a null ping from ProtocolLib, please report it to ProtocolLib, this issue is not caused by PixelMOTD");
            }
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
            protocol = plugin.getSettings().getInt("bukkit.allow-null-connections.default-protocol", 762);
        }

        if (socketAddress != null) {
            final InetAddress address = socketAddress.getAddress();

            user = getPlayerDatabase().getPlayer(address.getHostAddress(), unknown);
        } else {
            user = unknown;
        }

        if (isBlacklisted && plugin.getConfiguration(Configuration.BLACKLIST).getStringList("players.by-name").contains(user)) {
            pingBuilder.execute(MotdType.BLACKLIST, ping, protocol, user);
            return;
        }

        if (isWhitelisted) {
            pingBuilder.execute(MotdType.WHITELIST, ping, protocol, user);
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            pingBuilder.execute(MotdType.NORMAL, ping, protocol, user);
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            pingBuilder.execute(MotdType.OUTDATED_SERVER, ping, protocol, user);
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, ping, protocol, user);
        }
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }

}
