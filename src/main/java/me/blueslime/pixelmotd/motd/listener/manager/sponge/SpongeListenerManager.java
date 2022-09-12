package me.blueslime.pixelmotd.motd.listener.manager.sponge;

import com.google.inject.Inject;
import me.blueslime.pixelmotd.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.listener.Ping;
import org.spongepowered.api.Server;
import org.spongepowered.plugin.PluginContainer;

@SuppressWarnings("unused")
public class SpongeListenerManager implements ListenerManager {

    private final SlimeLogs logs;

    @Inject
    private PluginContainer container;

    @SuppressWarnings("unchecked")
    public <T> SpongeListenerManager(T plugin, SlimeLogs logs) {
        PixelMOTD<Server> slimePlugin = (PixelMOTD<Server>) plugin;
        this.logs = logs;
    }

    @Override
    public void register() {
    }

    @Override
    public void update() {
    }

    @Override
    public Ping getPing() {
        return null;
    }
}

