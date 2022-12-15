package me.blueslime.pixelmotd.listener.manager.platforms;

import com.google.inject.Inject;
import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.spongepowered.api.Server;
import org.spongepowered.plugin.PluginContainer;

@SuppressWarnings("unused")
public class SpongeListenerManager extends ListenerManager<Server> {

    @Inject
    private PluginContainer container;

    public SpongeListenerManager(PixelMOTD<?> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public void register() {
    }

    @Override
    public void update() {
    }

}

