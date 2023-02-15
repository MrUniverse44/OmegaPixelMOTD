package me.blueslime.pixelmotd.motd.manager.platforms;

import me.blueslime.pixelmotd.motd.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.bungeecord.events.type.login.NormalLoginListener;
import me.blueslime.pixelmotd.listener.bungeecord.events.ProxyPingListener;
import me.blueslime.pixelmotd.listener.bungeecord.events.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.listener.bungeecord.events.abstracts.AbstractServerConnectListener;
import me.blueslime.pixelmotd.listener.bungeecord.events.type.server.NormalServerListener;
import me.blueslime.pixelmotd.utils.Extras;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeListenerManager extends ListenerManager<Plugin> {

    private final AbstractServerConnectListener connection;
    private final AbstractLoginListener login;
    private final ProxyPingListener listener;

    public BungeeListenerManager(PixelMOTD<Plugin> plugin) {
        super(plugin);

        this.connection = new NormalServerListener(plugin);
        this.listener   = new ProxyPingListener(plugin);
        this.login      = new NormalLoginListener(plugin);



    }

    @Override
    public void register() {
        final Plugin plugin = getPlugin().getPlugin();

        PluginManager manager = plugin.getProxy().getPluginManager();

        manager.registerListener(plugin, connection);
        manager.registerListener(plugin, listener);
        manager.registerListener(plugin, login);


        getLogs().info("ProxyPingListener has been registered to the proxy.");
    }

    @Override
    public void update() {
        connection.update();
        listener.update();
        login.update();
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
