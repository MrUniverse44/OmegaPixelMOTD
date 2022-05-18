package dev.justjustin.pixelmotd;

import dev.justjustin.pixelmotd.commands.MainCommand;
import dev.justjustin.pixelmotd.players.PlayerHandler;
import dev.justjustin.pixelmotd.storage.MotdStorage;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.input.InputManager;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.loader.DefaultSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLog;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

@SuppressWarnings("unused")
public class PixelMOTD<T> implements SlimePlugin<T> {

    private final SlimePluginInformation information;

    private final ListenerManager listenerManager;

    private final BaseSlimeLoader<T> slimeLoader;

    private final PlayerHandler playerHandler;

    private final MotdStorage motdStorage;

    private final SlimePlatform platform;

    private final SlimeLogs logs;

    private final File folder;

    private final T plugin;

    public PixelMOTD(SlimePlatform platform, T plugin, File dataFolder) {
        this.playerHandler = PlayerHandler.fromPlatform(platform, plugin);

        this.slimeLoader   = new DefaultSlimeLoader<>(
                this,
                InputManager.createInputManager(
                        platform,
                        plugin
                )
        );

        this.folder        = dataFolder;
        this.platform      = platform;
        this.plugin        = plugin;
        this.information   = new SlimePluginInformation(platform, plugin);

        this.logs          = SlimeLog.createLogs(platform, this);

        getLoader().setFiles(SlimeFile.class);

        getLoader().init();

        getLoader().getCommands().register(new MainCommand<>(this));

        motdStorage = new MotdStorage(getLoader().getFiles());

        listenerManager = ListenerManager.createNewInstance(platform, this);

        listenerManager.register();
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    @Override
    public SlimePluginInformation getPluginInformation() {
        return information;
    }

    @Override
    public SlimePlatform getServerType() {
        return platform;
    }

    @Override
    public SlimeLogs getLogs() {
        return logs;
    }

    @Override
    public BaseSlimeLoader<T> getLoader() {
        return slimeLoader;
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public void reload() {
        slimeLoader.reload();

        motdStorage.update(
                slimeLoader.getFiles()
        );
    }

    @Override
    public File getDataFolder() {
        return folder;
    }
}
