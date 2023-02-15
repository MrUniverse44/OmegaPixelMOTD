package me.blueslime.pixelmotd.listener.spigot.events;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.BukkitFavicon;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.BukkitHover;
import me.blueslime.pixelmotd.motd.platforms.BukkitPing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;

public class ServerPingListener implements Ping, Listener {

    private final PixelMOTD<JavaPlugin> plugin;

    private final BukkitPing pingBuilder;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private String unknown;

    private MotdType type;

    private ConfigurationHandler modes;

    public ServerPingListener(PixelMOTD<JavaPlugin> plugin) {
        this.pingBuilder = new BukkitPing(
                plugin,
                new BukkitFavicon(
                        plugin
                ),
                new BukkitHover(
                        plugin
                )
        );

        this.plugin = plugin;
        load();
    }

    public void update() {
        load();
        pingBuilder.update();
    }

    public void updateModes() {
        modes = plugin.getConfigurationHandler(Configuration.MODES);
    }

    private void load() {
        updateModes();

        final ConfigurationHandler control = plugin.getSettings();

        this.unknown = control.getString("settings.unknown-player", "unknown#1");

        if (control.getString("settings.default-priority-motd", "DEFAULT").equalsIgnoreCase("HEX")) {
            this.type = MotdType.NORMAL_HEX;
        } else {
            this.type = MotdType.NORMAL;
        }

        isWhitelisted = modes.getStatus("whitelist.global.enabled") && modes.getStatus("whitelist.global.enable-motd");
        isBlacklisted = modes.getStatus("blacklist.global.enabled") && modes.getStatus("blacklist.global.enable-motd");
    }

    @EventHandler
    public void onPing(ServerListPingEvent ping) {

        final InetAddress address = ping.getAddress();

        final String user = getPlayerDatabase().getPlayer(address.getHostAddress(), unknown);

        if (isBlacklisted && modes.getStringList("blacklist.global.players.by-name").contains(user)) {
            if (type.isHexMotd()) {
                pingBuilder.execute(MotdType.BLACKLIST_HEX, ping, 735, user);
                return;
            }
            pingBuilder.execute(MotdType.BLACKLIST, ping, -1, user);
            return;
        }

        if (isWhitelisted) {
            if (type.isHexMotd()) {
                pingBuilder.execute(MotdType.WHITELIST_HEX, ping, 735, user);
                return;
            }
            pingBuilder.execute(MotdType.WHITELIST, ping, -1, user);
            return;
        }

        if (type.isHexMotd()) {
            pingBuilder.execute(MotdType.NORMAL_HEX, ping, 735, user);
            return;
        }
        pingBuilder.execute(type, ping, -1, user);
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }

}
