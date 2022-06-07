package dev.justjustin.pixelmotd.listener.bungeecord;

import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.bungeecord.events.ProxyPingListener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeListenerManager implements ListenerManager {

    private final PixelMOTD<Plugin> slimePlugin;
    private final ProxyPingListener listener;

    @SuppressWarnings("unchecked")
    public <T> BungeeListenerManager(PixelMOTD<T> plugin) {
        this.slimePlugin = (PixelMOTD<Plugin>) plugin;
        this.listener = new ProxyPingListener(slimePlugin);
    }

    @Override
    public void register() {
        final Plugin plugin = slimePlugin.getPlugin();

        plugin.getProxy().getPluginManager().registerListener(plugin, listener);

    }

    @Override
    public void update() {
        listener.update();
    }
}
