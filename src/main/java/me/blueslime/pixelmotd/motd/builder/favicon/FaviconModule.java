package me.blueslime.pixelmotd.motd.builder.favicon;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.utils.internal.storage.PluginStorage;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class FaviconModule<T, I> {
    private final PluginStorage<String, Icon<I>> icons = PluginStorage.initAsHash();
    private final PixelMOTD<T> plugin;

    public FaviconModule(PixelMOTD<T> plugin) {
        this.plugin = plugin;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        icons.clear();
        initialize();
    }

    private void initialize() {

        getLogs().info("Loading icons");
        File data = new File(plugin.getDataFolder(), "icons");

        if (!data.exists() && !data.mkdirs()) {
            getLogs().error("Can't initialize icons folder");
        }

        File[] files = data.listFiles((d, fn) -> fn.endsWith(".png"));

        if (files == null) {
            return;
        }

        for (File icon : files) {
            icons.set(
                    icon.getName(),
                    createIcon(icon)
            );
        }
    }

    public Icon<I> createIcon(File icon) {
        return createIcon(
                plugin.getLogs(),
                icon
        );
    }

    public abstract Icon<I> createIcon(SlimeLogs logs, File icon);

    public I getFavicon(String key) {
        if (key.equalsIgnoreCase("RANDOM")) {
            List<Icon<I>> values = icons.getValues();

            int randomIndex = ThreadLocalRandom.current().nextInt(values.size());

            return values.get(randomIndex).getFavicon();
        }

        if (icons.contains(key)) {
            return icons.get(key).getFavicon();
        }

        return null;
    }

    @SuppressWarnings("unused")
    public PluginStorage<String, Icon<I>> getIcons() {
        return icons;
    }

    public PixelMOTD<T> getPlugin() {
        return plugin;
    }

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }
}
