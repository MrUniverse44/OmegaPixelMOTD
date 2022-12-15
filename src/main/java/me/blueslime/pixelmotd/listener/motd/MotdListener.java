package me.blueslime.pixelmotd.listener.motd;

import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.motd.MotdType;

public abstract class MotdListener<T> {
    private final PixelMOTD<T> plugin;

    private CachedMotd current;

    public MotdListener(PixelMOTD<T> plugin) {
        this.plugin = plugin;
    }

    public void randomMotd(MotdType motdType) {

    }
    public abstract void register();

    public T getPlugin() {
        return plugin.getPlugin();
    }

    public PixelMOTD<T> getManager() {
        return plugin;
    }

    public CachedMotd getCurrentMotd() {
        return current;
    }

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }
}
