package me.blueslime.omegapixelmotd.modules.listeners;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.wardenplugin.WardenPlugin;

public abstract class PluginListener {
    private final OmegaPixelMOTD plugin;

    public PluginListener(final OmegaPixelMOTD plugin) {
        this.plugin = plugin;
    }

    public abstract void initialize();
    public abstract void shutdown();
    public abstract void reload();

    public <T> T getPlugin() {
        if (plugin == null || plugin.getPlugin() == null) {
            return null;
        }

        WardenPlugin<T> plugin = this.plugin.cast();

        return plugin.getPlugin();
    }
}
