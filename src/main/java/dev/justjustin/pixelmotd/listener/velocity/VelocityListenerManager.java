package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.initialization.velocity.VelocityMOTD;
import dev.justjustin.pixelmotd.listener.velocity.events.ProxyPingListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;

public class VelocityListenerManager implements ListenerManager {

    private final PixelMOTD<ProxyServer> slimePlugin;

    private final ProxyPingListener listener;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> VelocityListenerManager(T plugin, SlimeLogs logs) {
        this.slimePlugin = (PixelMOTD<ProxyServer>) plugin;
        this.listener = new ProxyPingListener(slimePlugin, logs);
        this.logs = logs;
    }

    public void register(VelocityMOTD plugin) {
        slimePlugin.getPlugin().getEventManager().register(
                plugin,
                listener
        );

        logs.info("ProxyPingListener has been registered");
    }

    @Override
    public void register() {
        slimePlugin.getPlugin().getEventManager().register(
                slimePlugin.getPlugin(),
                listener
        );

        logs.info("ProxyPingListener has been registered");
    }

    @Override
    public void update() {
        listener.update();
    }
}
