package me.blueslime.pixelmotd.listener.bukkit.server;

import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.utils.ping.Ping;
import me.blueslime.pixelmotd.listener.type.BukkitPluginListener;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.platforms.BukkitFavicon;
import me.blueslime.pixelmotd.motd.builder.hover.platforms.BukkitHover;
import me.blueslime.pixelmotd.motd.platforms.BukkitPing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;

public class ServerListPingListener extends BukkitPluginListener implements Listener, Ping {
    private final BukkitPing builder;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private String unknown;

    public ServerListPingListener(PixelMOTD<?> plugin) {
        super(plugin);
        register();

        builder = new BukkitPing(
                getBasePlugin(),
                new BukkitFavicon(
                        getBasePlugin()
                ),
                new BukkitHover(
                        getBasePlugin()
                )
        );

        reload();
    }

    @Override
    public void reload() {
        this.unknown = getSettings().getString("settings.unknown-player", "unknown#1");

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        this.isWhitelisted = whitelist.getStatus("enabled") &&
                whitelist.getStatus("motd");

        this.isBlacklisted = blacklist.getStatus("enabled") &&
                blacklist.getStatus("motd");

        builder.update();
    }

    @EventHandler
    public void on(ServerListPingEvent event) {
        final InetAddress address = event.getAddress();

        final String user = getPlayerDatabase().getPlayer(address.getHostAddress(), unknown);

        if (isBlacklisted && getBlacklist().getStringList("players.by-name").contains(user)) {
            builder.execute(MotdType.BLACKLIST, event, -1, user);
            return;
        }

        if (isWhitelisted) {
            builder.execute(MotdType.WHITELIST, event, -1, user);
            return;
        }

        builder.execute(MotdType.NORMAL, event, -1, user);
    }

    @Override
    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return builder;
    }
}
