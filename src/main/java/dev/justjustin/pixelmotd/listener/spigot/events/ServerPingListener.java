package dev.justjustin.pixelmotd.listener.spigot.events;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.listener.Ping;
import dev.justjustin.pixelmotd.listener.spigot.SpigotMotdBuilder;
import dev.justjustin.pixelmotd.listener.spigot.SpigotPingBuilder;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.storage.FileStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;

public class ServerPingListener implements Ping, Listener {

    private final PixelMOTD<JavaPlugin> slimePlugin;

    private final SpigotPingBuilder pingBuilder;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private MotdType type;

    private Control modes;

    public ServerPingListener(PixelMOTD<JavaPlugin> slimePlugin) {
        this.pingBuilder = new SpigotPingBuilder(
                slimePlugin,
                new SpigotMotdBuilder(
                        slimePlugin,
                        slimePlugin.getLogs()
                )
        );

        this.slimePlugin = slimePlugin;
        load();
    }

    public void update() {
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

        if (control.getString("settings.default-priority-motd", "DEFAULT").equalsIgnoreCase("HEX")) {
            type = MotdType.NORMAL_HEX;
        }

        isWhitelisted = modes.getStatus("whitelist.global.enabled") && modes.getStatus("whitelist.global.enable-motd");
        isBlacklisted = modes.getStatus("blacklist.global.enabled") && modes.getStatus("blacklist.global.enable-motd");
    }

    @EventHandler
    public void onPing(ServerListPingEvent ping) {

        final InetAddress address = ping.getAddress();

        final String user = getPlayerDatabase().getPlayer(address.getHostAddress());

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

}
