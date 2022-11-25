package me.blueslime.pixelmotd.motd.builder.favicon;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.utils.internal.storage.PluginStorage;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class FaviconController<T, I> {
    private final PluginStorage<MotdType, Map<String, Icon<I>>> icons = PluginStorage.initAsHash();

    private final PixelMOTD<T> plugin;

    private final SlimeLogs logs;

    public FaviconController(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.plugin = plugin;
        this.logs = logs;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        icons.clear();

        for (MotdType motdType : MotdType.values()) {
            load(motdType);
        }

    }

    private void load(MotdType motdType) {

        final Map<String, Icon<I>> iconsPerType = icons.get(
                motdType, new HashMap<>()
        );

        File data = new File(plugin.getDataFolder(), "icons");

        File[] files = data.listFiles((d, fn) -> fn.endsWith(".png"));

        if (files == null) {
            icons.replace(motdType, iconsPerType);
            return;
        }

        for (File icon : files) {
            iconsPerType.put(
                    icon.getName(),
                    createIcon(motdType, icon)
            );
        }
        icons.replace(motdType, iconsPerType);
    }

    public Icon<I> createIcon(MotdType motdType, File icon) {
        return null;
    }

    public I getFavicon(MotdType motdType, String key) {
        Map<String, Icon<I>> icons = this.icons.get(motdType);

        if (icons == null) {

            load(motdType);

            icons = this.icons.get(motdType);
        }

        if (key.equalsIgnoreCase("RANDOM")) {
            List<Icon<I>> values = new ArrayList<>(icons.values());

            int randomIndex = ThreadLocalRandom.current().nextInt(values.size());

            return values.get(randomIndex).getFavicon();
        }

        if (icons.containsKey(key)) {
            return icons.get(key).getFavicon();
        }

        return null;
    }

    @SuppressWarnings("unused")
    public PluginStorage<MotdType, Map<String, Icon<I>>> getIcons() {
        return icons;
    }

    public PixelMOTD<T> getPlugin() {
        return plugin;
    }

    public SlimeLogs getLogs() {
        return logs;
    }
}
