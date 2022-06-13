package dev.justjustin.pixelmotd.listener.velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.listener.Ping;
import dev.justjustin.pixelmotd.listener.velocity.VelocityMotdBuilder;
import dev.justjustin.pixelmotd.listener.velocity.VelocityPingBuilder;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.storage.FileStorage;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ProxyPingListener implements Ping {

    private final PixelMOTD<ProxyServer> slimePlugin;

    private final VelocityPingBuilder pingBuilder;

    private boolean hasOutdatedClient;

    private boolean hasOutdatedServer;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private int MIN_PROTOCOL;

    private int MAX_PROTOCOL;

    private String unknown;

    private MotdType type;

    private Control modes;

    public ProxyPingListener(PixelMOTD<ProxyServer> slimePlugin) {
        this.pingBuilder = new VelocityPingBuilder(
                slimePlugin,
                new VelocityMotdBuilder(
                        slimePlugin,
                        slimePlugin.getLogs()
                )
        );
        this.slimePlugin = slimePlugin;
        load();
    }

    public void updateModes() {
        modes = slimePlugin.getLoader().getFiles().getControl(SlimeFile.MODES);
    }

    private void load() {
        updateModes();

        FileStorage fileStorage = slimePlugin.getLoader().getFiles();

        final Control control = fileStorage.getControl(SlimeFile.SETTINGS);

        type = MotdType.NORMAL;

        unknown = slimePlugin.getLoader().getFiles().getControl(SlimeFile.SETTINGS).getString("settings.unknown-player", "unknown#1");

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
}
