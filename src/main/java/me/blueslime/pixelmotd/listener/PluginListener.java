package me.blueslime.pixelmotd.listener;

import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.exception.NotFoundLanguageException;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.servers.ServerHandler;
import me.blueslime.pixelmotd.utils.list.PluginList;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;

public interface PluginListener<T> {

    default String replace(String message, boolean wl, String key, String username, String uniqueId) {
        ConfigurationHandler settings;

        if (wl) {
            settings = getWhitelist();
        } else {
            settings = getBlacklist();
        }

        if (key.contains("global")) {
            message = message.replace(
                    "%reason%", settings.getString("reason", "")
            ).replace(
                    "%author%", settings.getString("author", "")
            );
        } else {
            key = key.replace(
                    "whitelist.", ""
            ).replace(
                    "blacklist.", ""
            );

            message = message.replace(
                    "%reason%", settings.getString(key + ".reason", "")
            ).replace(
                    "%author%", settings.getString(key + ".author", "")
            );
        }

        return getPlaceholders().replace(
                message.replace("%username%", username)
                        .replace("%nick%", username)
                        .replace("%uniqueId%", uniqueId)
                        .replace("%uuid%", uniqueId),
                getPlayerHandler().getPlayersSize(),
                getPlayerHandler().getMaxPlayers(),
                username
        );
    }

    default void reload() {
        //TODO: This reload method is empty, to be used a class need to @Override this method
    }

    ConfigurationHandler getMessages() throws NotFoundLanguageException;

    ConfigurationHandler getConfiguration(Configuration configuration);

    PluginPlaceholders getPlaceholders();

    ConfigurationHandler getWhitelist();

    ConfigurationHandler getBlacklist();

    ConfigurationHandler getSettings();

    PlayerDatabase getPlayerDatabase();

    PlayerHandler getPlayerHandler();

    ServerHandler getServerHandler();

    PluginList getSerializer();

    PixelMOTD<T> getBasePlugin();

    SlimeLogs getLogs();

    void register();

}
