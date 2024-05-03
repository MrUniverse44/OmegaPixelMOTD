package me.blueslime.omegapixelmotd.modules.listeners.velocity;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.Listeners;
import me.blueslime.omegapixelmotd.modules.listeners.velocity.list.ProxyPingListener;
import me.blueslime.omegapixelmotd.modules.listeners.velocity.list.ServerConnectListener;
import me.blueslime.wardenplugin.modules.list.VelocityModule;

public class VelocityListeners extends VelocityModule {
    private final Listeners listeners = new Listeners();
    private final OmegaPixelMOTD plugin;

    public VelocityListeners(OmegaPixelMOTD plugin) {
        super(plugin.cast());
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        listeners.registerListener(
            new ProxyPingListener(plugin),
            new ServerConnectListener(plugin)
        );

        listeners.initialize();
    }

    @Override
    public void shutdown() {
        listeners.shutdown();
    }

    @Override
    public void reload() {
        listeners.reload();
    }
}
