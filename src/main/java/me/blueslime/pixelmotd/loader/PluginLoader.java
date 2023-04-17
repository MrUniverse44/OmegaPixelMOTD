package me.blueslime.pixelmotd.loader;

import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import me.blueslime.pixelmotd.players.PlayerDatabase;

public class PluginLoader<T> extends BaseSlimeLoader<T> {

    private final PlayerDatabase database = new PlayerDatabase();

    public PluginLoader(SlimePlugin<T> plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        if (getFiles() != null) {
            getFiles().init();
        }
    }

    public PlayerDatabase getDatabase() {
        return database;
    }

    @Override
    public void shutdown() {
        getCommands().unregister();
    }

    @Override
    public void reload() {
        getFiles().reloadFiles();
    }


}