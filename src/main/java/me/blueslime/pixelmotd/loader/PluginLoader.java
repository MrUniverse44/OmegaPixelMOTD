package me.blueslime.pixelmotd.loader;

import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;

public class PluginLoader<T> extends BaseSlimeLoader<T> {

    public PluginLoader(SlimePlugin<T> plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        if (getFiles() != null) {
            getFiles().init();
        }
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