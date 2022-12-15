package me.blueslime.pixelmotd;

import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import me.blueslime.pixelmotd.utils.NotFoundLanguageException;
import me.blueslime.pixelmotd.external.MetricsHandler;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.servers.ServerHandler;
import me.blueslime.pixelmotd.utils.FileUtilities;
import me.blueslime.pixelmotd.utils.Updater;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.ConfigurationProvider;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLogger;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.utils.internal.LoggerSetup;
import me.blueslime.pixelmotd.utils.internal.loader.PluginLoader;

import java.io.File;

@SuppressWarnings("unused")
public class PixelMOTD<T> implements SlimePlugin<T> {

    private final SlimePluginInformation information = new SlimePluginInformation(
            getServerType(),
            getPlugin()
    );

    private final ListenerManager listenerManager;

    private final BaseSlimeLoader<T> slimeLoader = new PluginLoader<>(this);

    private final PlayerHandler playerHandler = PlayerHandler.fromPlatform(
            getServerType(),
            getPlugin()
    );

    private final ServerHandler serverHandler = ServerHandler.fromPlatform(
            getServerType(),
            getPlugin()
    );

    private ConfigurationHandler messages;

    private final SlimePlatform platform;

    private final SlimeLogs logs = SlimeLogger.createLogs(
            getServerType(),
            this
    );;

    private final File folder;

    private final File lang;

    private final T plugin;

    public PixelMOTD(SlimePlatform platform, T plugin, File dataFolder) {

        this.folder        = dataFolder;
        this.platform      = platform;
        this.plugin        = plugin;

        LoggerSetup.initialize(logs);

        getLoader().setFiles(SlimeFile.class);

        getLoader().init();

        listenerManager = ListenerManager.createNewInstance(
                platform,
                this,
                logs
        );

        if (platform != SlimePlatform.VELOCITY && platform != SlimePlatform.SPONGE) {
            listenerManager.register();
        }

        if (getSettings().getStatus("settings.update-check", true)) {
            if (getSettings().getStatus("settings.auto-download-updates", true)) {
                new Updater(logs, information.getVersion(), 37177, getDataFolder(), Updater.UpdateType.CHECK_DOWNLOAD);
            } else {
                new Updater(logs, information.getVersion(), 37177, getDataFolder(), Updater.UpdateType.VERSION_CHECK);
            }

        }

        lang = new File(dataFolder, "lang");

        if (!lang.exists()) {
            loadMessageFile("cz", "en", "it", "de", "es", "jp", "pl", "zh-CN", "zh-TW", "he_IL", "id", "ko");
        }

        String code = getSettings().getString("settings.language", "en");

        File langFile = new File(
                lang,
                "messages_" + code + ".yml"
        );

        if (langFile.exists()) {
            ConfigurationProvider provider = getServerType().getProvider().getNewInstance();

            messages = provider.create(
                    logs,
                    langFile
            );

            logs.info("Messages are loaded from Lang files successfully.");
        } else {
            logs.error("Can't load messages correctly, debug will be showed after this message:");
            logs.debug("Language file of messages: " + langFile.getAbsolutePath());
            logs.debug("Language name file of messages: " + langFile.getName());

        }

        MetricsHandler.fromPlatform(
                platform,
                plugin
        ).initialize();
    }

    private void loadMessageFile(String... files) {
        for (String file : files) {
            FileUtilities.load(
                    logs,
                    lang,
                    file + ".yml",
                    "/lang/" + file + ".yml"
            );
        }
    }

    public ConfigurationHandler getCommandSettings() {
        return getConfigurationHandler(SlimeFile.COMMANDS);
    }
    public ConfigurationHandler getMessages() {
        if (messages == null) {
            exception();
        }
        return messages;
    }

    private void exception() {
        new NotFoundLanguageException("The current language in the settings file doesn't exists, probably you will see errors in console").printStackTrace();
    }

    public ConfigurationHandler getSettings() {
        return getConfigurationHandler(SlimeFile.SETTINGS);
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
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

        listenerManager.update();
    }

    @Override
    public File getDataFolder() {
        return folder;
    }
}
