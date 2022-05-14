package dev.justjustin.pixelmotd.listener.velocity.events;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.mruniverse.slimelib.SlimePlugin;

public class ProxyPingListener {

    private final SlimePlugin<ProxyServer> slimePlugin;

    public ProxyPingListener(SlimePlugin<ProxyServer> slimePlugin) {
        this.slimePlugin = slimePlugin;
    }

}
