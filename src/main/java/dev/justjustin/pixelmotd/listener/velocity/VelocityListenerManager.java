package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.ListenerManager;
import dev.mruniverse.slimelib.SlimePlugin;

public class VelocityListenerManager implements ListenerManager {

    private final SlimePlugin<ProxyServer> slimePlugin;

    @SuppressWarnings("unchecked")
    public <T> VelocityListenerManager(T plugin) {
        this.slimePlugin = (SlimePlugin<ProxyServer>) plugin;
    }

    @Override
    public void register() {

    }
}
