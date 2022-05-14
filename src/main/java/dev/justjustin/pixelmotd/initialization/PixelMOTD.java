package dev.justjustin.pixelmotd.initialization;

import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.players.PlayerHandler;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.input.InputManager;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.loader.DefaultSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLog;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public class PixelMOTD<T> implements SlimePlugin<T> {

    private final BaseSlimeLoader<T> slimeLoader;

    private final PlayerHandler playerHandler;

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

        this.logs          = SlimeLog.createLogs(platform, this);

        getLoader().setFiles(SlimeFile.class);

        getLoader().init();
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
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
    }

    @Override
    public File getDataFolder() {
        return folder;
    }
}
