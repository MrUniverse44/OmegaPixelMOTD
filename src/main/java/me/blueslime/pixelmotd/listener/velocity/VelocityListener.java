package me.blueslime.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.listener.velocity.player.LoginListener;
import me.blueslime.pixelmotd.listener.velocity.proxy.ProxyPingListener;
import me.blueslime.pixelmotd.listener.velocity.server.ServerConnectListener;

public enum VelocityListener {
    SERVER_CONNECT(ServerConnectListener.class),
    PROXY_PING(ProxyPingListener.class),
    LOGIN(LoginListener.class);

    private final Class<? extends PluginListener<ProxyServer>> parent;

    VelocityListener(Class<? extends PluginListener<ProxyServer>> parent) {
        this.parent = parent;
    }
    VelocityListener() {
        this.parent = null;
    }

    public Class<? extends PluginListener<ProxyServer>> getParent() {
        return parent;
    }
}