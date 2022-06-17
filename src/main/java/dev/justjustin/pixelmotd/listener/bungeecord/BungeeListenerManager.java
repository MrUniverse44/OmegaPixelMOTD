package dev.justjustin.pixelmotd.listener.bungeecord;

import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.bungeecord.events.ProxyPingListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeListenerManager implements ListenerManager {

    private final PixelMOTD<Plugin> slimePlugin;

    private final ProxyPingListener listener;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> BungeeListenerManager(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.slimePlugin = (PixelMOTD<Plugin>) plugin;
        this.listener = new ProxyPingListener(slimePlugin, logs);
        this.logs = logs;
    }

    @Override
    public void register() {
        final Plugin plugin = slimePlugin.getPlugin();

        plugin.getProxy().getPluginManager().registerListener(plugin, listener);

        logs.info("ProxyPingListener has been registered to the proxy.");
    }

    @Override
    public void update() {
        listener.update();
    }
}
