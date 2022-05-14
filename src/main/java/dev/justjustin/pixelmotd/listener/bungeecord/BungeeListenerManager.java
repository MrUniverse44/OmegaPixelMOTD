package dev.justjustin.pixelmotd.listener.bungeecord;

import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.listener.bungeecord.events.ProxyPingListener;
import dev.mruniverse.slimelib.SlimePlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeListenerManager implements ListenerManager {

    private final SlimePlugin<Plugin> slimePlugin;

    @SuppressWarnings("unchecked")
    public <T> BungeeListenerManager(SlimePlugin<T> plugin) {
        this.slimePlugin = (SlimePlugin<Plugin>) plugin;
    }

    @Override
    public void register() {
        final Plugin plugin = slimePlugin.getPlugin();

        plugin.getProxy().getPluginManager().registerListener(plugin, new ProxyPingListener(slimePlugin));

    }
}
