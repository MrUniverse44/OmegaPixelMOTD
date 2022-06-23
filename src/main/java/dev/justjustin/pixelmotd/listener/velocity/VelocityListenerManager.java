package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.initialization.velocity.VelocityMOTD;
import dev.justjustin.pixelmotd.listener.Ping;
import dev.justjustin.pixelmotd.listener.velocity.events.ProxyPingListener;
import dev.justjustin.pixelmotd.listener.velocity.events.abstracts.AbstractLoginListener;
import dev.justjustin.pixelmotd.listener.velocity.events.abstracts.AbstractServerConnectListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;

public class VelocityListenerManager implements ListenerManager {


    private final AbstractServerConnectListener serverListener;

    private final AbstractLoginListener loginListener;

    private final PixelMOTD<ProxyServer> slimePlugin;

    private final ProxyPingListener listener;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> VelocityListenerManager(T plugin, SlimeLogs logs) {
        this.logs = logs;

        this.slimePlugin = (PixelMOTD<ProxyServer>) plugin;
        this.listener    = new ProxyPingListener(slimePlugin, logs);
        this.serverListener = new AbstractServerConnectListener(slimePlugin);
        this.loginListener  = new AbstractLoginListener(slimePlugin);
    }

    public void register(VelocityMOTD plugin) {
        EventManager manager = plugin.getServer().getEventManager();

        manager.register(
                plugin,
                listener
        );

        manager.register(
                plugin,
                loginListener
        );

        manager.register(
                plugin,
                serverListener
        );

        logs.info("LoginListener has been registered");
        logs.info("ProxyPingListener has been registered");
        logs.info("ServerConnectListener has been registered");
    }

    @Override
    public void register() {
        EventManager manager = slimePlugin.getPlugin().getEventManager();

        manager.register(
                slimePlugin.getPlugin(),
                listener
        );

        manager.register(
                slimePlugin.getPlugin(),
                loginListener
        );

        manager.register(
                slimePlugin.getPlugin(),
                serverListener
        );

        logs.info("LoginListener has been registered");
        logs.info("ProxyPingListener has been registered");
        logs.info("ServerConnectListener has been registered");
    }

    @Override
    public void update() {
        listener.update();
    }

    @Override
    public Ping getPing() {
        return listener;
    }
}
