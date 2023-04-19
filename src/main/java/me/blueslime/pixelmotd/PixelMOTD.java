package me.blueslime.pixelmotd;

import me.blueslime.slimelib.SlimeFiles;
import me.blueslime.pixelmotd.commands.PluginCommand;
import me.blueslime.pixelmotd.exception.NotFoundLanguageException;
import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.loader.PluginLoader;
import me.blueslime.pixelmotd.loader.listener.ListenerLoader;
import me.blueslime.pixelmotd.metrics.MetricsHandler;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.servers.ServerHandler;
import me.blueslime.pixelmotd.utils.FileUtilities;
import me.blueslime.slimelib.SlimePlatform;
import me.blueslime.slimelib.SlimePlugin;
import me.blueslime.slimelib.SlimePluginInformation;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.slimelib.file.configuration.ConfigurationProvider;
import me.blueslime.slimelib.logs.SlimeLogger;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.utils.logger.LoggerSetup;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PixelMOTD<T> implements SlimePlugin<T> {
    private final NotFoundLanguageException LANGUAGE_EXCEPTION = new NotFoundLanguageException(
            "The current language in the settings file doesn't exists, probably you will see errors in console"
    );

    private final List<PluginListener<T>> listenerList = new ArrayList<>();

    private final SlimePluginInformation information;

    private final PluginPlaceholders placeholders;

    private final PluginLoader<T> loader;

    private final PlayerHandler playerHandler;

    private final ServerHandler serverHandler;

    private ConfigurationHandler messages;

    private final SlimePlatform platform;

    private final SlimeLogs logs;

    private final File folder;
    private final File motds;

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

        this.placeholders = new PluginPlaceholders(this);

        if (getSettings().getStatus("settings.update-check", true)) {
            //TODO: In the future the new auto updater will be created
            logs.info("&fUpdater has been disabled for now...");
        }

        this.motds = new File(dataFolder, "motds");
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

        if (!motds.exists() && motds.mkdirs()) {
            loadMotdFile("one", "two", "three", "four", "five", "six");
        } else {
            File[] files = motds.listFiles((dir, name) -> name.contains("server_motds"));

            if (files != null && files.length >= 1) {
                try {
                    FileUtilities.copy(
                            motds,
                            new File(
                                    getDataFolder(),
                                    "backup-motds"
                            ),
                            true
                    );
                } catch (IOException ignored) {}
                loadMotdFile("one", "two", "three", "four", "five", "six");
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
                    langFile,
                    true
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

        ListenerLoader.initialize(this);

        List<String> listeners = new ArrayList<>();

        for (PluginListener<?> listener : listenerList) {
            listeners.add(listener.getName());
        }

        getLogs().info(
                "[Debug Scheduler] &6Registered listeners for platform '&f" + getServerType().toString() + "&6':&f " + listeners.toString().replace(
                        "[",
                        ""
                ).replace(
                        "]",
                        ""
                ).replace(
                        "{",
                        ""
                ).replace(
                        "}",
                        ""
                )
        );
    }

    public void addListener(PluginListener<T> listener) {
        listenerList.add(listener);
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

    private void loadMotdFile(String... files) {
        for (String file : files) {
            FileUtilities.load(
                    logs,
                    motds,
                    file + ".yml",
                    "/motds/" + file + ".yml"
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

    public PluginPlaceholders getPlaceholders() {
        return placeholders;
    }

    public File getMotdFolder() {
        return motds;
    }

    private void exception() {
        LANGUAGE_EXCEPTION.printStackTrace();
    }

    public ConfigurationHandler getSettings() {
        return getConfigurationHandler(Configuration.SETTINGS);
    }

    public ConfigurationHandler getConfiguration(SlimeFiles configuration) {
        return getConfigurationHandler(configuration);
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

    public void reloadListeners() {
        for (PluginListener<T> listener : listenerList) {
            listener.reload();
        }
    }

    @Override
    public void reload() {
        loader.reload();

        reloadListeners();
    }

    @Override
    public File getDataFolder() {
        return folder;
    }
}
