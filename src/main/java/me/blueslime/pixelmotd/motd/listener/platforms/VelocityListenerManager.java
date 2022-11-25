package me.blueslime.pixelmotd.motd.listener.platforms;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.pixelmotd.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import me.blueslime.pixelmotd.extras.listeners.velocity.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.extras.listeners.velocity.abstracts.AbstractServerConnectListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.listener.Ping;

public class VelocityListenerManager implements ListenerManager {


    private final AbstractServerConnectListener serverListener;

    private final AbstractLoginListener loginListener;

    private final PixelMOTD<ProxyServer> slimePlugin;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> VelocityListenerManager(T plugin, SlimeLogs logs) {
        this.logs = logs;

        this.slimePlugin = (PixelMOTD<ProxyServer>) plugin;
        this.serverListener = new AbstractServerConnectListener(slimePlugin);
        this.loginListener  = new AbstractLoginListener(slimePlugin);
    }

    public void register(VelocityMOTD plugin) {
        EventManager manager = plugin.getServer().getEventManager();

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
        loginListener.update();
        serverListener.update();
    }

    @Override
    public Ping getPing() {
        return null;
    }
}

