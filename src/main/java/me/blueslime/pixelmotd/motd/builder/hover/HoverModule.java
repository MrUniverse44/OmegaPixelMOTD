package me.blueslime.pixelmotd.motd.builder.hover;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.PluginModule;
import me.blueslime.pixelmotd.utils.Extras;

import java.util.List;

public abstract class HoverModule<T> extends PluginModule {

    public HoverModule(PixelMOTD<?> plugin) {
        super(plugin);
    }

    public abstract List<T> generate(ConfigurationHandler configuration, String path, String user, int online, int max);

    public abstract T[] convert(List<T> list);

    public Extras getExtras() {
        return getPlugin().getListenerManager().getExtras();
    }

    public boolean hasPlayers() {
        return getPlugin().getListenerManager().isPlayer();
    }
}
