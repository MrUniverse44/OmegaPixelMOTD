package me.blueslime.pixelmotd.motd.builder;

import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.MotdType;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.Extras;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public abstract class PingBuilder<T, I, E, H> {
    private final Map<MotdType, List<String>> motdsMap = new ConcurrentHashMap<>();
    private final FaviconModule<T, I> faviconModule;
    private final HoverModule<H> hoverModule;

    private boolean playerSystem = false;

    private final PixelMOTD<T> plugin;

    private boolean debug = false;


    private boolean iconSystem = true;

    private final Extras extras;

    public PingBuilder(PixelMOTD<T> plugin, FaviconModule<T, I> faviconModule, HoverModule<H> hoverModule) {
        this.faviconModule = faviconModule;
        this.hoverModule = hoverModule;
        this.plugin  = plugin;
        this.extras  = new Extras(plugin);
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
            playerSystem = settings.getStatus("settings.player-system.enabled", true);

            debug = settings.getStatus("settings.debug-mode", false);
        } else {
            iconSystem = false;
            playerSystem = true;

            debug = false;

            plugin.getLogs().error("Can't load settings data");
        }

        motdsMap.clear();

        for (MotdType motdType : MotdType.values()) {

            ConfigurationHandler configuration = plugin.getConfigurationHandler(motdType.getFile());

            List<String> motds;

            if (configuration == null) {
                motds = new ArrayList<>();

                if (isDebug()) {
                    plugin.getLogs().info("&aNo motds found in motd file: " + motdType.getFile().getFileName() + ", for motdType: " + motdType);
                }
            } else {
                motds = configuration.getContent(
                        motdType.toString(),
                        false
                );
            }

            motdsMap.put(
                    motdType,
                    motds
            );
        }
    }

    public List<String> loadMotds(MotdType type) {
        ConfigurationHandler control = plugin.getConfigurationHandler(type.getFile());

        List<String> list;

        if (control != null) {
            list = control.getContent(
                    type.toString(),
                    false
            );
        } else {
            list = new ArrayList<>();
        }

        motdsMap.put(
                type,
                list
        );
        return list;
    }

    public String getMotd(MotdType type) {
        List<String> motds = motdsMap.get(type);

        if (motds == null) {
            motds = loadMotds(type);
        }

        if (motds.size() == 0) {
            return "8293829382382732127413475y42732749832748327472fyfs";
        }

        if (motds.size() == 1) {
            return motds.get(0);
        }

        return motds.get(ThreadLocalRandom.current().nextInt(motds.size()));
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

    public boolean isPlayerSystem() {
        return playerSystem;
    }

    public boolean isDebug() {
        return debug;
    }

    public Extras getExtras() {
        return extras;
    }
}