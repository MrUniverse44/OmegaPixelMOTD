package me.blueslime.pixelmotd.listener;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.list.PluginList;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.util.UUID;

public abstract class ConnectionListener<T, E, S> {

    private final PixelMOTD<T> plugin;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    public ConnectionListener(PixelMOTD<T> plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        ConfigurationHandler whitelist = plugin.getConfiguration(Configuration.WHITELIST);
        ConfigurationHandler blacklist = plugin.getConfiguration(Configuration.BLACKLIST);


        this.isWhitelisted = whitelist.getStatus("enabled");
        this.isBlacklisted = blacklist.getStatus("enabled");
    }

    public void update() {
        load();
    }

    public PluginList getPlace() {
        return PluginList.fromPlatform(plugin.getServerType());
    }

    public abstract void execute(E event);

    public abstract S colorize(String message);

    public String replace(String message, boolean wl, String key, String username, String uniqueId) {
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

        return getExtras().replace(
                message.replace("%username%", username)
                    .replace("%nick%", username)
                    .replace("%uniqueId%", uniqueId)
                    .replace("%uuid%", uniqueId),
                plugin.getPlayerHandler().getPlayersSize(),
                plugin.getPlayerHandler().getMaxPlayers(),
                username
        );
    }

    public boolean hasWhitelist() {
        return isWhitelisted;
    }

    public boolean hasBlacklist() {
        return isBlacklisted;
    }

    public ConfigurationHandler getSettings() {
        return plugin.getConfigurationHandler(Configuration.SETTINGS);
    }

    public boolean checkPlayer(ListType listType, String path, String username) {
        if (path.equals("global")) {
            return plugin.getConfiguration(listType.getFile()).getStringList("players.by-name").contains(username);
        }
        return plugin.getConfiguration(listType.getFile()).getStringList(path + ".players.by-name").contains(username);
    }

    public boolean checkUUID(ListType listType, String path, UUID uniqueId) {
        if (path.equals("global")) {
            return plugin.getConfiguration(listType.getFile()).getStringList("players.by-uuid").contains(uniqueId.toString());
        }
        return plugin.getConfiguration(listType.getFile()).getStringList(path + ".players.by-uuid").contains(uniqueId.toString());
    }

    public ConfigurationHandler getWhitelist() {
        return plugin.getConfiguration(Configuration.WHITELIST);
    }

    public ConfigurationHandler getBlacklist() {
        return plugin.getConfiguration(Configuration.BLACKLIST);
    }

    public PluginPlaceholders getExtras() {
        return plugin.getListenerManager().getExtras();
    }

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }

    public PlayerDatabase getPlayerDatabase() {
        return plugin.getListenerManager().getDatabase();
    }

}
