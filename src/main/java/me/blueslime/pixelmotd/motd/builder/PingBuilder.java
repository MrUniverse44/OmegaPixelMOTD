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
    private final PluginStorage<MotdType, List<CachedMotd>> hexStorage = PluginStorage.initAsConcurrentHash();
    private final FaviconModule<T, I> faviconModule;
    private final HoverModule<H> hoverModule;

    private boolean iconSystem = false;

    private boolean separated = false;

    private final PixelMOTD<T> plugin;

    private final PluginPlaceholders pluginPlaceholders;

    public PingBuilder(PixelMOTD<T> plugin, FaviconModule<T, I> faviconModule, HoverModule<H> hoverModule) {
        this.faviconModule = faviconModule;
        this.hoverModule = hoverModule;
        this.plugin  = plugin;
        this.pluginPlaceholders = new PluginPlaceholders(plugin);
    }

    public void update() {
        load();
        faviconModule.update();
    }

    private void load() {
        ConfigurationHandler settings = plugin.getSettings();

        if (settings != null) {

            iconSystem = settings.getStatus("settings.icon-system", false);
            separated = settings.getStatus("settings.hide-hex-motds-in-legacy-versions", true);

        } else {
            iconSystem = true;
            separated = true;

            plugin.getLogs().error("Can't load settings data");
        }

        File[] files = plugin.getMotdFolder().listFiles((dir, name) -> name.contains(".yml"));

        if (files == null) {
            return;
        }

        for (MotdType type : MotdType.values()) {
            if (hexStorage.contains(type)) {
                hexStorage.get(type).clear();
            } else {
                hexStorage.set(
                        type,
                        new ArrayList<>()
                );
            }
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

        if (!separated) {
            for (File file : files) {
                ConfigurationHandler motd = provider.create(
                        plugin.getLogs(),
                        file,
                        true
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
        } else {
            for (File file : files) {
                ConfigurationHandler motd = provider.create(
                        plugin.getLogs(),
                        file,
                        true
                );
                MotdType type = MotdType.parseMotd(
                        motd.getInt("type")
                );

                CachedMotd cachedMotd = new CachedMotd(motd);

                if (motd.getBoolean("hex-motd", false)) {
                    hexStorage.get(type).add(
                            cachedMotd
                    );
                } else {
                    motdStorage.get(type).add(
                            cachedMotd
                    );
                }
            }
        }
    }

    public List<CachedMotd> loadMotds(MotdType type) {
        File[] files = plugin.getMotdFolder().listFiles((dir, name) -> name.contains(".yml"));

        if (files == null) {
            return Collections.emptyList();
        }

        List<CachedMotd> motdList = new ArrayList<>();
        List<CachedMotd> hexList = new ArrayList<>();

        ConfigurationProvider provider = plugin.getServerType()
                .getProvider()
                .getNewInstance();

        for (File file : files) {
            ConfigurationHandler motd = provider.create(
                    plugin.getLogs(),
                    file,
                    true
            );
            if (MotdType.parseMotd(
                    motd.getInt("type")
            ) == type) {
                if (motd.getBoolean("hex-motd", false) && separated) {
                    hexList.add(
                            new CachedMotd(motd)
                    );
                } else {
                    motdList.add(
                            new CachedMotd(
                                    motd
                            )
                    );
                }
            }
        }
        motdStorage.set(type, motdList);
        hexStorage.set(type, hexList);
        return motdList;
    }

    public CachedMotd fetchMotd(MotdType type, int protocol) {
        List<CachedMotd> motds;

        if (separated && protocol >= 735) {
            motds = hexStorage.get(type);
        } else {
            motds = motdStorage.get(type);
        }

        if (motds == null) {
            motds = loadMotds(type);
        }

        if (motds.size() == 0) {
            if (type == MotdType.OUTDATED_CLIENT && (motdStorage.get(type).size() == 0 || (separated && hexStorage.get(type).size() == 0))) {
                return fetchMotd(MotdType.NORMAL, protocol);
            }
            if (type == MotdType.OUTDATED_SERVER && (motdStorage.get(type).size() == 0 || (separated && hexStorage.get(type).size() == 0))) {
                return fetchMotd(MotdType.NORMAL, protocol);
            }
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
        return plugin.getSettings().getBoolean("settings.debug-mode", false);
    }

    public PluginPlaceholders getExtras() {
        return pluginPlaceholders;
    }
}