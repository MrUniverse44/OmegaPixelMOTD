package me.blueslime.pixelmotd.motd.builder.hover;

import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.slimelib.file.configuration.TextDecoration;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.PluginModule;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;

import java.util.List;

public abstract class HoverModule<T> extends PluginModule {

    public HoverModule(PixelMOTD<?> plugin) {
        super(plugin);
    }

    public List<T> generate(ConfigurationHandler configuration, String path, String user, int online, int max) {
        return generate(
                configuration.getStringList(TextDecoration.LEGACY, path),
                user,
                online,
                max
        );
    }
    public abstract List<T> generate(List<String> lines, String user, int online, int max);

    public abstract T[] convert(List<T> list);

    public PluginPlaceholders getExtras() {
        return getPlugin().getPlaceholders();
    }

    public boolean hasPlayers() {
        return getPlugin().getSettings().getBoolean("settings.player-system.enabled");
    }
}
