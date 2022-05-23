package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;
import java.util.*;

public abstract class MotdBuilder<T, I> {
    private final Map<MotdType, Map<String, Icon<I>>> icons = new HashMap<>();

    private final Random random = new Random();

    private final PixelMOTD<T> plugin;

    private final SlimeLogs logs;

    public MotdBuilder(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.plugin = plugin;
        this.logs = logs;
        load();
    }

    public void update() {
        load();
    }

    private void load() {
        icons.clear();

        load(MotdType.NORMAL);
        load(MotdType.WHITELIST);
        load(MotdType.BLACKLIST);
        load(MotdType.OUTDATED_SERVER);
        load(MotdType.OUTDATED_CLIENT);

    }

    private void load(MotdType motdType) {

        final Map<String, Icon<I>> iconsPerType = icons.computeIfAbsent(
                motdType,
                l -> new HashMap<>()
        );

        File data = new File(plugin.getDataFolder(), "icons");

        File folder = new File(data, motdType.toString());

        if (!folder.exists()) {
            logs.info("Icon-Folder (" + folder.getName() + ") has been created. [" + folder.mkdirs() + "]");
        }

        File[] files = folder.listFiles((d, fn) -> fn.endsWith(".png"));

        if (files == null) {
            icons.put(motdType, iconsPerType);
            return;
        }

        for (File icon : files) {
            iconsPerType.put(
                    icon.getName(),
                    createIcon(motdType, icon)
            );
        }
        icons.put(motdType, iconsPerType);
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

            int randomIndex = random.nextInt(values.size());

            return values.get(randomIndex).getFavicon();
        }

        if (icons.containsKey(key)) {
            return icons.get(key).getFavicon();
        }

        return null;
    }

    public Map<MotdType, Map<String, Icon<I>>> getIcons() {
        return icons;
    }

    public PixelMOTD<T> getPlugin() {
        return plugin;
    }

    public SlimeLogs getLogs() {
        return logs;
    }
}
