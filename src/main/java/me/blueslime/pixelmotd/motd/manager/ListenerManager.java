package me.blueslime.pixelmotd.motd.manager;

import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;

public abstract class ListenerManager<P> {

    private final PlayerDatabase database = new PlayerDatabase();

    private final PixelMOTD<P> plugin;

    public ListenerManager(PixelMOTD<P> plugin) {
        this.plugin = plugin;
    }

    /**
     * Register the default listener
     */
    public abstract void register();

    public abstract void update();

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }

    public PlayerDatabase getDatabase() {
        return database;
    }

    public PixelMOTD<P> getPlugin() {
        return plugin;
    }

    public abstract boolean isPlayer();

    public abstract PluginPlaceholders getExtras();

}
