package me.blueslime.pixelmotd.extras.listeners;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.utils.Extras;
import me.blueslime.pixelmotd.extras.ListType;
import me.blueslime.pixelmotd.utils.WhitelistLocation;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.util.List;
import java.util.UUID;

public abstract class ConnectionListener<T, E, S> {

    private final PixelMOTD<T> plugin;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    private final Extras extras;

    public ConnectionListener(PixelMOTD<T> plugin) {
        this.extras = new Extras(plugin);
        this.plugin = plugin;
        load();
    }

    private void load() {
        isWhitelisted = plugin.getConfigurationHandler(SlimeFile.WHITELIST).getStatus("enabled", false);
        isBlacklisted = plugin.getConfigurationHandler(SlimeFile.BLACKLIST).getStatus("enabled", false);
    }

    public void update() {
        load();
    }

    public WhitelistLocation getPlace() {
        return WhitelistLocation.fromPlatform(plugin.getServerType());
    }

    public abstract void execute(E event);

    public abstract S colorize(String message);

    public String replace(String message, String username, String uniqueId) {
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

    public ConfigurationHandler getWhitelist() {
        return plugin.getConfigurationHandler(SlimeFile.WHITELIST);
    }

    public ConfigurationHandler getBlacklist() {
        return plugin.getConfigurationHandler(SlimeFile.BLACKLIST);
    }

    public ConfigurationHandler getSpecifiedList(ListType listType) {
        switch (listType) {
            case BLACKLIST:
                return plugin.getConfigurationHandler(SlimeFile.BLACKLIST_LIST);
            default:
            case WHITELIST:
                return plugin.getConfigurationHandler(SlimeFile.WHITELIST_LIST);
        }
    }

    public List<String> getPlayers(ListType listType) {
        return getSpecifiedList(listType).getStringList("names");
    }

    public List<String> getUniqueIds(ListType listType) {
        return getSpecifiedList(listType).getStringList("unique-ids");
    }

    public ConfigurationHandler getSettings() {
        return plugin.getConfigurationHandler(SlimeFile.SETTINGS);
    }

    public boolean checkPlayer(ListType listType, String path, String username) {
        if (path.isEmpty()) {
            return getPlayers(listType).contains(username);
        }
        return getSpecifiedList(listType).getStringList(path + ".names").contains(username);
    }

    public boolean checkUUID(ListType listType, String path, UUID uniqueId) {
        if (path.isEmpty()) {
            return getUniqueIds(listType).contains(uniqueId.toString());
        }
        return getSpecifiedList(listType).getStringList(path + ".unique-ids").contains(uniqueId.toString());
    }

    public Extras getExtras() {
        return extras;
    }

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }

    public PlayerDatabase getPlayerDatabase() {
        return plugin.getListenerManager().getPing().getPlayerDatabase();
    }
}