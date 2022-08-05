package me.blueslime.pixelmotd.listener.sponge;

import com.google.inject.Inject;
import me.blueslime.pixelmotd.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.listener.sponge.events.ServerPingListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

@SuppressWarnings("unused")
public class SpongeListenerManager implements ListenerManager {

    private final ServerPingListener listener;

    private final SlimeLogs logs;

    @Inject
    private PluginContainer container;

    @SuppressWarnings("unchecked")
    public <T> SpongeListenerManager(T plugin, SlimeLogs logs) {
        PixelMOTD<Server> slimePlugin = (PixelMOTD<Server>) plugin;
        this.listener = new ServerPingListener(slimePlugin, logs);
        this.logs = logs;
    }

    @Override
    public void register() {
        Sponge.eventManager().registerListeners(container, listener);

        logs.info("ServerPingListener has been registered to the server.");
    }

    @Override
    public void update() {
        listener.update();
    }

    @Override
    public Ping getPing() {
        return listener;
    }
}
