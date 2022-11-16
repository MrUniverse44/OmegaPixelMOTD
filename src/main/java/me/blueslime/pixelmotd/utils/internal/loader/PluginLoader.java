package me.blueslime.pixelmotd.utils.internal.loader;

import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.commands.PluginCommand;
import me.blueslime.pixelmotd.utils.internal.LoggerSetup;

public class PluginLoader<T> extends BaseSlimeLoader<T> {
    private final PixelMOTD<T> plugin;

    public PluginLoader(PixelMOTD<T> plugin) {
        super(plugin);

        this.plugin = plugin;
    }

    @Override
    public void init() {
        if (getFiles() != null) {
            getFiles().init();
        }

        getCommands().register(
                new PluginCommand<>(plugin)
        );
    }

    @Override
    public void shutdown() {
        getCommands().unregister();

        LoggerSetup.shutdown(
                plugin.getLogs(),
                plugin.getPluginInformation().getVersion()
        );
    }

    @Override
    public void reload() {
        if (getFiles() != null) {
            getFiles().reloadFiles();
        }
    }

}
