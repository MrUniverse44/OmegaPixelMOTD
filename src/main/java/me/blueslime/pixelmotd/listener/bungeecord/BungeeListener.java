package me.blueslime.pixelmotd.listener.bungeecord;

import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.listener.bungeecord.player.LoginListener;
import me.blueslime.pixelmotd.listener.bungeecord.proxy.ProxyPingListener;
import me.blueslime.pixelmotd.listener.bungeecord.server.ServerConnectListener;
import net.md_5.bungee.api.plugin.Plugin;

public enum BungeeListener {
    SERVER_CONNECT(ServerConnectListener.class),
    PROXY_PING(ProxyPingListener.class),
    LOGIN(LoginListener.class);

    private final Class<? extends PluginListener<Plugin>> parent;

    BungeeListener(Class<? extends PluginListener<Plugin>> parent) {
        this.parent = parent;
    }
    BungeeListener() {
        this.parent = null;
    }

    public Class<? extends PluginListener<Plugin>> getParent() {
        return parent;
    }
}
