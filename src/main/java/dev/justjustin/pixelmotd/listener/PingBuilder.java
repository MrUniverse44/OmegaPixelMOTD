package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.MotdType;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.utils.Extras;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.storage.FileStorage;

import java.util.*;

@SuppressWarnings("unused")
public abstract class PingBuilder<T, I, E, H> {
    private final Map<MotdType, List<String>> motdsMap = new HashMap<>();

    private final Random random = new Random();

    private final MotdBuilder<T, I> builder;

    private boolean playerSystem = false;

    private final PixelMOTD<T> plugin;


    private boolean iconSystem = true;

    private final Extras extras;

    public PingBuilder(PixelMOTD<T> plugin, MotdBuilder<T, I> builder) {
        this.plugin = plugin;
        this.builder = builder;
        this.extras = new Extras(plugin);
        load();
    }

    public void update() {
        load();
        builder.update();
    }

    private void load() {
        FileStorage fileStorage = plugin.getLoader().getFiles();

        iconSystem = fileStorage.getConfigurationHandler(SlimeFile.SETTINGS).getStatus("settings.icon-system");
        playerSystem = fileStorage.getConfigurationHandler(SlimeFile.SETTINGS).getStatus("settings.player-system.enabled",true);

        motdsMap.clear();

        for (MotdType motdType : MotdType.values()) {

            ConfigurationHandler configuration = plugin.getConfigurationHandler(motdType.getFile());

            List<String> motds;

            if (configuration == null) {
                motds = new ArrayList<>();
                plugin.getLogs().info("&aNo motds found in motd file: " + motdType.getFile().getFileName() + ", for motdType: " + motdType);
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

        List<String> list = control.getContent(
                type.toString(),
                false
        );

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

        return motds.get(random.nextInt(motds.size()));
    }

    public abstract void execute(MotdType motdType, E ping, int code, String user);

    public abstract H[] getHover(MotdType motdType, String path, int online, int max, String user);

    public abstract H[] addHoverLine(H[] player, H info);

    public PixelMOTD<T> getPlugin() {
        return plugin;
    }

    public MotdBuilder<T, I> getBuilder() {
        return builder;
    }

    public boolean isIconSystem() {
        return iconSystem;
    }

    public boolean isPlayerSystem() {
        return playerSystem;
    }

    public Extras getExtras() {
        return extras;
    }
}