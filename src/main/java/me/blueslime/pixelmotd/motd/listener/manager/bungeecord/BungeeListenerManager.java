package me.blueslime.pixelmotd.motd.listener.manager.bungeecord;

import me.blueslime.pixelmotd.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.listener.Ping;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeListenerManager implements ListenerManager {

    private final PixelMOTD<Plugin> slimePlugin;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> BungeeListenerManager(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.logs = logs;

        this.slimePlugin   = (PixelMOTD<Plugin>) plugin;
    }

    @Override
    public void register() {
        final Plugin plugin = slimePlugin.getPlugin();

        PluginManager manager = plugin.getProxy().getPluginManager();

        //TODO: Register

        logs.info("ProxyPingListener has been registered to the proxy.");
    }

    @Override
    public void update() {
        //TODO: Update
    }

    @Override
    public Ping getPing() {
        return null;
    }

}
