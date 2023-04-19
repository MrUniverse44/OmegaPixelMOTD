package me.blueslime.pixelmotd.loader;

import me.blueslime.pixelmotd.utils.storage.PluginFiles;
import me.blueslime.slimelib.SlimeFiles;
import me.blueslime.slimelib.SlimePlugin;
import me.blueslime.slimelib.loader.BaseSlimeLoader;
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

    @Override
    public <O extends Enum<O> & SlimeFiles> void setFiles(Class<O> clazz) {
        fileStorage(
                new PluginFiles(
                        getPlugin()
                ).setEnums(
                        process(clazz)
                )
        );
    }

    private static <T extends Enum<T> & SlimeFiles> SlimeFiles[] process(Class<T> paramClass) {
        return paramClass.getEnumConstants();
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