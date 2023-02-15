package me.blueslime.pixelmotd;

import me.blueslime.pixelmotd.commands.PluginCommand;
import me.blueslime.pixelmotd.exception.NotFoundLanguageException;
import me.blueslime.pixelmotd.motd.manager.ListenerManager;
import me.blueslime.pixelmotd.loader.PluginLoader;
import me.blueslime.pixelmotd.metrics.MetricsHandler;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.servers.ServerHandler;
import me.blueslime.pixelmotd.utils.FileUtilities;
import me.blueslime.pixelmotd.utils.Updater;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.ConfigurationProvider;
import dev.mruniverse.slimelib.logs.SlimeLogger;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.utils.logger.LoggerSetup;

import java.io.File;

@SuppressWarnings("unused")
public class PixelMOTD<T> implements SlimePlugin<T> {
    private final NotFoundLanguageException LANGUAGE_EXCEPTION = new NotFoundLanguageException(
            "The current language in the settings file doesn't exists, probably you will see errors in console"
    );

    private final SlimePluginInformation information;

    private ListenerManager<T> listenerManager;

    private final PluginLoader<T> loader;

    private final PlayerHandler playerHandler;

    private final ServerHandler serverHandler;

    private ConfigurationHandler messages;

    private final SlimePlatform platform;

    private final SlimeLogs logs;

    private final File folder;

    private final File lang;

    private final T plugin;

    public PixelMOTD(SlimePlatform platform, T plugin, File dataFolder) {

        this.platform = platform;
        this.folder = dataFolder;
        this.plugin = plugin;

        this.logs = SlimeLogger.createLogs(
                platform,
                this,
                "PixelMOTD"
        );

        this.playerHandler = PlayerHandler.fromPlatform(platform, plugin);

        this.serverHandler = ServerHandler.fromPlatform(platform, plugin);

        this.information = new SlimePluginInformation(platform, plugin);

        this.loader = new PluginLoader<>(
                this
        );

        LoggerSetup.start(logs);

        this.loader.setFiles(Configuration.class);

        this.loader.init();

        this.loader.getCommands().register(new PluginCommand<>(this));

        if (getSettings().getStatus("settings.update-check", true)) {
            if (getSettings().getStatus("settings.auto-download-updates", true)) {
                new Updater(logs, information.getVersion(), 37177, getDataFolder(), Updater.UpdateType.CHECK_DOWNLOAD);
            } else {
                new Updater(logs, information.getVersion(), 37177, getDataFolder(), Updater.UpdateType.VERSION_CHECK);
            }

        }

        this.lang = new File(dataFolder, "lang");

        if (!lang.exists() && lang.mkdirs()) {
            loadMessageFile("cz", "en", "it", "de", "es", "jp", "pl", "zh_CN", "zh_TW", "he_IL", "id", "ko");
        } else {
            File[] files = lang.listFiles((dir, name) -> name.contains("messages_"));

            if (files != null && files.length >= 1) {
                try {
                    FileUtilities.copy(
                            lang,
                            new File(
                                    getDataFolder(),
                                    "backup-languages"
                            )
                    );
                } catch (Exception ignored) { }
                loadMessageFile("cz", "en", "it", "de", "es", "jp", "pl", "zh_CN", "zh_TW", "he_IL", "id", "ko");
            }
        }

        String code = getSettings().getString("settings.language", "en");

        File langFile = new File(
                lang,
                code + ".yml"
        );

        if (langFile.exists()) {
            ConfigurationProvider provider = platform.getProvider().getNewInstance();

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

    public void initialize(ListenerManager<T> manager) {
        this.listenerManager = manager;
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
        return getConfigurationHandler(Configuration.COMMANDS);
    }

    public ConfigurationHandler getMessages() throws NotFoundLanguageException {
        if (messages == null) {
            throw LANGUAGE_EXCEPTION;
        }
        return messages;
    }

    private void exception() {
        LANGUAGE_EXCEPTION.printStackTrace();
    }

    public ConfigurationHandler getSettings() {
        return getConfigurationHandler(Configuration.SETTINGS);
    }

    private void loadMessageFile(String file) {
        FileUtilities.load(
                logs,
                lang,
                "messages_" + file + ".yml",
                "/lang/messages_" + file + ".yml"
        );
    }

    public ListenerManager<T> getListenerManager() {
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
    public PluginLoader<T> getLoader() {
        return loader;
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public void reload() {
        loader.reload();

        listenerManager.update();
    }

    @Override
    public File getDataFolder() {
        return folder;
    }
}
