package me.blueslime.pixelmotd.listener.bukkit;

import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.listener.bukkit.packets.PacketListener;
import me.blueslime.pixelmotd.listener.bukkit.player.PlayerLoginListener;
import me.blueslime.pixelmotd.listener.bukkit.player.PlayerTeleportListener;
import me.blueslime.pixelmotd.listener.bukkit.server.ServerListPingListener;
import org.bukkit.plugin.java.JavaPlugin;

public enum BukkitListener {
    SERVER_LIST_PING(ServerListPingListener.class),
    PLAYER_TELEPORT(PlayerTeleportListener.class),
    PLAYER_LOGIN(PlayerLoginListener.class),
    PACKET_LISTENER(PacketListener.class);

    private final Class<? extends PluginListener<JavaPlugin>> parent;

    BukkitListener(Class<? extends PluginListener<JavaPlugin>> parent) {
        this.parent = parent;
    }
    BukkitListener() {
        this.parent = null;
    }

    public Class<? extends PluginListener<JavaPlugin>> getParent() {
        return parent;
    }
}
