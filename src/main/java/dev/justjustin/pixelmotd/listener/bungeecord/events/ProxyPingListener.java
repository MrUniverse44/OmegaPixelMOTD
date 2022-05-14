package dev.justjustin.pixelmotd.listener.bungeecord.events;

import dev.mruniverse.slimelib.SlimePlugin;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyPingListener implements Listener {

    private final SlimePlugin<Plugin> slimePlugin;

    public ProxyPingListener(SlimePlugin<Plugin> slimePlugin) {
        this.slimePlugin = slimePlugin;
    }

}
