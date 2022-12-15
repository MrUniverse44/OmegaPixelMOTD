package me.blueslime.pixelmotd.listener.manager.platforms;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import me.blueslime.pixelmotd.extras.listeners.velocity.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.extras.listeners.velocity.abstracts.AbstractServerConnectListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;

public class VelocityListenerManager extends ListenerManager<ProxyServer> {


    private final AbstractServerConnectListener serverListener;

    private final AbstractLoginListener loginListener;

    public VelocityListenerManager(PixelMOTD<?> plugin, SlimeLogs logs) {
        super(plugin, logs);

        this.serverListener = new AbstractServerConnectListener(getPlugin());
        this.loginListener  = new AbstractLoginListener(getPlugin());
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

        getLogs().info("LoginListener has been registered");
        getLogs().info("ProxyPingListener has been registered");
        getLogs().info("ServerConnectListener has been registered");
    }

    @Override
    public void register() {
        EventManager manager = getPlugin().getPlugin().getEventManager();

        manager.register(
                getPlugin().getPlugin(),
                loginListener
        );

        manager.register(
                getPlugin().getPlugin(),
                serverListener
        );

        getLogs().info("LoginListener has been registered");
        getLogs().info("ProxyPingListener has been registered");
        getLogs().info("ServerConnectListener has been registered");
    }

    @Override
    public void update() {
        loginListener.update();
        serverListener.update();
    }
}

