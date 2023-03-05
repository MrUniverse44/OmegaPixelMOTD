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

    private void load() {
        final ConfigurationHandler control = plugin.getSettings();

        this.unknown = control.getString("settings.unknown-player", "unknown#1");

        ConfigurationHandler whitelist = plugin.getConfiguration(Configuration.WHITELIST);
        ConfigurationHandler blacklist = plugin.getConfiguration(Configuration.BLACKLIST);

        this.isWhitelisted = whitelist.getStatus("enabled") &&
                whitelist.getStatus("motd");

        this.isBlacklisted = blacklist.getStatus("enabled") &&
                blacklist.getStatus("motd");
    }

    @EventHandler
    public void onPing(ServerListPingEvent ping) {

        final InetAddress address = ping.getAddress();

        final String user = getPlayerDatabase().getPlayer(address.getHostAddress(), unknown);

        if (isBlacklisted && plugin.getConfiguration(Configuration.BLACKLIST).getStringList("players.by-name").contains(user)) {
            pingBuilder.execute(MotdType.BLACKLIST, ping, -1, user);
            return;
        }

        if (isWhitelisted) {
            pingBuilder.execute(MotdType.WHITELIST, ping, -1, user);
            return;
        }

        pingBuilder.execute(MotdType.NORMAL, ping, -1, user);
    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return pingBuilder;
    }

}
