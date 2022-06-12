package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.initialization.velocity.VelocityMOTD;
import dev.justjustin.pixelmotd.listener.velocity.events.ProxyPingListener;

public class VelocityListenerManager implements ListenerManager {

    private final PixelMOTD<ProxyServer> slimePlugin;
    private final ProxyPingListener listener;

    @SuppressWarnings("unchecked")
    public <T> VelocityListenerManager(T plugin) {
        this.slimePlugin = (PixelMOTD<ProxyServer>) plugin;
        this.listener = new ProxyPingListener(slimePlugin);
    }

    public void register(VelocityMOTD plugin) {
        slimePlugin.getPlugin().getEventManager().register(
                plugin,
                listener
        );
    }

    @Override
    public void register() {
        slimePlugin.getPlugin().getEventManager().register(
                slimePlugin.getPlugin(),
                listener
        );
    }

    @Override
    public void update() {
        listener.update();
    }
}
