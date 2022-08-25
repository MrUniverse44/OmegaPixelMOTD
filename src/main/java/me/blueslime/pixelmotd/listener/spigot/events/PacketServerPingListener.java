package me.blueslime.pixelmotd.listener.spigot.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.listener.PingBuilder;
import me.blueslime.pixelmotd.listener.spigot.packets.PacketSpigotMotdBuilder;
import me.blueslime.pixelmotd.listener.spigot.packets.PacketSpigotPingBuilder;
import me.blueslime.pixelmotd.listener.spigot.version.PlayerVersionHandler;
import me.blueslime.pixelmotd.listener.spigot.version.handlers.None;
import me.blueslime.pixelmotd.listener.spigot.version.handlers.ProtocolLib;
import me.blueslime.pixelmotd.listener.spigot.version.handlers.ViaVersion;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.storage.FileStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PacketServerPingListener extends PacketAdapter implements Ping {

    private final PlayerVersionHandler playerVersionHandler;

    private final PixelMOTD<JavaPlugin> slimePlugin;

    private final PacketSpigotPingBuilder pingBuilder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private boolean isDebug;

    private String unknown;

    private MotdType type;

    private ConfigurationHandler modes;

    public PacketServerPingListener(PixelMOTD<JavaPlugin> slimePlugin) {
        super(slimePlugin.getPlugin(), ListenerPriority.HIGHEST, PacketType.Status.Server.SERVER_INFO);

        this.pingBuilder = new PacketSpigotPingBuilder(
                slimePlugin,
                new PacketSpigotMotdBuilder(
                        slimePlugin,
                        slimePlugin.getLogs()
                )
        );

        if (slimePlugin.getPlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            this.playerVersionHandler = new ProtocolLib();
        } else {
            if (slimePlugin.getPlugin().getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
                this.playerVersionHandler = new None();
            } else {
                this.playerVersionHandler = new ViaVersion();
            }
        }

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

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (event.getPlayer() == null) {
            return;
        }

        final WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        if (ping == null) {
            return;
        }

        final InetSocketAddress socketAddress = event.getPlayer().getAddress();

        final String user;

        final int protocol = playerVersionHandler.getProtocol(event.getPlayer());

        SlimeLogs logs = slimePlugin.getLogs();

        if (isDebug) {
            logs.debug("Loading motd for a specified user, client-protocol: " + protocol);
        }

        if (socketAddress != null) {
            final InetAddress address = socketAddress.getAddress();

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
                pingBuilder.execute(MotdType.BLACKLIST_HEX, ping, protocol, user);
                if (isDebug) {
                    logs.debug("Showing BLACKLIST_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.BLACKLIST, ping, protocol, user);
            if (isDebug) {
                logs.debug("Showing BLACKLIST, client-protocol: " + protocol);
            }
            return;
        }

        if (isWhitelisted) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, ping, protocol, user);
                if (isDebug) {
                    logs.debug("Showing WHITELIST_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST, ping, protocol, user);
            if (isDebug) {
                logs.debug("Showing WHITELIST, client-protocol: " + protocol);
            }
            return;
        }

        if (!hasOutdatedClient && !hasOutdatedServer || protocol >= MIN_PROTOCOL && protocol <= MAX_PROTOCOL) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.NORMAL_HEX, ping, protocol, user);
                if (isDebug) {
                    logs.debug("Showing NORMAL_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(type, ping, protocol, user);
            if (isDebug) {
                logs.debug("Showing MOTD Priority from settings.yml:" + type.original() + ", client-protocol: " + protocol);
            }
            return;
        }
        if (MAX_PROTOCOL < protocol && hasOutdatedServer) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.OUTDATED_SERVER_HEX, ping, protocol, user);
                if (isDebug) {
                    logs.debug("Showing OUTDATED_SERVER_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.OUTDATED_SERVER, ping, protocol, user);
            if (isDebug) {
                logs.debug("Showing OUTDATED_SERVER, client-protocol: " + protocol);
            }
            return;
        }
        if (MIN_PROTOCOL > protocol && hasOutdatedClient) {
            if (protocol >= 735) {
                pingBuilder.execute(MotdType.OUTDATED_CLIENT_HEX, ping, protocol, user);
                if (isDebug) {
                    logs.debug("Showing OUTDATED_CLIENT_HEX, client-protocol: " + protocol);
                }
                return;
            }
            pingBuilder.execute(MotdType.OUTDATED_CLIENT, ping, protocol, user);
            if (isDebug) {
                logs.debug("Showing OUTDATED_CLIENT, client-protocol: " + protocol);
            }
        }
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }

}
