package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.players.PlayerDatabase;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.logs.SlimeLogs;

public abstract class ConnectionListener<T, E, S> {

    private final PixelMOTD<T> plugin;

    private boolean isWhitelisted;

    private boolean isBlacklisted;

    public ConnectionListener(PixelMOTD<T> plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        isWhitelisted = plugin.getLoader().getFiles().getControl(SlimeFile.MODES).getStatus("whitelist.global.enabled", false);
        isBlacklisted = plugin.getLoader().getFiles().getControl(SlimeFile.MODES).getStatus("blacklist.global.enabled", false);
    }

    public void update() {
        load();
    }

    public abstract void execute(E event);

    public abstract S colorize(String message);

    public String replace(String message, String key, String username, String uniqueId) {
        Control settings = getControl();

        return message.replace("%username%", username)
                .replace("%nick%", username)
                .replace("%uniqueId%", uniqueId)
                .replace("%uuid%", uniqueId)
                .replace("%reason%", settings.getString(key + ".reason", ""))
                .replace("%author%", settings.getString(key + ".author", ""));
    }

    public boolean hasWhitelist() {
        return isWhitelisted;
    }

    public boolean hasBlacklist() {
        return isBlacklisted;
    }

    public Control getControl() {
        return plugin.getLoader().getFiles().getControl(SlimeFile.MODES);
    }

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }

    public PlayerDatabase getPlayerDatabase() {
        return plugin.getListenerManager().getPing().getPlayerDatabase();
    }

}
