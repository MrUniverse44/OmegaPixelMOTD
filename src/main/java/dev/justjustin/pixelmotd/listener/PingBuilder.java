package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.MotdType;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.utils.Extras;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.storage.FileStorage;

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

        iconSystem = fileStorage.getControl(SlimeFile.SETTINGS).getStatus("settings.icon-system");
        playerSystem = fileStorage.getControl(SlimeFile.SETTINGS).getStatus("settings.player-system.enabled",true);

        motdsMap.clear();

        for (MotdType motdType : MotdType.values()) {

            Control control = fileStorage.getControl(motdType.getFile());

            List<String> motds = control.getContent(
                    motdType.toString(),
                    false
            );

            motdsMap.put(
                    motdType,
                    motds
            );

            plugin.getLogs().info("&3Motds loaded for type &f" + motdType + "&3, motds loaded: &f" + motds.toString().replace("[","").replace("]",""));
        }
    }

    public List<String> loadMotds(MotdType type) {
        Control control = plugin.getLoader().getFiles().getControl(type.getFile());

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