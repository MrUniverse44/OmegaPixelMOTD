package me.blueslime.omegapixelmotd.modules.listeners.bungeecord;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.Listeners;
import me.blueslime.omegapixelmotd.modules.listeners.bungeecord.list.ProxyPingListener;
import me.blueslime.omegapixelmotd.modules.listeners.bungeecord.list.ServerConnectListener;
import me.blueslime.wardenplugin.modules.list.BungeecordModule;

public class BungeecordListeners extends BungeecordModule {
    private final Listeners listeners = new Listeners();
    private final OmegaPixelMOTD plugin;

    public BungeecordListeners(OmegaPixelMOTD plugin) {
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
