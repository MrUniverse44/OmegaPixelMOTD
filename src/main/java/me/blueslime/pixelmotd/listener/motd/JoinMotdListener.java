package me.blueslime.pixelmotd.listener.motd;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinMotdListener<T, E, S> {

    private final List<String> blacklist = new ArrayList<>();

    private final PixelMOTD<T> plugin;

    private boolean isGlobalEnabled;

    public JoinMotdListener(PixelMOTD<T> plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        blacklist.clear();

    }

    public void update() {
        load();
    }

    public abstract void execute(E event);

    public abstract S colorize(String message);

    public String replace(String message, String key, String username, String uniqueId) {
        return getExtras().replace(
                message.replace("%username%", username)
                        .replace("%player%", username)
                        .replace("%nick%", username)
                        .replace("%user%", username)
                        .replace("%uniqueId%", uniqueId)
                        .replace("%uuid%", uniqueId),
                plugin.getPlayerHandler().getPlayersSize(),
                plugin.getPlayerHandler().getMaxPlayers(),
                username
        );
    }

    public boolean hasGlobalServer() {
        return isGlobalEnabled;
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
