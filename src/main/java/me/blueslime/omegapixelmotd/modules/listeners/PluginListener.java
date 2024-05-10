package me.blueslime.omegapixelmotd.modules.listeners;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.wardenplugin.WardenPlugin;
import me.blueslime.wardenplugin.logs.WardenLogs;

public abstract class PluginListener {
    protected final OmegaPixelMOTD plugin;

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

    public WardenLogs getLogs() {
        return plugin.getLogs();
    }
}
