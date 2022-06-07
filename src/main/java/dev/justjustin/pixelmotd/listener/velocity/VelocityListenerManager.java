package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.velocity.events.ProxyPingListener;

public class VelocityListenerManager implements ListenerManager {

    private final PixelMOTD<ProxyServer> slimePlugin;
    private final ProxyPingListener listener;

    @SuppressWarnings("unchecked")
    public <T> VelocityListenerManager(T plugin) {
        this.slimePlugin = (PixelMOTD<ProxyServer>) plugin;
        this.listener = new ProxyPingListener(slimePlugin);
    }

    @Override
    public void register() {
        slimePlugin.getPlugin().getEventManager().register(
                slimePlugin,
                listener
        );
    }

    @Override
    public void update() {
        listener.update();
    }
}
