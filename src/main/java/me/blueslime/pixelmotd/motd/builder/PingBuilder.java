package me.blueslime.pixelmotd.motd.builder;

import me.blueslime.slimelib.file.configuration.ConfigurationProvider;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.motd.MotdType;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.utils.internal.storage.PluginStorage;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public abstract class PingBuilder<T, I, E, H> {
    private final PluginStorage<MotdType, List<CachedMotd>> motdStorage = PluginStorage.initAsConcurrentHash();
    private final FaviconModule<T, I> faviconModule;
    private final HoverModule<H> hoverModule;

    private boolean iconSystem = false;

    private final PixelMOTD<T> plugin;

    private boolean debug = false;

    private final PluginPlaceholders pluginPlaceholders;

    public PingBuilder(PixelMOTD<T> plugin, FaviconModule<T, I> faviconModule, HoverModule<H> hoverModule) {
        this.faviconModule = faviconModule;
        this.hoverModule = hoverModule;
        this.plugin  = plugin;
        this.pluginPlaceholders = new PluginPlaceholders(plugin);
        load();
    }

    public void update() {
        load();
        faviconModule.update();
    }

    private void load() {
        ConfigurationHandler settings = plugin.getSettings();

        if (settings != null) {

            iconSystem = settings.getStatus("settings.icon-system", false);

            debug = settings.getStatus("settings.debug-mode", false);
        } else {

            debug = false;

            plugin.getLogs().error("Can't load settings data");
        }

        File[] files = plugin.getMotdFolder().listFiles((dir, name) -> name.contains(".yml"));

        if (files == null) {
            return;
        }

        for (MotdType type : MotdType.values()) {
            if (motdStorage.contains(type)) {
                motdStorage.get(type).clear();
            } else {
                motdStorage.set(
                        type,
                        new ArrayList<>()
                );
            }
        }

        ConfigurationProvider provider = plugin.getServerType()
                .getProvider()
                .getNewInstance();

        for (File file : files) {
            ConfigurationHandler motd = provider.create(
                    plugin.getLogs(),
                    file
            );
            MotdType type = MotdType.parseMotd(
                    motd.getInt("type")
            );
            motdStorage.get(type).add(
                    new CachedMotd(
                            motd
                    )
            );
        }
    }

    public List<CachedMotd> loadMotds(MotdType type) {
        File[] files = plugin.getMotdFolder().listFiles((dir, name) -> name.contains(".yml"));

        if (files == null) {
            return Collections.emptyList();
        }

        List<CachedMotd> motdList = new ArrayList<>();

        ConfigurationProvider provider = plugin.getServerType()
                .getProvider()
                .getNewInstance();

        for (File file : files) {
            ConfigurationHandler motd = provider.create(
                    plugin.getLogs(),
                    file
            );
            if (MotdType.parseMotd(
                    motd.getInt("type")
            ) == type) {
                motdList.add(
                        new CachedMotd(
                                motd
                        )
                );
            }
        }
        motdStorage.set(type, motdList);
        return motdList;
    }

    public CachedMotd getMotd(MotdType type) {
        List<CachedMotd> motds = motdStorage.get(type);

        if (motds == null) {
            motds = loadMotds(type);
        }

        if (motds.size() == 0) {
            return null;
        }

        if (motds.size() == 1) {
            return motds.get(0);
        }

        return motds.get(
                ThreadLocalRandom.current().nextInt(motds.size())
        );
    }

    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }

    public abstract void execute(MotdType motdType, E ping, int code, String user);

    public PixelMOTD<T> getPlugin() {
        return plugin;
    }

    public FaviconModule<T, I> getFavicon() {
        return faviconModule;
    }

    public HoverModule<H> getHoverModule() {
        return hoverModule;
    }

    public boolean isIconSystem() {
        return iconSystem;
    }

    public boolean isDebug() {
        return debug;
    }

    public PluginPlaceholders getExtras() {
        return pluginPlaceholders;
    }
}