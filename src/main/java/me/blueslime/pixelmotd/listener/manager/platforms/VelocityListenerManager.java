package me.blueslime.pixelmotd.listener.manager.platforms;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import me.blueslime.pixelmotd.listener.velocity.events.ProxyPingListener;
import me.blueslime.pixelmotd.listener.velocity.events.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.listener.velocity.events.abstracts.AbstractServerConnectListener;
import me.blueslime.pixelmotd.utils.Extras;

public class VelocityListenerManager extends ListenerManager<ProxyServer> {

    private final AbstractServerConnectListener connection;
    private final AbstractLoginListener login;
    private final ProxyPingListener listener;

    public VelocityListenerManager(PixelMOTD<ProxyServer> plugin) {
        super(plugin);

        this.connection = new AbstractServerConnectListener(plugin);
        this.listener   = new ProxyPingListener(plugin);
        this.login      = new AbstractLoginListener(plugin);
    }

    public void register(VelocityMOTD plugin) {
        EventManager manager = plugin.getServer().getEventManager();

        manager.register(
                plugin,
                listener
        );

        manager.register(
                plugin,
                login
        );

        manager.register(
                plugin,
                connection
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
                listener
        );

        manager.register(
                getPlugin().getPlugin(),
                login
        );

        manager.register(
                getPlugin().getPlugin(),
                connection
        );

        getLogs().info("LoginListener has been registered");
        getLogs().info("ProxyPingListener has been registered");
        getLogs().info("ServerConnectListener has been registered");
    }

    @Override
    public void update() {
        listener.update();
        login.update();
        connection.update();
    }

    @Override
    public boolean isPlayer() {
        return listener.getPingBuilder().isPlayerSystem();
    }

    @Override
    public Extras getExtras() {
        return listener.getPingBuilder().getExtras();
    }
}
