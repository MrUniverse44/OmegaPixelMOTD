package me.blueslime.omegapixelmotd.modules.listeners.sponge;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.Listeners;
import me.blueslime.omegapixelmotd.modules.listeners.sponge.list.ServerPingListener;
import me.blueslime.wardenplugin.modules.list.SpongeModule;

public class SpongeListeners extends SpongeModule {
    private final Listeners listeners = new Listeners();
    private final OmegaPixelMOTD plugin;

    public SpongeListeners(OmegaPixelMOTD plugin) {
        super(plugin.cast());
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        listeners.registerListener(
            new ServerPingListener(plugin)
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
